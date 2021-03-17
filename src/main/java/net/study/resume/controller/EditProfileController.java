package net.study.resume.controller;

import net.study.resume.entity.Contacts;
import net.study.resume.entity.Language;
import net.study.resume.entity.Profile;
import net.study.resume.entity.Skill;
import net.study.resume.exception.FormValidationException;
import net.study.resume.form.*;
import net.study.resume.model.LanguageLevel;
import net.study.resume.model.UploadCertificateResult;
import net.study.resume.security.UserPrincipal;
import net.study.resume.service.*;
import net.study.resume.service.image.ImageProcessorService;
import net.study.resume.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@Controller
public class EditProfileController {

    @Autowired
    private ProfileService profileService ;

    @Autowired
    private EditProfileService editProfileService;

    @Autowired
    private SkillCategoryService skillCategoryService;

    @Autowired
    private StaticDataService staticDataService;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private ImageProcessorService imageService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class,new StringTrimmerEditor(true));
        binder.registerCustomEditor(LanguageLevel.class, LanguageLevel.getPropertyEditor());
    }


    @GetMapping(value = "/edit")
    public String getEditProfile(@AuthenticationPrincipal UserPrincipal principal,
                                 Model model) {
        model.addAttribute("profileForm", profileService.findById(principal.getId()));
        return "edit/profile";
    }

    @PostMapping(value = "/edit")
    public String saveEditProfile(@AuthenticationPrincipal UserPrincipal principal,
                                  @Valid @ModelAttribute("profileForm") Profile profileForm,
                                  BindingResult bindingResult,
                                  @RequestParam("profilePhoto") MultipartFile uploadPhoto) {
        if (bindingResult.hasErrors()) {
            return "edit/profile";
        } else {
            try {
                editProfileService.updateProfileData(principal.getId(), profileForm, uploadPhoto);
                return "redirect:/edit/contacts";
            } catch (FormValidationException e) {
                bindingResult.addError(e.buildFieldError("profileForm"));
                return "edit/profile";
            }
        }
    }



    @GetMapping(value = "/edit/contacts")
    public String getEditContactsProfile(@AuthenticationPrincipal UserPrincipal principal,
                                         Model model) {
        model.addAttribute("contactsForm", editProfileService.getContacts(principal.getId()));
        return "edit/contacts";
    }

    @PostMapping(value = "/edit/contacts")
    public String saveEditContactsProfile(@AuthenticationPrincipal UserPrincipal principal,
                                          @Valid @ModelAttribute("contactsForm") Contacts contactsForm,
                                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "edit/contacts";
        } else {
            editProfileService.updateContacts(principal, contactsForm);
            return "redirect:/edit/skills";
        }
    }

    @GetMapping(value = "/edit/skills")
    public String getEditTechSkills(@AuthenticationPrincipal UserPrincipal principal, Model model) {
        model.addAttribute("skillForm", new SkillForm(editProfileService.getSkill(principal.getId())));
        return gotoSkillsJSP(model);
    }

    @PostMapping(value = "/edit/skills")
    public String saveEditTechSkills(@AuthenticationPrincipal UserPrincipal principal,
                                     @Valid @ModelAttribute("skillForm") SkillForm form,
                                     BindingResult bindingResult,
                                     Model model) {
        if (bindingResult.hasErrors()) {
            return gotoSkillsJSP(model);
        } else {
            editProfileService.editSkills(principal.getId(), form.getItems());
            return "redirect:/edit/practics";
        }
    }
    private String gotoSkillsJSP(Model model){
        model.addAttribute("skillCategories", skillCategoryService.findAll(Sort.by("category")));

        return "edit/skills";
    }


    @GetMapping(value = "/edit/practics")
    public String getEditPractics(@AuthenticationPrincipal UserPrincipal principal, Model model) {
        model.addAttribute("practicForm", new PracticForm(editProfileService.getPractic(principal.getId())));
        return gotoPracticsJSP(model);
    }

    @PostMapping(value = "/edit/practics")
    public String saveEditPractics(@AuthenticationPrincipal UserPrincipal principal,
                                   @Valid @ModelAttribute("practicForm") PracticForm form,
                                   BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return gotoPracticsJSP(model);
        } else {
            editProfileService.editPractic(principal.getId(), form.getItems());
            return "redirect:/edit/certificates";
        }
    }

    private String gotoPracticsJSP(Model model){
        model.addAttribute("years",  staticDataService.listPracticsYears());
        model.addAttribute("months", staticDataService.mapMonths());
        return "edit/practics";
    }

    @GetMapping(value = "/edit/certificates")
    public String getEditCertificates(@AuthenticationPrincipal UserPrincipal principal,
                                      Model model) {
        model.addAttribute("certificateForm", new CertificateForm(editProfileService.getCertificate(principal.getId())));
        return "edit/certificates";
    }

    @PostMapping(value = "/edit/certificates")
    public String saveEditCertificates(@AuthenticationPrincipal UserPrincipal principal,
                                       @Valid @ModelAttribute("certificateForm") CertificateForm form,
                                       BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "edit/certificates";
        } else {
            editProfileService.editCertificates(principal.getId(), form.getItems());
            return "redirect:/edit/courses";
        }
    }

    @PostMapping(value = "/edit/certificates/upload")
    public @ResponseBody UploadCertificateResult uploadCertificate(@RequestParam("certificateFile") MultipartFile certificateFile) {
        return imageService.processNewCertificateImage(certificateFile);
    }

    @GetMapping(value = "/edit/courses")
    public String getEditCourses(@AuthenticationPrincipal UserPrincipal principal,
                                 Model model) {
        model.addAttribute("courseForm", new CourseForm(editProfileService.getCourse(principal.getId())));
        return gotoCoursesJSP(model);
    }

    @PostMapping(value = "/edit/courses")
    public String saveEditCourses(@AuthenticationPrincipal UserPrincipal principal,
                                  @Valid @ModelAttribute("courseForm") CourseForm form,
                                  BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return gotoCoursesJSP(model);
        } else {
            editProfileService.editCourse(principal.getId(), form.getItems());
            return "redirect:/edit/education";
        }
    }

    private String gotoCoursesJSP(Model model){
        model.addAttribute("years",  staticDataService.listCourcesYears());
        model.addAttribute("months", staticDataService.mapMonths());
        return "edit/courses";
    }

    @GetMapping(value = "/edit/education")
    public String getEditEducation(@AuthenticationPrincipal UserPrincipal principal,
                                   Model model) {
        model.addAttribute("educationForm", new EducationForm(editProfileService.getEducation(principal.getId())));
        return gotoEducationJSP(model);
    }

    @PostMapping(value = "/edit/education")
    public String saveEditEducation(@AuthenticationPrincipal UserPrincipal principal,
                                    @Valid @ModelAttribute("educationForm") EducationForm form,
                                    BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return gotoEducationJSP(model);
        } else {
            editProfileService.editEducation(principal.getId(), form.getItems());
            return "redirect:/edit/languages";
        }
    }

    private String gotoEducationJSP(Model model){
        model.addAttribute("years",  staticDataService.listEducationYears());
        model.addAttribute("months", staticDataService.mapMonths());
        return "edit/education";
    }

    @GetMapping(value = "/edit/languages")
    public String getEditLanguages(@AuthenticationPrincipal UserPrincipal principal,
                                   Model model) {
        Language language = new Language();
        model.addAttribute("languageForm", new LanguageForm(editProfileService.getLanguage(principal.getId())));
        return gotoLanguagesJSP(model);
    }

    @PostMapping(value = "/edit/languages")
    public String saveEditLanguages(@AuthenticationPrincipal UserPrincipal principal,
                                    @Valid @ModelAttribute("languageForm") LanguageForm form,
                                    BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {

            return gotoLanguagesJSP(model);
        } else {
            editProfileService.editLanguage(principal.getId(), form.getItems());
            return "redirect:/edit/hobbies";
        }
    }

    private String gotoLanguagesJSP(Model model){
        model.addAttribute("languageTypes",  staticDataService.getAllLanguageTypes());
        model.addAttribute("languageLevels", staticDataService.getAllLanguageLevels());
        return "edit/languages";
    }

    @GetMapping(value = "/edit/hobbies")
    public String getEditHobbies(@AuthenticationPrincipal UserPrincipal principal,
                                 Model model) {
        model.addAttribute("hobbies", editProfileService.listHobbiesWithProfileSelected(principal.getId()));
        return "edit/hobbies";
    }

    @PostMapping(value = "/edit/hobbies")
    public String saveEditHobbies(@AuthenticationPrincipal UserPrincipal principal,
                                  @RequestParam("hobbies") List<String> hobbies) {
        editProfileService.editHobby(principal.getId(), hobbies);
        return "redirect:/edit/info";
    }

    @GetMapping(value = "/edit/info")
    public String getEditProfileInfo(@AuthenticationPrincipal UserPrincipal principal,
                                     Model model) {
        model.addAttribute("profile",profileService.findById(principal.getId()));
        return "edit/info";
    }

    @PostMapping(value = "/edit/info")
    public String saveEditProfileInfo(@AuthenticationPrincipal UserPrincipal principal,
                                      @Valid @ModelAttribute("profile") Profile profile,
                                      BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()){
            return "edit/info";
        } else {
            editProfileService.editInfo(principal.getId(), profile.getInfo());
            return "redirect:/my-profile";
        }
    }

    @GetMapping(value = "/edit/password")
    public String getEditPasswords(Model model) {
        model.addAttribute("passwordForm", new PasswordForm());
        return "password";
    }

    @PostMapping(value = "/edit/password")
    public String saveEditPasswords(@AuthenticationPrincipal UserPrincipal principal,
                                    @Valid @ModelAttribute("passwordForm") PasswordForm form,
                                    BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()){
            return "password";
        } else {
            Profile profile = editProfileService.updateProfilePassword(principal.getId(), form);
            securityUtil.authentificate(profile);
            return "redirect:/" + principal.getUid();
        }
    }

    @GetMapping(value = "/my-profile")
    public String getMyProfile(@AuthenticationPrincipal UserPrincipal principal) {
        return "redirect:/" + principal.getUid();
    }
}
