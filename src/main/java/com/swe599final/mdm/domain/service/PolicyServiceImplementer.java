package com.swe599final.mdm.domain.service;

import com.google.api.services.androidmanagement.v1.model.PasswordRequirements;
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
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
    public PolicyResponse createPolicy(PolicyDto policyDto, Authentication principal) throws IOException, EnterpriseNotFoundByUserIdException {
        try {


        UserDetails userDetails = (MdmUserDetailsImplementer) principal.getPrincipal();
        MdmUserDetails mappedUser = userDetailsService.loadUserByUsername(userDetails.getUsername());

        Optional<Enterprise> enterprise = enterpriseRepository.findByUserId(mappedUser.getId());
        enterprise.orElseThrow(() -> new EnterpriseNotFoundByUserIdException("Enterprise not found with user id: " + mappedUser.getId()));
        Enterprise mdmEnterprise = enterprise.get();
        com.google.api.services.androidmanagement.v1.model.Policy androidPolicy =
            new com.google.api.services.androidmanagement.v1.model.Policy()
                .setApplications(policyDto.getApplications());

        if (!policyDto.getPasswordQuality().equals("0")) {
            PasswordRequirements passwordRequirements = new PasswordRequirements()
                    .setPasswordScope("SCOPE_PROFILE")
                    .setPasswordQuality(policyDto.getPasswordQuality());

            switch (policyDto.getPasswordQuality()) {
                case "BIOMETRIC_WEAK":
                case "SOMETHING":
                    break;
                case "COMPLEX":
                    passwordRequirements
                        .setPasswordMinimumLength(4)
                        .setPasswordMinimumLetters(2)
                        .setPasswordMinimumLowerCase(1)
                        .setPasswordMinimumUpperCase(1)
                        .setPasswordMinimumNonLetter(2)
                        .setPasswordMinimumNumeric(1)
                        .setPasswordMinimumSymbols(1);
                    break;
                default:
                    passwordRequirements.setPasswordMinimumLength(4);
                    break;
            }

            androidPolicy.setPasswordPolicies(List.of(passwordRequirements));
        }

        androidPolicy = androidManager.createPolicy(mdmEnterprise.getName(), androidPolicy);
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
        policyResponse.setPasswordQuality(
            androidPolicy.getPasswordPolicies() == null ?
            "" : androidPolicy.getPasswordPolicies().get(0).getPasswordQuality()
        );
        policyResponse.setPasswordScope(
            androidPolicy.getPasswordPolicies() == null ?
            "" : androidPolicy.getPasswordPolicies().get(0).getPasswordScope()
        );
        policyResponse.setInitial(mdmPolicy.isInitial());


            return policyResponse;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new PolicyResponse();
        }


    }

    @Override
    public PolicyResponse getPolicy(Long policyId, Authentication principal)
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
        policyResponse.setPasswordQuality(
            androidPolicy.getPasswordPolicies() == null ?
            "" : androidPolicy.getPasswordPolicies().get(0).getPasswordQuality()
        );
        policyResponse.setPasswordScope(
            androidPolicy.getPasswordPolicies() == null ?
            "" : androidPolicy.getPasswordPolicies().get(0).getPasswordScope()
        );
        policyResponse.setApplications(androidPolicy.getApplications());

        return policyResponse;
    }

    @Override
    public List<PolicyResponse> listPolicies(Authentication principal) throws IOException, EnterpriseNotFoundByUserIdException {
        UserDetails userDetails = (MdmUserDetailsImplementer) principal.getPrincipal();
        MdmUserDetails mappedUser = userDetailsService.loadUserByUsername(userDetails.getUsername());

        Optional<Enterprise> enterprise = enterpriseRepository.findByUserId(mappedUser.getId());
        enterprise.orElseThrow(() -> new EnterpriseNotFoundByUserIdException("Enterprise not found with user id: " + mappedUser.getId()));
        Enterprise mdmEnterprise = enterprise.get();

        List<Policy> mdmPolicies = policyRepository.findByEnterpriseId(mdmEnterprise.getId());
        List<com.google.api.services.androidmanagement.v1.model.Policy> androidPolicies = androidManager.listPolicies(mdmEnterprise.getName());
        List<PolicyResponse> policyResponses = new ArrayList<>();

        for(int i = 0; i < mdmPolicies.size(); i++) {
            int finalI = i;
            com.google.api.services.androidmanagement.v1.model.Policy androidPolicy = androidPolicies.stream().filter(
                new Predicate<com.google.api.services.androidmanagement.v1.model.Policy>() {
                    @Override
                    public boolean test(com.google.api.services.androidmanagement.v1.model.Policy androidPolicy) {
                        return mdmPolicies.get(finalI).getName().equals(androidPolicy.getName());
                    }
                }
            ).collect(Collectors.toList()).get(0);
            Policy mdmPolicy = mdmPolicies.get(i);
            PolicyResponse policyResponse = new PolicyResponse();
            policyResponse.setId(mdmPolicy.getId());
            policyResponse.setName(androidPolicy.getName());
            policyResponse.setDisplayName(mdmPolicy.getDisplayName());
            policyResponse.setInitial(mdmPolicy.isInitial());
            policyResponse.setApplications(androidPolicy.getApplications());
            policyResponse.setPasswordQuality(
                androidPolicy.getPasswordPolicies() == null ?
                "" : androidPolicy.getPasswordPolicies().get(0).getPasswordQuality()
            );
            policyResponse.setPasswordScope(
                    androidPolicy.getPasswordPolicies() == null ?
                    "" : androidPolicy.getPasswordPolicies().get(0).getPasswordScope()
            );
            policyResponses.add(policyResponse);
        }

        return policyResponses;
    }

    @Override
    public void deletePolicy(Long policyId, String policyName) throws IOException {
        policyRepository.deleteById(policyId);
        androidManager.deletePolicy(policyName);
    }
}
