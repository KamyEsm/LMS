package com.KamyEsm.AAA.Service.User;

import com.KamyEsm.AAA.Entity.MyUser;
import com.KamyEsm.AAA.Entity.Permission;
import com.KamyEsm.AAA.Entity.Role;
import com.KamyEsm.AAA.Repository.UserRepository;
import com.KamyEsm.AAA.Security.Authority;
import com.KamyEsm.AAA.Security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository repository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<MyUser> userOptional = repository.findByUsername(username);
        if(userOptional.isPresent()){
            MyUser user = userOptional.get();
            Set<Role> roleSet = user.getRoles();
            Set<Authority> set = new HashSet<>();

//            for (Role r : roleSet){
//                set.add(new Authority(r.getName().startsWith("ROLE_") ? r.getName() : "ROLE_" + r.getName() , "System_Role"));
//                for (Permission p : r.getPermissions()){
//                    set.add(new Authority(p.getName() , p.getDescription()));
//                }
//            }




            roleSet.forEach(r -> {
                set.add(new Authority(r.getName().startsWith("ROLE_") ? r.getName() : "ROLE_" + r.getName() , "System_Role"));

                r.getPermissions().forEach(p -> {
                    set.add(new Authority(p.getName() , p.getDescription()));
                });

            });
            return new SecurityUser(user.getId() , user.getUsername() , user.getPassword() , set);
        }
        else {
            throw new UsernameNotFoundException("username not found");
        }
    }

}
