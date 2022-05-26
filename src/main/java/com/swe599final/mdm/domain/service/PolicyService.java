package com.swe599final.mdm.domain.service;

import com.swe599final.mdm.domain.exception.EnterpriseNotFoundByUserIdException;
import com.swe599final.mdm.domain.exception.PolicyNotFoundByIdAndEnterpriseIdException;
import com.swe599final.mdm.infrastructure.model.PolicyDto;
import com.swe599final.mdm.infrastructure.model.PolicyResponse;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.util.List;

public interface PolicyService {
    PolicyResponse createPolicy(PolicyDto policyDto, Authentication principal) throws IOException, EnterpriseNotFoundByUserIdException;
    PolicyResponse getPolicy(Long policyId, Authentication principal)
            throws IOException, EnterpriseNotFoundByUserIdException, PolicyNotFoundByIdAndEnterpriseIdException;
    List<PolicyResponse> listPolicies(Authentication principal) throws IOException, EnterpriseNotFoundByUserIdException;
    void deletePolicy(Long policyId, String policyName) throws IOException;
}
