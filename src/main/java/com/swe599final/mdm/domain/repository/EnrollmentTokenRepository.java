package com.swe599final.mdm.domain.repository;

import com.swe599final.mdm.infrastructure.model.EnrollmentToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentTokenRepository extends JpaRepository<EnrollmentToken, Long> {
    Optional<EnrollmentToken> findByIdAndEnterpriseId(Long policyId, Long enterpriseId);
    List<EnrollmentToken> findByEnterpriseId(Long enterpriseId);
}
