package com.KamyEsm.AAA.Service.Oauth2;

import com.KamyEsm.AAA.Entity.Oauth2RegisteredClientEntity;
import com.KamyEsm.AAA.Mapper.RegisteredClientMapper;
import com.KamyEsm.AAA.Repository.Oauth2RegisteredClientRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class Oauth2RegisteredClientService implements RegisteredClientRepository {

    private final Oauth2RegisteredClientRepository repository;
    private final RegisteredClientMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void save(RegisteredClient registeredClient) {
        if(!repository.existsById(Long.valueOf(registeredClient.getId()))){
            Oauth2RegisteredClientEntity entity = mapper.toEntity(registeredClient);
            String hashedClientSecret = passwordEncoder.encode(registeredClient.getClientSecret());
            entity.setClientSecret(hashedClientSecret);
            repository.save(entity);
        }
        else {
            Oauth2RegisteredClientEntity entity = repository.findById(Long.valueOf(registeredClient.getId())).get();
            mapper.update(registeredClient , entity);
            if(!passwordEncoder.matches(registeredClient.getClientSecret() , entity.getClientSecret()))
                entity.setClientSecret(passwordEncoder.encode(registeredClient.getClientSecret()));
        }
    }

    @Override
    public @Nullable RegisteredClient findById(String id) {
        return repository.findById(Long.valueOf(id)).map(mapper::toDto).orElse(null);
        // اسپرینگ انتظار دارد null برگردانی تا بفهمد کلاینت احراز هویت نشده و 401 پرتاپ کند
    }

    @Override
    public @Nullable RegisteredClient findByClientId(String clientId) {
        return repository.findByClientId(clientId).map(mapper::toDto).orElse(null);
    }
}
