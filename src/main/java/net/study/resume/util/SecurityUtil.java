package net.study.resume.util;

import net.study.resume.entity.Profile;
import net.study.resume.security.UserPrincipal;
import net.study.resume.service.RememberMeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class SecurityUtil {

	@Autowired
	public RememberMeService rememberMeService;

	public  Authentication authentificate(Profile profile) {
		UserPrincipal currentProfile =  UserPrincipal.create(profile);
		Authentication authentication = new UsernamePasswordAuthenticationToken(currentProfile, currentProfile.getPassword(), currentProfile.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return authentication;
	}

	public  void authentificateWithRememberMe(Profile profile) {
		Authentication authentication = authentificate(profile);
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		rememberMeService.createAutoLoginToken(requestAttributes.getRequest(), requestAttributes.getResponse(), authentication);
	}

	public  void authentificateWithRememberMe(Authentication authentication) {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		rememberMeService.createAutoLoginToken(requestAttributes.getRequest(), requestAttributes.getResponse(), authentication);
	}
}
