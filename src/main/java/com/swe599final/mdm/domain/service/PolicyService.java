package com.swe599final.mdm.domain.service;

import com.swe599final.mdm.domain.exception.EnterpriseNotFoundByUserIdException;
import com.swe599final.mdm.domain.exception.PolicyNotFoundByIdAndEnterpriseIdException;
import com.swe599final.mdm.infrastructure.model.PolicyDto;
import com.swe599final.mdm.infrastructure.model.PolicyResponse;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.util.List;

public interface PolicyService {
    PolicyResponse create(PolicyDto policyDto, Authentication principal) throws IOException, EnterpriseNotFoundByUserIdException;
    PolicyResponse get(Long policyId, Authentication principal)
            throws IOException, EnterpriseNotFoundByUserIdException, PolicyNotFoundByIdAndEnterpriseIdException;
    List<PolicyResponse> list(Authentication principal) throws IOException, EnterpriseNotFoundByUserIdException;
    PolicyResponse update(Long policyId, PolicyDto policyDto, Authentication principal);
    void delete(Long policyId);
}
