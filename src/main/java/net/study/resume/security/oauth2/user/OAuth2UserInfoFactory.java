package net.study.resume.security.oauth2.user;


import net.study.resume.Constants;
import net.study.resume.exception.OAuth2AuthenticationProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        switch(registrationId){
            case Constants
                    .AuthProvider.GOOGLE: return new GoogleOAuth2UserInfo(attributes);
            case Constants
                    .AuthProvider.GITHUB: return new GithubOAuth2UserInfo(attributes);
            case Constants
                    .AuthProvider.FACEBOOK: return new FacebookOAuth2UserInfo(attributes);
            default: throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationId + " is not supported yet.");
        }
    }

}
