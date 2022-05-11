package com.swe599final.mdm.domain.service;

import com.swe599final.mdm.domain.exception.DeviceUserNotFoundByEnterpriseIdAndAccountIdentifierException;
import com.swe599final.mdm.domain.exception.EnrollmentTokenNotFoundByIdAndEnterpriseIdException;
import com.swe599final.mdm.domain.exception.EnterpriseNotFoundByUserIdException;
import com.swe599final.mdm.domain.exception.PolicyNotFoundByIdAndEnterpriseIdException;
import com.swe599final.mdm.domain.repository.DeviceUserRepository;
import com.swe599final.mdm.domain.repository.EnrollmentTokenRepository;
import com.swe599final.mdm.domain.repository.EnterpriseRepository;
import com.swe599final.mdm.domain.repository.PolicyRepository;
import com.swe599final.mdm.infrastructure.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
final public class EnrollmentTokenServiceImplementer implements EnrollmentTokenService {
    @Autowired
    private AndroidManager androidManager;

    @Autowired
    private EnrollmentTokenRepository enrollmentTokenRepository;

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private EnterpriseRepository enterpriseRepository;

    @Autowired
    private DeviceUserRepository deviceUserRepository;

    @Autowired
    private MdmUserDetailsService userDetailsService;

    @Override
    public EnrollmentTokenResponse create(EnrollmentTokenDto enrollmentTokenDto, Authentication principal)
            throws IOException, EnterpriseNotFoundByUserIdException, PolicyNotFoundByIdAndEnterpriseIdException,
                    DeviceUserNotFoundByEnterpriseIdAndAccountIdentifierException {
        UserDetails userDetails = (MdmUserDetailsImplementer) principal.getPrincipal();
        MdmUserDetails mappedUser = userDetailsService.loadUserByUsername(userDetails.getUsername());

        Optional<Enterprise> enterprise = enterpriseRepository.findByUserId(mappedUser.getId());
        enterprise.orElseThrow(() -> new EnterpriseNotFoundByUserIdException("Enterprise not found with user id: " + mappedUser.getId()));
        Enterprise mdmEnterprise = enterprise.get();

        System.out.println("policy id: " + enrollmentTokenDto.getPolicyId() + ", enterprise id: " + mdmEnterprise.getId());
        Optional<Policy> policy = policyRepository.findByIdAndEnterpriseId(enrollmentTokenDto.getPolicyId(), mdmEnterprise.getId());
        policy.orElseThrow(() -> new PolicyNotFoundByIdAndEnterpriseIdException(
                "Enterprise not found with policy id " + enrollmentTokenDto.getPolicyId() + " and enterprise id: " + mdmEnterprise.getId()
            )
        );
        Policy mdmPolicy = policy.get();
        System.out.println("MDM Policy Id: " + mdmPolicy.getId());
        com.google.api.services.androidmanagement.v1.model.EnrollmentToken androidEnrollmentToken =
                androidManager.createEnrollmentToken(mdmPolicy.getName(), mdmEnterprise.getName(), enrollmentTokenDto.getDeviceUser());
        System.out.println("Coming here!!" + androidEnrollmentToken.getUser().getAccountIdentifier());

        Optional<DeviceUser> deviceUser = deviceUserRepository.findByEnterpriseIdAndAccountIdentifier(mdmEnterprise.getId(), enrollmentTokenDto.getDeviceUser());
        DeviceUser mdmDeviceUser = new DeviceUser();

        if (deviceUser.isEmpty()) {
            mdmDeviceUser.setAccountIdentifier(androidEnrollmentToken.getUser().getAccountIdentifier());
            mdmDeviceUser.setEnterprise(mdmEnterprise);
            mdmDeviceUser = deviceUserRepository.save(mdmDeviceUser);
        } else {
            mdmDeviceUser = deviceUser.get();
        }


        System.out.println("Device User Id: " + mdmDeviceUser.getId());

        try {
            EnrollmentToken mdmEnrollmentToken = new EnrollmentToken();
            mdmEnrollmentToken.setPolicyName(androidEnrollmentToken.getPolicyName());
            mdmEnrollmentToken.setToken(androidEnrollmentToken.getValue());
            mdmEnrollmentToken.setEnterprise(mdmEnterprise);
            mdmEnrollmentToken.setPolicyName(androidEnrollmentToken.getPolicyName());
            mdmEnrollmentToken.setPolicy(mdmPolicy);
            mdmEnrollmentToken.setDuration(androidEnrollmentToken.getDuration());
            mdmEnrollmentToken.setAdditionalData(androidEnrollmentToken.getAdditionalData());
            mdmEnrollmentToken.setName(androidEnrollmentToken.getName());
            mdmEnrollmentToken.setAllowPersonalUsage(androidEnrollmentToken.getAllowPersonalUsage());
            mdmEnrollmentToken.setOneTimeOnly(androidEnrollmentToken.getOneTimeOnly());
            mdmEnrollmentToken.setExpirationTimestamp(androidEnrollmentToken.getExpirationTimestamp());
            mdmEnrollmentToken.setStatus(EnrollmentTokenStatus.QUEUE);
            mdmEnrollmentToken.setDeviceUser(mdmDeviceUser);
            mdmEnrollmentToken.setQrCode(androidEnrollmentToken.getQrCode());
            System.out.println("Before enrollment token creation!!!");
            mdmEnrollmentToken = enrollmentTokenRepository.save(mdmEnrollmentToken);
            System.out.println("After enrollment token creation!!!");

            EnrollmentTokenResponse enrollmentTokenResponse = new EnrollmentTokenResponse();
            enrollmentTokenResponse.setId(mdmEnrollmentToken.getId());
            enrollmentTokenResponse.setName(androidEnrollmentToken.getName());
            enrollmentTokenResponse.setValue(androidEnrollmentToken.getValue());
            enrollmentTokenResponse.setAllowPersonalUsage(androidEnrollmentToken.getAllowPersonalUsage());
            enrollmentTokenResponse.setDuration(androidEnrollmentToken.getDuration());
            enrollmentTokenResponse.setAdditionalData(androidEnrollmentToken.getAdditionalData());
            enrollmentTokenResponse.setExpirationTimestamp(androidEnrollmentToken.getExpirationTimestamp());
            enrollmentTokenResponse.setPolicy(mdmPolicy);
            enrollmentTokenResponse.setPolicyName(androidEnrollmentToken.getPolicyName());
            enrollmentTokenResponse.setOneTimeOnly(androidEnrollmentToken.getOneTimeOnly());
            enrollmentTokenResponse.setQrCode(androidEnrollmentToken.getQrCode());
            enrollmentTokenResponse.setDeviceUser(mdmDeviceUser);
            enrollmentTokenResponse.setStatus(EnrollmentTokenStatus.QUEUE);

            return enrollmentTokenResponse;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    @Override
    public EnrollmentTokenResponse delete(Long enrollmentTokenId) throws IOException {
        return null;
    }

    @Override
    public EnrollmentTokenResponse get(Long enrollmentTokenId, Authentication principal)
            throws IOException, EnterpriseNotFoundByUserIdException, EnrollmentTokenNotFoundByIdAndEnterpriseIdException {
        UserDetails userDetails = (MdmUserDetailsImplementer) principal.getPrincipal();
        MdmUserDetails mappedUser = userDetailsService.loadUserByUsername(userDetails.getUsername());

        Optional<Enterprise> enterprise = enterpriseRepository.findByUserId(mappedUser.getId());
        enterprise.orElseThrow(() -> new EnterpriseNotFoundByUserIdException("Enterprise not found with user id: " + mappedUser.getId()));
        Enterprise mdmEnterprise = enterprise.get();

        Optional<EnrollmentToken> enrollmentToken = enrollmentTokenRepository.findByIdAndEnterpriseId(enrollmentTokenId, mdmEnterprise.getId());
        enrollmentToken.orElseThrow(() -> new EnrollmentTokenNotFoundByIdAndEnterpriseIdException(
                "Enrollment token not found with enrollment token id " + enrollmentTokenId + " and enterprise id: " + mdmEnterprise.getId()
            )
        );
        EnrollmentToken mdmEnrollmentToken = enrollmentToken.get();
        EnrollmentTokenResponse enrollmentTokenResponse = new EnrollmentTokenResponse();
        enrollmentTokenResponse.setId(mdmEnrollmentToken.getId());
        enrollmentTokenResponse.setStatus(mdmEnrollmentToken.getStatus());
        enrollmentTokenResponse.setQrCode(mdmEnrollmentToken.getQrCode());
        enrollmentTokenResponse.setPolicy(mdmEnrollmentToken.getPolicy());
        enrollmentTokenResponse.setName(mdmEnrollmentToken.getName());
        enrollmentTokenResponse.setValue(mdmEnrollmentToken.getToken());
        enrollmentTokenResponse.setDeviceUser(mdmEnrollmentToken.getDeviceUser());
        enrollmentTokenResponse.setOneTimeOnly(mdmEnrollmentToken.getOneTimeOnly());
        enrollmentTokenResponse.setPolicyName(mdmEnrollmentToken.getPolicyName());
        enrollmentTokenResponse.setExpirationTimestamp(mdmEnrollmentToken.getExpirationTimestamp());
        enrollmentTokenResponse.setAdditionalData(mdmEnrollmentToken.getAdditionalData());
        enrollmentTokenResponse.setDuration(mdmEnrollmentToken.getDuration());
        enrollmentTokenResponse.setAllowPersonalUsage(mdmEnrollmentToken.getAllowPersonalUsage());

        return enrollmentTokenResponse;
    }
}