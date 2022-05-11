package com.swe599final.mdm.domain.repository;

import com.swe599final.mdm.infrastructure.model.DeviceUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeviceUserRepository extends JpaRepository<DeviceUser, Long> {
    Optional<DeviceUser> findByEnterpriseIdAndAccountIdentifier(Long enterpriseId, String accountIdentifier);
}
