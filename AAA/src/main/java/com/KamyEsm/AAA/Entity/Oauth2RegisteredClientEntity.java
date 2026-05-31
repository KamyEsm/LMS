package com.KamyEsm.AAA.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Getter @Setter
@Entity
@Table(
        name = "oauth2_registered_client",
        indexes = {
                @Index(name = "idx_oauth2_client_client_id", columnList = "client_id")
        }
)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Oauth2RegisteredClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_id", nullable = false, unique = true, length = 200)
    private String clientId;

    @Column(name = "client_id_issued_at")
    private Instant clientIdIssuedAt;

    @Column(name = "client_secret", length = 500)
    private String clientSecret;

    @Column(name = "client_secret_expires_at")
    private Instant clientSecretExpiresAt;

    @Column(name = "client_name", nullable = false, length = 100)
    private String clientName;

    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "oauth2_client_auth_method",
            joinColumns = @JoinColumn(name = "oauth2_registered_client_id"))
    @Column(name = "auth_method", nullable = false, length = 100)
    private Set<String> clientAuthenticationMethods = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "oauth2_client_grant_type",
            joinColumns = @JoinColumn(name = "oauth2_registered_client_id"))
    @Column(name = "grant_type", nullable = false, length = 100)
    private Set<String> authorizationGrantTypes = new HashSet<>();

//    @ElementCollection(fetch = FetchType.EAGER)
//    @CollectionTable(name = "oauth2_client_scope",
//            joinColumns = @JoinColumn(name = "client_pk"))
//    @Column(name = "scope", nullable = false, length = 200)
//    private Set<String> scopes = new HashSet<>();


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            joinColumns = @JoinColumn(name = "oauth2_registered_client_id") ,
            inverseJoinColumns = @JoinColumn(name = "permission_id") ,
            name = "client_permission"
    )
    private Set<Permission> scopes;

    // --- Token settings ---
    @Column(name = "access_token_ttl_seconds")
    private Long accessTokenTimeToLiveSeconds; // TTL access token

    @Column(name = "refresh_token_ttl_seconds")
    private Long refreshTokenTimeToLiveSeconds; // TTL refresh token

    @Column(name = "reuse_refresh_tokens")
    private Boolean reuseRefreshTokens; // old refresh token is valid?

    // --- Audit ---
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @PrePersist
    void prePersist() {
        var now = Instant.now();
        if (createdAt == null) createdAt = now;
        updatedAt = now;
        if (clientIdIssuedAt == null) clientIdIssuedAt = now;
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = Instant.now();
    }


    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "oauth2_client_redirect_uri",
            joinColumns = @JoinColumn(name = "oauth2_registered_client_id")
    )
    @Column(name = "redirect_uri", nullable = false, length = 500)
    private Set<String> redirectUris = new HashSet<>();


    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "oauth2_client_post_logout_redirect_uri",
            joinColumns = @JoinColumn(name = "oauth2_registered_client_id")
    )
    @Column(name = "post_logout_redirect_uri", nullable = false, length = 500)
    private Set<String> postLogoutRedirectUris = new HashSet<>();

    @Column(name = "require_authorization_consent")
    private Boolean requireAuthorizationConsent;

    @Column(name = "require_proof_key")
    private Boolean requireProofKey;


}
