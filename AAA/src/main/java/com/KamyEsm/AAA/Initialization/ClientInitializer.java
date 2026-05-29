package com.KamyEsm.AAA.Initialization;

import com.KamyEsm.AAA.Entity.Oauth2RegisteredClientEntity;
import com.KamyEsm.AAA.Entity.Permission;
import com.KamyEsm.AAA.Enum.InitialPermission;
import com.KamyEsm.AAA.Repository.Oauth2RegisteredClientRepository;
import com.KamyEsm.AAA.Repository.PermissionsRepository;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ClientInitializer implements CommandLineRunner {

    private final Oauth2RegisteredClientRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final PermissionsRepository permissionsRepository;

    @Override
    @Transactional
    public void run(String @NonNull ... args) throws Exception {
        String clientId = "inventory-service";

        if (repository.findByClientId(clientId).isEmpty()) {
            Oauth2RegisteredClientEntity client = new Oauth2RegisteredClientEntity();

            client.setClientId(clientId);
            client.setClientSecret(passwordEncoder.encode("s2y78sugha2390uvsdmnh39dkm3d"));
            client.setClientName("Inventory Microservice");

            client.setAuthorizationGrantTypes(Set.of("client_credentials"));
            client.setClientAuthenticationMethods(Set.of("client_secret_basic"));

            Set<Permission> permissions = new HashSet<>();
            for (InitialPermission p : InitialPermission.values()) {
                Optional<Permission> optional = permissionsRepository.findByName(p.name());
                optional.ifPresent(permissions::add);
            }

            client.setScopes(permissions);

             client.setAccessTokenTimeToLiveSeconds(3600L);

            repository.save(client);
            System.out.println(">>> Test client seeded successfully: " + clientId);
        }
    }
}
