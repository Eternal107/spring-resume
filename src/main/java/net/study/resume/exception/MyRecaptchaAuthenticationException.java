package net.study.resume.exception;

import org.springframework.security.core.AuthenticationException;

public class MyRecaptchaAuthenticationException extends AuthenticationException {

    public MyRecaptchaAuthenticationException(String message){
        super(message);
    }
}
