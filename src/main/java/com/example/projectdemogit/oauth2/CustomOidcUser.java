package com.example.projectdemogit.oauth2;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.Collection;
import java.util.Map;

public class CustomOidcUser implements OidcUser {

    private Map<String, Object> attributes;
    private OidcIdToken idToken;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomOidcUser(Map<String, Object> attributes, OidcIdToken idToken, Collection<? extends GrantedAuthority> authorities) {
        this.attributes = attributes;
        this.idToken = idToken;
        this.authorities = authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Map<String, Object> getClaims() {
        return idToken.getClaims();
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return null;
    }

    @Override
    public OidcIdToken getIdToken() {
        return idToken;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return authorities;
    }

    @Override
    public String getName() {
        return idToken.getSubject();
    }

    // other overridden methods...
}