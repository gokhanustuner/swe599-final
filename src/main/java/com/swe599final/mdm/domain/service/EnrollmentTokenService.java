package com.swe599final.mdm.domain.service;

import com.swe599final.mdm.domain.exception.DeviceUserNotFoundByEnterpriseIdAndAccountIdentifierException;
import com.swe599final.mdm.domain.exception.EnrollmentTokenNotFoundByIdAndEnterpriseIdException;
import com.swe599final.mdm.domain.exception.EnterpriseNotFoundByUserIdException;
import com.swe599final.mdm.domain.exception.PolicyNotFoundByIdAndEnterpriseIdException;
import com.swe599final.mdm.infrastructure.model.EnrollmentTokenDto;
import com.swe599final.mdm.infrastructure.model.EnrollmentTokenResponse;
import org.springframework.security.core.Authentication;

import java.io.IOException;

public interface EnrollmentTokenService {
    EnrollmentTokenResponse create(EnrollmentTokenDto enrollmentTokenDto, Authentication principal)
            throws IOException, EnterpriseNotFoundByUserIdException, PolicyNotFoundByIdAndEnterpriseIdException,
                    DeviceUserNotFoundByEnterpriseIdAndAccountIdentifierException;
    EnrollmentTokenResponse delete(Long enrollmentTokenId) throws IOException;
    EnrollmentTokenResponse get(Long enrollmentTokenId, Authentication principal)
            throws IOException, EnterpriseNotFoundByUserIdException, EnrollmentTokenNotFoundByIdAndEnterpriseIdException;
}
