package com.KamyEsm.AAA.Repository;

import com.KamyEsm.AAA.Entity.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<MyUser, Long> {
    Optional<MyUser> findByUsername(String username);

    boolean existsByUsername(String username);
}
