package net.study.resume.security;

import net.study.resume.entity.Profile;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class UserPrincipal implements OAuth2User, UserDetails {
    private final Long id;
    private final String Uid;
    private final String password;
    private final String fullName;
    private final String email;
    private final boolean enabled;
    private final Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;

    public UserPrincipal(Long id, String Uid, String password,String fullName,String email,boolean enabled, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.Uid = Uid;
        this.password = password;
        this.authorities = authorities;
        this.fullName = fullName;
        this.email = email;
        this.enabled = enabled;
    }


    public static UserPrincipal create(Profile profile) {
        List<GrantedAuthority> authorities = Collections.
                singletonList(new SimpleGrantedAuthority("USER"));

        return new UserPrincipal(
                profile.getId(),
                profile.getUid(),
                profile.getPassword(),
                profile.getFullName(),
                profile.getEmail(),
                profile.isEnabled(),
                authorities
        );
    }

    public static UserPrincipal create(Profile profile, Map<String, Object> attributes) {
        UserPrincipal userPrincipal = UserPrincipal.create(profile);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }

    public String getEmail() {
        return email;
    }

    public Long getId() {
        return id;
    }

    public String getUid() {
        return Uid;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return getUid();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getName() {
        return fullName;
    }

    public String getFullName() {
        return fullName;
    }
}
