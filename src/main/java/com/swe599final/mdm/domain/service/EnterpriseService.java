package com.swe599final.mdm.domain.service;

import com.google.api.services.androidmanagement.v1.model.ContactInfo;
import com.google.api.services.androidmanagement.v1.model.Empty;
import com.swe599final.mdm.domain.exception.EnterpriseNotFoundByUserIdException;
import com.swe599final.mdm.infrastructure.model.Enterprise;
import com.swe599final.mdm.infrastructure.model.EnterpriseDto;
import com.swe599final.mdm.infrastructure.model.EnterpriseId;
import com.swe599final.mdm.infrastructure.model.EnterpriseResponse;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface EnterpriseService {
    EnterpriseResponse create(EnterpriseDto enterpriseDto, Authentication principal) throws IOException, GeneralSecurityException;
    EnterpriseResponse get(Long enterpriseId, Authentication principal) throws IOException, EnterpriseNotFoundByUserIdException;
    Enterprise update(Long enterpriseId, String displayName, ContactInfo contactInfo, Authentication principal);
    Empty delete(Long enterpriseId, Authentication principal) throws IOException, EnterpriseNotFoundByUserIdException;
}
