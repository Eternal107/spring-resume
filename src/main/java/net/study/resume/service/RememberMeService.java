package net.study.resume.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Service
public class RememberMeService extends PersistentTokenBasedRememberMeServices {

	@Autowired
	public RememberMeService(AuthenticationService authenticationService, PersistentTokenRepository tokenRepository) {
		super("some_private_key", authenticationService, tokenRepository);
	}

	public void createAutoLoginToken(HttpServletRequest request, HttpServletResponse response, Authentication successfulAuthentication) {
		super.onLoginSuccess(request, response, successfulAuthentication);
	}
}
