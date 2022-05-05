package com.swe599final.mdm.domain.repository;

import com.swe599final.mdm.infrastructure.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
