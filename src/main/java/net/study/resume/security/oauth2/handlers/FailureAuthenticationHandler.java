package net.study.resume.security.oauth2.handlers;

import com.github.mkopylec.recaptcha.RecaptchaProperties;
import com.github.mkopylec.recaptcha.security.RecaptchaAuthenticationException;
import com.github.mkopylec.recaptcha.security.login.LoginFailuresCountingHandler;
import com.github.mkopylec.recaptcha.security.login.LoginFailuresManager;
import com.github.mkopylec.recaptcha.security.login.RecaptchaAwareRedirectStrategy;
import net.study.resume.exception.MyRecaptchaAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class FailureAuthenticationHandler extends LoginFailuresCountingHandler {

    public FailureAuthenticationHandler(LoginFailuresManager failuresManager, RecaptchaProperties recaptcha, RecaptchaAwareRedirectStrategy redirectStrategy) {
        super(failuresManager,recaptcha,redirectStrategy);
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        if(exception instanceof RecaptchaAuthenticationException){
            exception = new MyRecaptchaAuthenticationException("Please enter reCAPTCHA");
        }
        super.onAuthenticationFailure(request, response, exception);
    }

}
