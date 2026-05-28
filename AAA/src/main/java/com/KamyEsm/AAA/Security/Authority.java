package com.KamyEsm.AAA.Security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@AllArgsConstructor
public class Authority implements GrantedAuthority {
    private final String name;
    @Getter
    private final String description;


    @Override
    public String getAuthority() {
        return this.name;
    }

}
