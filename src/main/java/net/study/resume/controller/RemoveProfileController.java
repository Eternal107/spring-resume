package net.study.resume.controller;

import net.study.resume.entity.Profile;
import net.study.resume.repository.search.ProfileSearchRepository;
import net.study.resume.security.UserPrincipal;
import net.study.resume.service.EditProfileService;
import net.study.resume.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.Filter;

@Controller
public class RemoveProfileController {

    @Autowired
    private EditProfileService editProfileService;

    @Autowired
    private ProfileSearchRepository profileSearchRepository;
    @Autowired
    private SecurityUtil securityUtil;

    @GetMapping(value = "/remove")
    public String showRemovePage() {
        return "remove";
    }

    @PostMapping(value = "/remove")
    public String removeProfile(@AuthenticationPrincipal UserPrincipal principal) {
        editProfileService.removeProfile(principal.getId());
        SecurityContextHolder.getContext().setAuthentication(null);
        return "redirect:/home";
    }

    @GetMapping("/delete")
    public String removeSearchProfiles(){
        Iterable<Profile> profiles = profileSearchRepository.findAll();
        profileSearchRepository.deleteAll(profiles);
        return "redirect:/home";
    }
}
