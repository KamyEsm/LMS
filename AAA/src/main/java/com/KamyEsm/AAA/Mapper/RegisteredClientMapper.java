package com.KamyEsm.AAA.Mapper;

import com.KamyEsm.AAA.Entity.Oauth2RegisteredClientEntity;
import com.KamyEsm.AAA.Entity.Permission;
import org.mapstruct.*;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface RegisteredClientMapper {

    default RegisteredClient toDomain(Oauth2RegisteredClientEntity entity) {
        if (entity == null) {
            return null;
        }

        RegisteredClient.Builder builder = RegisteredClient.withId(String.valueOf(entity.getId()))
                .clientId(entity.getClientId())
                .clientIdIssuedAt(entity.getClientIdIssuedAt())
                .clientSecret(entity.getClientSecret())
                .clientSecretExpiresAt(entity.getClientSecretExpiresAt())
                .clientName(entity.getClientName());

        if (entity.getClientAuthenticationMethods() != null) {
            entity.getClientAuthenticationMethods().forEach(method ->
                    builder.clientAuthenticationMethod(new ClientAuthenticationMethod(method))
            );
        }

        if (entity.getAuthorizationGrantTypes() != null) {
            entity.getAuthorizationGrantTypes().forEach(grant ->
                    builder.authorizationGrantType(new AuthorizationGrantType(grant))
            );
        }


        Set<Permission> permissions = entity.getScopes();
        Set<String> scopeNames = permissions.stream()
                .map(Permission::getName)
                .collect(Collectors.toSet());
        builder.scopes(scopes -> scopes.addAll(scopeNames));

//        builder.clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build());
//        builder.tokenSettings(TokenSettings.builder().build());

        return builder.build();
    }

    default Oauth2RegisteredClientEntity toEntity(RegisteredClient domain) {
        if (domain == null) {
            return null;
        }

        Oauth2RegisteredClientEntity entity = new Oauth2RegisteredClientEntity();

        entity.setId(Long.valueOf(domain.getId()));
        entity.setClientId(domain.getClientId());
        entity.setClientIdIssuedAt(domain.getClientIdIssuedAt());
        entity.setClientSecret(domain.getClientSecret());
        entity.setClientSecretExpiresAt(domain.getClientSecretExpiresAt());
        entity.setClientName(domain.getClientName());

        if (domain.getClientAuthenticationMethods() != null) {
            Set<String> methods = domain.getClientAuthenticationMethods().stream()
                    .map(ClientAuthenticationMethod::getValue)
                    .collect(Collectors.toSet());
            entity.setClientAuthenticationMethods(methods);
        }

        if (domain.getAuthorizationGrantTypes() != null) {
            Set<String> grants = domain.getAuthorizationGrantTypes().stream()
                    .map(AuthorizationGrantType::getValue)
                    .collect(Collectors.toSet());
            entity.setAuthorizationGrantTypes(grants);
        }


        // entity.setClientSettingsJson(serialize(domain.getClientSettings()));
        // entity.setTokenSettingsJson(serialize(domain.getTokenSettings()));

        return entity;
    }



    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "clientSecret", ignore = true)
    @Mapping(target = "scopes" , ignore = true)
    void update(RegisteredClient registeredClient , @MappingTarget Oauth2RegisteredClientEntity entity);

    default String toAuthorizationGrantTypeString(AuthorizationGrantType grantType){
        if(grantType != null){
            return grantType.getValue();
        }
        else return null;
    }

    default String toClientAuthenticationMethodString(ClientAuthenticationMethod method){
        if(method != null){
            return method.getValue();
        }
        else return null;
    }
}
