package com.KamyEsm.AAA.Security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Set;

@AllArgsConstructor
@Getter
public class SecurityUser implements UserDetails {

    private final Long id;
    private final String username;
    @Setter
    private String password;
    private final Set<Authority> authorities;


    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

}
