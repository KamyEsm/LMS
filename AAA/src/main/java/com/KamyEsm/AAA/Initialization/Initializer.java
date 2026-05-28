package com.KamyEsm.AAA.Initialization;

import com.KamyEsm.AAA.Entity.MyUser;
import com.KamyEsm.AAA.Entity.Permission;
import com.KamyEsm.AAA.Entity.Role;
import com.KamyEsm.AAA.Enum.InitialPermission;
import com.KamyEsm.AAA.Enum.InitialRole;
import com.KamyEsm.AAA.Properties.FirstUserProperties;
import com.KamyEsm.AAA.Properties.JWTProperties;
import com.KamyEsm.AAA.Repository.PermissionsRepository;
import com.KamyEsm.AAA.Repository.RoleRepository;
import com.KamyEsm.AAA.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class Initializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PermissionsRepository permissionsRepository;
    private final PasswordEncoder encoder;
    private final FirstUserProperties firstUser;
    private final JWTProperties jwt;



    @Override
    @Transactional
    public void run(String... args) {
        if(!roleRepository.existsByName(InitialRole.ROLE_MANAGER.name())){
            Set<Role> roleSet = new HashSet<>();
            Set<Permission> permissionSet = new HashSet<>();

            MyUser user = null;
            if (userRepository.existsByUsername(firstUser.getUsername())){
                Optional<MyUser> optional = userRepository.findByUsername(firstUser.getUsername());
                user = optional.get();
            }
            else {
                user = new MyUser(null , firstUser.getUsername() , encoder.encode(firstUser.getPassword()) , null);
            }

            for (InitialPermission p : InitialPermission.values()){
                permissionSet.add(new Permission(null , p.name() , new HashSet<>() , null));
            }

            Role adminRole = new Role(null , InitialRole.ROLE_MANAGER.name() , null , new HashSet<>() , permissionSet);
            roleSet.add(adminRole);
            user.setRoles(roleSet);

            userRepository.save(user);

        }
    }
}
