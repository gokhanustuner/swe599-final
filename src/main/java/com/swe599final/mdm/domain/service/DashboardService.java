package com.swe599final.mdm.domain.service;

import com.swe599final.mdm.infrastructure.model.Enterprise;
import org.springframework.security.core.userdetails.UserDetails;

public interface DashboardService {
    Enterprise getApplicationUsersEnterprise(UserDetails userDetails);
}
