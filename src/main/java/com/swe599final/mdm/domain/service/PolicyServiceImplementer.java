package com.swe599final.mdm.domain.service;

import com.swe599final.mdm.domain.exception.EnterpriseNotFoundByUserIdException;
import com.swe599final.mdm.domain.exception.PolicyNotFoundByIdAndEnterpriseIdException;
import com.swe599final.mdm.domain.repository.EnterpriseRepository;
import com.swe599final.mdm.domain.repository.PolicyRepository;
import com.swe599final.mdm.infrastructure.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public final class PolicyServiceImplementer implements PolicyService {
    @Autowired
    private AndroidManager androidManager;

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private EnterpriseRepository enterpriseRepository;

    @Autowired
    private MdmUserDetailsService userDetailsService;

    @Override
    public PolicyResponse create(PolicyDto policyDto, Authentication principal) throws IOException, EnterpriseNotFoundByUserIdException {
        UserDetails userDetails = (MdmUserDetailsImplementer) principal.getPrincipal();
        MdmUserDetails mappedUser = userDetailsService.loadUserByUsername(userDetails.getUsername());

        Optional<Enterprise> enterprise = enterpriseRepository.findByUserId(mappedUser.getId());
        enterprise.orElseThrow(() -> new EnterpriseNotFoundByUserIdException("Enterprise not found with user id: " + mappedUser.getId()));
        Enterprise mdmEnterprise = enterprise.get();
        com.google.api.services.androidmanagement.v1.model.Policy androidPolicy =
            androidManager.createPolicy(
                mdmEnterprise.getName(),
                new com.google.api.services.androidmanagement.v1.model.Policy()
                        .setApplications(policyDto.getApplications())
            );

        Policy mdmPolicy = new Policy();
        mdmPolicy.setDisplayName(policyDto.getDisplayName());
        mdmPolicy.setName(androidPolicy.getName());
        mdmPolicy.setInitial(policyDto.isInitial());
        mdmPolicy.setEnterprise(mdmEnterprise);
        mdmPolicy = policyRepository.save(mdmPolicy);

        PolicyResponse policyResponse = new PolicyResponse();
        policyResponse.setId(mdmPolicy.getId());
        policyResponse.setDisplayName(mdmPolicy.getDisplayName());
        policyResponse.setName(androidPolicy.getName());
        policyResponse.setApplications(androidPolicy.getApplications());
        policyResponse.setInitial(mdmPolicy.isInitial());

        return policyResponse;
    }

    @Override
    public PolicyResponse get(Long policyId, Authentication principal)
            throws IOException, EnterpriseNotFoundByUserIdException, PolicyNotFoundByIdAndEnterpriseIdException {
        UserDetails userDetails = (MdmUserDetailsImplementer) principal.getPrincipal();
        MdmUserDetails mappedUser = userDetailsService.loadUserByUsername(userDetails.getUsername());

        Optional<Enterprise> enterprise = enterpriseRepository.findByUserId(mappedUser.getId());
        enterprise.orElseThrow(() -> new EnterpriseNotFoundByUserIdException("Enterprise not found with user id: " + mappedUser.getId()));
        Enterprise mdmEnterprise = enterprise.get();

        Optional<Policy> policy = policyRepository.findByIdAndEnterpriseId(policyId, mdmEnterprise.getId());
        policy.orElseThrow(() -> new PolicyNotFoundByIdAndEnterpriseIdException(
                "Enterprise not found with policy id " + policyId + " and enterprise id: " + mdmEnterprise.getId()
            )
        );
        Policy mdmPolicy = policy.get();
        com.google.api.services.androidmanagement.v1.model.Policy androidPolicy = androidManager.getPolicy(mdmPolicy.getName());

        PolicyResponse policyResponse = new PolicyResponse();
        policyResponse.setId(mdmPolicy.getId());
        policyResponse.setName(androidPolicy.getName());
        policyResponse.setDisplayName(mdmPolicy.getDisplayName());
        policyResponse.setInitial(mdmPolicy.isInitial());
        policyResponse.setApplications(androidPolicy.getApplications());

        return policyResponse;
    }

    @Override
    public List<PolicyResponse> list(Authentication principal) throws IOException, EnterpriseNotFoundByUserIdException {
        return null;
    }

    @Override
    public PolicyResponse update(Long policyId, PolicyDto policyDto, Authentication principal) {
        return null;
    }

    @Override
    public void delete(Long policyId) {

    }
}
