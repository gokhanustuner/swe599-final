package com.swe599final.mdm.domain.service;

import com.swe599final.mdm.domain.exception.EnterpriseNotFoundByUserIdException;
import com.swe599final.mdm.domain.exception.PolicyNotFoundByIdAndEnterpriseIdException;
import com.swe599final.mdm.infrastructure.model.PolicyDto;
import com.swe599final.mdm.infrastructure.model.PolicyResponse;
import org.springframework.security.core.Authentication;

import java.io.IOException;

public interface PolicyService {
    public PolicyResponse create(PolicyDto policyDto, Authentication principal) throws IOException, EnterpriseNotFoundByUserIdException;
    public PolicyResponse get(Long policyId, Authentication principal)
            throws IOException, EnterpriseNotFoundByUserIdException, PolicyNotFoundByIdAndEnterpriseIdException;
    public PolicyResponse update(Long policyId, PolicyDto policyDto, Authentication principal);
    public void delete(Long policyId);
}
