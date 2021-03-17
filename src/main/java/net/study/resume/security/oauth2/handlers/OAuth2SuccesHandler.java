package net.study.resume.security.oauth2.handlers;

import net.study.resume.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class OAuth2SuccesHandler implements AuthenticationSuccessHandler {

    @Autowired
    SecurityUtil securityUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        //SecurityContextHolder.getContext().setAuthentication(authentication);

        securityUtil.authentificateWithRememberMe(authentication);
        response.sendRedirect(request.getContextPath() + "/");
    }
}
