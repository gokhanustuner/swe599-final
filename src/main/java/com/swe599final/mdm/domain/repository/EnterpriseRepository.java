package com.swe599final.mdm.domain.repository;

import com.swe599final.mdm.infrastructure.model.Enterprise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnterpriseRepository extends JpaRepository<Enterprise, Long> {
    Optional<Enterprise> findByName(String name);
    Optional<Enterprise> findByUserId(long userId);
    Optional<Enterprise> findByIdAndUserId(long id, long userId);
}
