package net.study.resume.controller;

import com.nimbusds.oauth2.sdk.util.StringUtils;
import net.study.resume.entity.Profile;
import net.study.resume.exception.CantCompleteClientRequestException;
import net.study.resume.exception.EmailNotConfirmedException;
import net.study.resume.exception.FormValidationException;
import net.study.resume.form.SignUpForm;
import net.study.resume.repository.search.ProfileSearchRepository;
import net.study.resume.security.RecaptchaLoginFailuresManager;
import net.study.resume.security.UserPrincipal;
import net.study.resume.service.ProfileService;
import net.study.resume.util.SecurityUtil;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpRequest;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import static net.study.resume.Constants.UI.MAX_PROFILES_PER_PAGE;

@Controller
public class homeController {

    @Autowired
    ProfileService profileService;

    @Autowired
    ProfileSearchRepository profileSearchRepository;

    @Autowired
    SecurityUtil securityUtil;

    @Autowired
    RecaptchaLoginFailuresManager recaptchaLoginFailuresManager;

    @GetMapping("/")
    public String index(){
        return "redirect:/home";
    }

    @GetMapping(value = "/{uid}")
    public String profile(@AuthenticationPrincipal UserPrincipal principal,
                          @PathVariable String uid,
                          Model model
                         ) {
        Profile profile = profileService.findByUid(uid);

        if (profile == null) {
            return "profile-not-found";
        } else if (!profile.isCompleted()) {
            UserPrincipal currentProfile = principal;
            if (currentProfile == null || !currentProfile.getId().equals(profile.getId())) {
                return "profile-not-found";
            } else {
                return "redirect:/edit";
            }
        } else {
            model.addAttribute("profile", profile);
            return "profile";
        }
    }

    @GetMapping(value = { "/home" })
    public String listAll(Model model) {
        Page<Profile> profiles = profileService.findAllByCompleted(true,PageRequest.of(0, MAX_PROFILES_PER_PAGE, Sort.by("id")));
        model.addAttribute("profiles", profiles.getContent());
        model.addAttribute("page", profiles);
        return "home";
    }


    @GetMapping(value = "/fragment/more")
    public String moreProfiles(Model model,
                               @RequestParam(value="query", required=false) String query,
                               @PageableDefault(size=MAX_PROFILES_PER_PAGE) @SortDefault(sort="id") Pageable pageable) {
        Page<Profile> profiles = null;
        if(StringUtils.isNotBlank(query)) {
            profiles = profileService.findBySearchQuery(query, pageable);
        } else {
            profiles = profileService.findAllByCompleted(true,pageable);
        }
        model.addAttribute("profiles", profiles.getContent());
        return "fragment/profile-items";
    }

    @GetMapping(value = "/sign-in")
    public String signIn(Model model) {
        model.addAttribute("showRecaptcha",recaptchaLoginFailuresManager.isRecaptchaRequired(null));
        return "sign-in";
    }

    @GetMapping(value = "/sign-up")
    public String signUp(Model model) {
        model.addAttribute("profileForm", new SignUpForm());
        return "sign-up";
    }

    @PostMapping(value = "/sign-up")
    public String signUp(@Valid @ModelAttribute("profileForm") SignUpForm signUpForm,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "sign-up";
        } else {
            try {
                Profile profile = profileService.register(signUpForm);
                profileService.sentConfirmEmail(profile);

            }
            catch (EmailNotConfirmedException e) {
                return "redirect:/sign-up/confirmation";
            }
            catch (FormValidationException e){
                bindingResult.addError(e.buildFieldError("profileForm"));
                return "sign-up";
            }
            //securityUtil.authentificateWithRememberMe(profile);
            return "redirect:/sign-up/confirmation";
        }
    }

    @GetMapping(value = "/sign-up/confirmation")
    public String signUpConfirmation() {
        return "sign-up-confirmation";
    }

    @GetMapping(value = "/sign-up/{confirmationToken}")
    public String signUpConfirmation(@PathVariable String confirmationToken) {
        Profile profile = null;
        try{
            profile = profileService.findByConfirmationToken(confirmationToken);
        }
        catch (CantCompleteClientRequestException e){
            String errorMessage = "confirmation token is expired or doesn't exist";
            return "redirect:/sign-up?error=" + errorMessage;
        }
        profile.setEnabled(true);
        securityUtil.authentificateWithRememberMe(profile);
        return "redirect:/sign-up/success";
    }

    @GetMapping(value = "/sign-up/success")
    public String signUpSuccess() {
        return "sign-up-success";
    }


    @GetMapping(value = "/sign-in-failed")
    public String signInFailed(HttpServletRequest request) {
        return "sign-in";

    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String searchProfiles(@RequestParam(value="query", required=false) String query, Model model,
                                 @PageableDefault(size=MAX_PROFILES_PER_PAGE) @SortDefault(sort="id") Pageable pageable) {
        if(StringUtils.isBlank(query)){
            return "redirect:/home";
        } else {
            Page<Profile> profiles = profileService.findBySearchQuery(query, pageable);
            model.addAttribute("profiles", profiles.getContent());
            model.addAttribute("page", profiles);
            model.addAttribute("query", URLDecoder.decode(query, StandardCharsets.UTF_8));
            return "search-results";
        }
    }

    @GetMapping(value = "/restore")
    public String getRestoreAccess() {
        return "restore";
    }

    @GetMapping(value = "/restore/success")
    public String getRestoreSuccess() {
        return "restore-success";
    }

    @PostMapping(value = "/restore")
    public String processRestoreAccess(@RequestParam("uid") String anyUnigueId) {
        try {
            profileService.restoreAccess(anyUnigueId);
        }
        catch (UsernameNotFoundException e){
              String errorMessage = "user with this Uid not found";
              return "redirect:/restore?error=" + errorMessage;
        }
        return "redirect:/restore/success";
    }

    @GetMapping(value = "/restore/{token}")
    public String restoreAccess(@PathVariable("token") String token) {
        Profile profile = null;
        try{
             profile = profileService.findByRestoreToken(token);
        }
        catch (CantCompleteClientRequestException e){
            String errorMessage = "restore token is expired or doesn't exist";
            return "redirect:/restore?error=" + errorMessage;
        }
        securityUtil.authentificate(profile);
        return "redirect:/edit/password";
    }
}
