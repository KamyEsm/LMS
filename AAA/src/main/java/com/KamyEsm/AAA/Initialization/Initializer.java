package com.KamyEsm.AAA.Initialization;

import com.KamyEsm.AAA.Entity.MyUser;
import com.KamyEsm.AAA.Entity.Oauth2RegisteredClientEntity;
import com.KamyEsm.AAA.Entity.Permission;
import com.KamyEsm.AAA.Entity.Role;
import com.KamyEsm.AAA.Enum.InitialPermission;
import com.KamyEsm.AAA.Enum.InitialRole;
import com.KamyEsm.AAA.Properties.FirstUserProperties;
import com.KamyEsm.AAA.Properties.JWTProperties;
import com.KamyEsm.AAA.Repository.Oauth2RegisteredClientRepository;
import com.KamyEsm.AAA.Repository.PermissionsRepository;
import com.KamyEsm.AAA.Repository.RoleRepository;
import com.KamyEsm.AAA.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
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


    private final Oauth2RegisteredClientRepository repository;



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
                permissionSet.add(new Permission(null , p.name() , new HashSet<>() , null , null));
            }

            Role adminRole = new Role(null , InitialRole.ROLE_MANAGER.name() , null , new HashSet<>() , permissionSet);
            roleSet.add(adminRole);
            user.setRoles(roleSet);

            userRepository.save(user);


            String clientId = "inventory-service";

            if (repository.findByClientId(clientId).isEmpty()) {
                Oauth2RegisteredClientEntity client = new Oauth2RegisteredClientEntity();

                client.setClientId(clientId);
                client.setClientSecret(encoder.encode("s2y78sugha2390uvsdmnh39dkm3d"));
                client.setClientName("Inventory Microservice");

                client.setAuthorizationGrantTypes(new HashSet<>(Set.of("client_credentials")));
                client.setClientAuthenticationMethods(new HashSet<>(Set.of("client_secret_basic")));

                Set<Permission> permissions = new HashSet<>();
                for (InitialPermission p : InitialPermission.values()) {
                    Optional<Permission> optional = permissionsRepository.findByName(p.name());
                    optional.ifPresent(permissions::add);
                }

                client.setScopes(permissions);

                client.setAccessTokenTimeToLiveSeconds(3600L);

                repository.save(client);

                String userClientId = "frontend-client";

                if (repository.findByClientId(userClientId).isEmpty()) {
                    Oauth2RegisteredClientEntity userClient = new Oauth2RegisteredClientEntity();

                    userClient.setClientId(userClientId);
                    userClient.setClientName("frontend-client");

                    userClient.setAuthorizationGrantTypes(new HashSet<>(Set.of("authorization_code", "refresh_token")));
                    userClient.setClientAuthenticationMethods(new HashSet<>(Set.of("none")));

                    userClient.setRedirectUris(new HashSet<>(new HashSet<>(Set.of("http://localhost:5173/dashboard"))));
                    userClient.setPostLogoutRedirectUris(new HashSet<>(Set.of("http://localhost:5173/auth/callback")));

                    Set<Permission> permissionSets = new HashSet<>();

                    permissionsRepository.findByName(InitialPermission.openid.name()).ifPresent(permissionSets::add);
                    permissionsRepository.findByName(InitialPermission.profile.name()).ifPresent(permissionSets::add);

                    userClient.setScopes(permissionSets);

                    userClient.setAccessTokenTimeToLiveSeconds(600L);
                    userClient.setRefreshTokenTimeToLiveSeconds(2592000L);
                    userClient.setReuseRefreshTokens(false);

                    userClient.setRequireAuthorizationConsent(false);
                    userClient.setRequireProofKey(true);

                    repository.save(userClient);
                }


            }

        }
    }
}
