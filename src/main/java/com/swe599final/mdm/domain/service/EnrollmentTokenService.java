package com.swe599final.mdm.domain.service;

import com.google.zxing.WriterException;
import com.swe599final.mdm.domain.exception.DeviceUserNotFoundByEnterpriseIdAndAccountIdentifierException;
import com.swe599final.mdm.domain.exception.EnrollmentTokenNotFoundByIdAndEnterpriseIdException;
import com.swe599final.mdm.domain.exception.EnterpriseNotFoundByUserIdException;
import com.swe599final.mdm.domain.exception.PolicyNotFoundByIdAndEnterpriseIdException;
import com.swe599final.mdm.infrastructure.model.EnrollmentTokenDto;
import com.swe599final.mdm.infrastructure.model.EnrollmentTokenResponse;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.util.List;

public interface EnrollmentTokenService {
    EnrollmentTokenResponse createEnrollmentToken(EnrollmentTokenDto enrollmentTokenDto, Authentication principal)
            throws IOException, EnterpriseNotFoundByUserIdException, PolicyNotFoundByIdAndEnterpriseIdException,
                    DeviceUserNotFoundByEnterpriseIdAndAccountIdentifierException, WriterException;
    void deleteEnrollmentToken(Long enrollmentTokenId, String enrollmentTokenName) throws IOException;
    List<EnrollmentTokenResponse> listEnrollmentTokens(Authentication principal) throws EnterpriseNotFoundByUserIdException;
    EnrollmentTokenResponse getEnrollmentToken(Long enrollmentTokenId, Authentication principal)
            throws EnterpriseNotFoundByUserIdException, EnrollmentTokenNotFoundByIdAndEnterpriseIdException;
}
