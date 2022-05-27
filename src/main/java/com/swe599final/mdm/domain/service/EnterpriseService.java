package com.swe599final.mdm.domain.service;

import com.google.api.services.androidmanagement.v1.model.Empty;
import com.swe599final.mdm.domain.exception.EnterpriseNotFoundByUserIdException;
import com.swe599final.mdm.infrastructure.model.EnterpriseDto;
import com.swe599final.mdm.infrastructure.model.EnterpriseResponse;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface EnterpriseService {
    EnterpriseResponse createEnterprise(EnterpriseDto enterpriseDto, Authentication principal) throws IOException, GeneralSecurityException;
    EnterpriseResponse getEnterprise(Long enterpriseId, Authentication principal) throws IOException, EnterpriseNotFoundByUserIdException;
    Empty deleteEnterprise(Long enterpriseId, Authentication principal) throws IOException, EnterpriseNotFoundByUserIdException;
}
