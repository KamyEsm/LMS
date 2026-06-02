package com.KamyEsm.AAA.Mapper;

import com.KamyEsm.AAA.Entity.Oauth2RegisteredClientEntity;
import com.KamyEsm.AAA.Entity.Permission;
import org.mapstruct.*;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.time.Duration;
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
                    builder.clientAuthenticationMethod(mapClientAuthMethod(method))
            );
        }

        if (entity.getAuthorizationGrantTypes() != null) {
            entity.getAuthorizationGrantTypes().forEach(grant ->
                    builder.authorizationGrantType(mapToAuthorizationGrantType(grant))
            );
        }

        if (entity.getRedirectUris() != null) {
            entity.getRedirectUris().forEach(builder::redirectUri);
        }

        if (entity.getPostLogoutRedirectUris() != null) {
            entity.getPostLogoutRedirectUris().forEach(builder::postLogoutRedirectUri);
        }

        if (entity.getScopes() != null) {
            Set<String> scopeNames = entity.getScopes().stream()
                    .map(Permission::getName)
                    .collect(Collectors.toSet());

            builder.scopes(scopes -> scopes.addAll(scopeNames));
        }

        TokenSettings.Builder tokenSettingsBuilder = TokenSettings.builder()
                .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                .idTokenSignatureAlgorithm(SignatureAlgorithm.RS256);

        if (entity.getAccessTokenTimeToLiveSeconds() != null) {
            tokenSettingsBuilder.accessTokenTimeToLive(
                    Duration.ofSeconds(entity.getAccessTokenTimeToLiveSeconds())
            );
        }

        if (entity.getRefreshTokenTimeToLiveSeconds() != null) {
            tokenSettingsBuilder.refreshTokenTimeToLive(
                    Duration.ofSeconds(entity.getRefreshTokenTimeToLiveSeconds())
            );
        }

        if (entity.getReuseRefreshTokens() != null) {
            tokenSettingsBuilder.reuseRefreshTokens(entity.getReuseRefreshTokens());
        }

        builder.tokenSettings(tokenSettingsBuilder.build());

        ClientSettings.Builder clientSettingsBuilder = ClientSettings.builder();

        if (entity.getRequireAuthorizationConsent() != null) {
            clientSettingsBuilder.requireAuthorizationConsent(entity.getRequireAuthorizationConsent());
        }

        if (entity.getRequireProofKey() != null) {
            clientSettingsBuilder.requireProofKey(entity.getRequireProofKey());
        }

        builder.clientSettings(clientSettingsBuilder.build());

        return builder.build();
    }


    default Oauth2RegisteredClientEntity toEntity(RegisteredClient domain) {
        if (domain == null) {
            return null;
        }

        Oauth2RegisteredClientEntity entity = new Oauth2RegisteredClientEntity();

        if (domain.getId() != null && domain.getId().chars().allMatch(Character::isDigit)) {
            entity.setId(Long.valueOf(domain.getId()));
        }

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
        } else {
            entity.setClientAuthenticationMethods(Set.of());
        }

        if (domain.getAuthorizationGrantTypes() != null) {
            Set<String> grants = domain.getAuthorizationGrantTypes().stream()
                    .map(AuthorizationGrantType::getValue)
                    .collect(Collectors.toSet());
            entity.setAuthorizationGrantTypes(grants);
        } else {
            entity.setAuthorizationGrantTypes(Set.of());
        }

        if (domain.getRedirectUris() != null) {
            entity.setRedirectUris(Set.copyOf(domain.getRedirectUris()));
        } else {
            entity.setRedirectUris(Set.of());
        }

        if (domain.getPostLogoutRedirectUris() != null) {
            entity.setPostLogoutRedirectUris(Set.copyOf(domain.getPostLogoutRedirectUris()));
        } else {
            entity.setPostLogoutRedirectUris(Set.of());
        }

        if (domain.getTokenSettings() != null) {
            if (domain.getTokenSettings().getAccessTokenTimeToLive() != null) {
                entity.setAccessTokenTimeToLiveSeconds(
                        domain.getTokenSettings().getAccessTokenTimeToLive().toSeconds()
                );
            }

            if (domain.getTokenSettings().getRefreshTokenTimeToLive() != null) {
                entity.setRefreshTokenTimeToLiveSeconds(
                        domain.getTokenSettings().getRefreshTokenTimeToLive().toSeconds()
                );
            }

            entity.setReuseRefreshTokens(domain.getTokenSettings().isReuseRefreshTokens());
        }

        if (domain.getClientSettings() != null) {
            entity.setRequireAuthorizationConsent(
                    domain.getClientSettings().isRequireAuthorizationConsent()
            );

            entity.setRequireProofKey(
                    domain.getClientSettings().isRequireProofKey()
            );
        }

        return entity;
    }


    private ClientAuthenticationMethod mapClientAuthMethod(String method) {
        return switch (method) {
            case "client_secret_basic" -> ClientAuthenticationMethod.CLIENT_SECRET_BASIC;
            case "client_secret_post"  -> ClientAuthenticationMethod.CLIENT_SECRET_POST;
            case "private_key_jwt"     -> ClientAuthenticationMethod.PRIVATE_KEY_JWT;
            case "client_secret_jwt"   -> ClientAuthenticationMethod.CLIENT_SECRET_JWT;
            case "none"                -> ClientAuthenticationMethod.NONE;
            default -> new ClientAuthenticationMethod(method);
        };
    }

    private AuthorizationGrantType mapToAuthorizationGrantType(String authGrantType){
        return switch (authGrantType){
            case "authorization_code" -> AuthorizationGrantType.AUTHORIZATION_CODE;
            case "refresh_token" -> AuthorizationGrantType.REFRESH_TOKEN;
            case "client_credentials" -> AuthorizationGrantType.CLIENT_CREDENTIALS;
            default -> new AuthorizationGrantType(authGrantType);
        };
    }


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "clientSecret", ignore = true)
    @Mapping(target = "scopes", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "clientAuthenticationMethods", expression = "java(toClientAuthenticationMethods(registeredClient))")
    @Mapping(target = "authorizationGrantTypes", expression = "java(toAuthorizationGrantTypes(registeredClient))")
    @Mapping(target = "redirectUris", expression = "java(toRedirectUris(registeredClient))")
    @Mapping(target = "postLogoutRedirectUris", expression = "java(toPostLogoutRedirectUris(registeredClient))")
    @Mapping(target = "accessTokenTimeToLiveSeconds", expression = "java(toAccessTokenTtl(registeredClient))")
    @Mapping(target = "refreshTokenTimeToLiveSeconds", expression = "java(toRefreshTokenTtl(registeredClient))")
    @Mapping(target = "reuseRefreshTokens", expression = "java(toReuseRefreshTokens(registeredClient))")
    @Mapping(target = "requireAuthorizationConsent", expression = "java(toRequireAuthorizationConsent(registeredClient))")
    @Mapping(target = "requireProofKey", expression = "java(toRequireProofKey(registeredClient))")
    void update(RegisteredClient registeredClient, @MappingTarget Oauth2RegisteredClientEntity entity);

    default Set<String> toClientAuthenticationMethods(RegisteredClient registeredClient) {
        if (registeredClient == null || registeredClient.getClientAuthenticationMethods() == null) {
            return Set.of();
        }

        return registeredClient.getClientAuthenticationMethods().stream()
                .map(ClientAuthenticationMethod::getValue)
                .collect(Collectors.toSet());
    }

    default Set<String> toAuthorizationGrantTypes(RegisteredClient registeredClient) {
        if (registeredClient == null || registeredClient.getAuthorizationGrantTypes() == null) {
            return Set.of();
        }

        return registeredClient.getAuthorizationGrantTypes().stream()
                .map(AuthorizationGrantType::getValue)
                .collect(Collectors.toSet());
    }

    default Set<String> toRedirectUris(RegisteredClient registeredClient) {
        if (registeredClient == null || registeredClient.getRedirectUris() == null) {
            return Set.of();
        }

        return Set.copyOf(registeredClient.getRedirectUris());
    }

    default Set<String> toPostLogoutRedirectUris(RegisteredClient registeredClient) {
        if (registeredClient == null || registeredClient.getPostLogoutRedirectUris() == null) {
            return Set.of();
        }

        return Set.copyOf(registeredClient.getPostLogoutRedirectUris());
    }

    default Long toAccessTokenTtl(RegisteredClient registeredClient) {
        if (registeredClient == null || registeredClient.getTokenSettings() == null
                || registeredClient.getTokenSettings().getAccessTokenTimeToLive() == null) {
            return null;
        }

        return registeredClient.getTokenSettings().getAccessTokenTimeToLive().toSeconds();
    }

    default Long toRefreshTokenTtl(RegisteredClient registeredClient) {
        if (registeredClient == null || registeredClient.getTokenSettings() == null
                || registeredClient.getTokenSettings().getRefreshTokenTimeToLive() == null) {
            return null;
        }

        return registeredClient.getTokenSettings().getRefreshTokenTimeToLive().toSeconds();
    }

    default Boolean toReuseRefreshTokens(RegisteredClient registeredClient) {
        if (registeredClient == null || registeredClient.getTokenSettings() == null) {
            return null;
        }

        return registeredClient.getTokenSettings().isReuseRefreshTokens();
    }

    default Boolean toRequireAuthorizationConsent(RegisteredClient registeredClient) {
        if (registeredClient == null || registeredClient.getClientSettings() == null) {
            return null;
        }

        return registeredClient.getClientSettings().isRequireAuthorizationConsent();
    }

    default Boolean toRequireProofKey(RegisteredClient registeredClient) {
        if (registeredClient == null || registeredClient.getClientSettings() == null) {
            return null;
        }

        return registeredClient.getClientSettings().isRequireProofKey();
    }
}
