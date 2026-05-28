package com.KamyEsm.AAA.Repository;

import com.KamyEsm.AAA.Entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionsRepository extends JpaRepository<Permission , Long> {
    boolean existsByName(String name);

    Optional<Permission> findByName(String name);
}
