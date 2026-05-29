package com.KamyEsm.AAA.Repository;

import com.KamyEsm.AAA.Entity.Oauth2RegisteredClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Oauth2RegisteredClientRepository extends JpaRepository<Oauth2RegisteredClientEntity , Long> {
    Optional<Oauth2RegisteredClientEntity> findByClientId(String clientId);
}
