package com.swe599final.mdm.domain.repository;

import com.swe599final.mdm.infrastructure.model.Policy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PolicyRepository extends JpaRepository<Policy, Long> {
    Optional<Policy> findByIdAndEnterpriseId(Long policyId, Long enterpriseId);
    List<Policy> findByEnterpriseId(Long enterpriseId);
}
