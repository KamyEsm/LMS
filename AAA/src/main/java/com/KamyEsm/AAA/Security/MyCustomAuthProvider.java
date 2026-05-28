package com.KamyEsm.AAA.Security;

import com.KamyEsm.AAA.Service.User.MyUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MyCustomAuthProvider implements AuthenticationProvider {

    private final MyUserDetailsService service;
    private final PasswordEncoder encoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String pass = authentication.getCredentials().toString();

       UserDetails user = service.loadUserByUsername(username);

        System.out.println(user instanceof SecurityUser);


        if(encoder.matches(pass , user.getPassword())){
            return new UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    user.getAuthorities()
            );

        }
        else throw new BadCredentialsException("password or username is not correct");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
