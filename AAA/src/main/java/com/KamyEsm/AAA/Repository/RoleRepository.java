package com.KamyEsm.AAA.Repository;

import com.KamyEsm.AAA.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role , Long> {
    boolean existsByName(String roleAdmin);

    Optional<Role> findByName(String name);
}
