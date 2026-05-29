package com.KamyEsm.AAA.Service.Oauth2;

import com.KamyEsm.AAA.Entity.Oauth2RegisteredClientEntity;
import com.KamyEsm.AAA.Entity.Permission;
import com.KamyEsm.AAA.Mapper.RegisteredClientMapper;
import com.KamyEsm.AAA.Repository.Oauth2RegisteredClientRepository;
import com.KamyEsm.AAA.Repository.PermissionsRepository;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class Oauth2RegisteredClientService implements RegisteredClientRepository {

    private final Oauth2RegisteredClientRepository repository;
    private final RegisteredClientMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final PermissionsRepository permissionsRepository;

    @Override
    @Transactional
    public void save(RegisteredClient registeredClient) {
        if(!repository.existsById(Long.valueOf(registeredClient.getId()))){
            Oauth2RegisteredClientEntity entity = mapper.toEntity(registeredClient);

            Set<String> scopes = registeredClient.getScopes();
            Set<Permission> permissions = ScopeSetToPermissionSet(scopes);
            entity.setScopes(permissions);

            String hashedClientSecret = passwordEncoder.encode(registeredClient.getClientSecret());
            entity.setClientSecret(hashedClientSecret);
            repository.save(entity);
        }
        else {
            Oauth2RegisteredClientEntity entity = repository.findById(Long.valueOf(registeredClient.getId())).get();
            mapper.update(registeredClient , entity);

            Set<String> scopes = registeredClient.getScopes();
            Set<Permission> permissions = ScopeSetToPermissionSet(scopes);
            entity.setScopes(permissions);

            if(!passwordEncoder.matches(registeredClient.getClientSecret() , entity.getClientSecret()))
                entity.setClientSecret(passwordEncoder.encode(registeredClient.getClientSecret()));
        }
    }

    private Set<Permission> ScopeSetToPermissionSet(Set<String> scopes){
        Set<Permission> permissions = new HashSet<>();
        for (String scope : scopes){
            if(permissionsRepository.existsByName(scope)){
                permissions.add(permissionsRepository.findByName(scope).get());
            }
            else {
                Permission permission = new Permission(null , scope , new HashSet<>() , null , null);
                permissionsRepository.save(permission);
                permissions.add(permission);
            }
        }
        return permissions;
    }

    @Override
    public @Nullable RegisteredClient findById(String id) {
        return repository.findById(Long.valueOf(id)).map(mapper::toDomain).orElse(null);// spring انتظار نال داره نه exception
    }

    @Override
    public @Nullable RegisteredClient findByClientId(String clientId) {
        return repository.findByClientId(clientId).map(mapper::toDomain).orElse(null);
    }
}
