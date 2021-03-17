package net.study.resume.security.oauth2.user;

import org.springframework.stereotype.Component;

import java.util.Map;

public abstract class OAuth2UserInfo {
    protected Map<String, Object> attributes;

    public OAuth2UserInfo(Map<String,Object> attributes){
        this.attributes = attributes;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public abstract String getId();

    public abstract String getName();

    public abstract String getEmail();

    public abstract String getImageUrl();

    public String getFirstName(){
        return getName().split( " ")[0];
    }

    public String getLastName(){
        return getName().split( " ")[1];
    }
}
