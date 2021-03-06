package com.swe599final.mdm.domain.service;

import com.google.api.services.androidmanagement.v1.model.Empty;
import com.swe599final.mdm.domain.exception.EnterpriseNotFoundByUserIdException;
import com.swe599final.mdm.domain.repository.DeviceUserRepository;
import com.swe599final.mdm.domain.repository.EnterpriseRepository;
import com.swe599final.mdm.infrastructure.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public final class EnterpriseServiceImplementer implements EnterpriseService {
    @Autowired
    private AndroidManager androidManager;

    @Autowired
    private EnterpriseRepository enterpriseRepository;

    @Autowired
    private MdmUserDetailsService userDetailsService;

    @Autowired
    private DeviceUserRepository deviceUserRepository;

    @Override
    public EnterpriseResponse createEnterprise(EnterpriseDto enterpriseDto, Authentication principal) throws IOException, UsernameNotFoundException {
        com.google.api.services.androidmanagement.v1.model.Enterprise androidEnterprise =
                androidManager.createEnterprise(enterpriseDto.getDisplayName(), enterpriseDto.getEmail());


        UserDetails userDetails = (MdmUserDetailsImplementer) principal.getPrincipal();
        MdmUserDetails mappedUser = userDetailsService.loadUserByUsername(userDetails.getUsername());

        Enterprise mdmEnterprise = new Enterprise();
        mdmEnterprise.setName(androidEnterprise.getName());
        mdmEnterprise.setActive(true);
        mdmEnterprise.setUser(mappedUser.getUser());
        mdmEnterprise = enterpriseRepository.save(mdmEnterprise);

        EnterpriseResponse enterpriseResponse = new EnterpriseResponse();
        enterpriseResponse.setId(mdmEnterprise.getId());
        enterpriseResponse.setName(androidEnterprise.getName());
        enterpriseResponse.setEmail(androidEnterprise.getContactInfo().getContactEmail());
        enterpriseResponse.setDisplayName(androidEnterprise.getEnterpriseDisplayName());

        return enterpriseResponse;
    }

    @Override
    public EnterpriseResponse getEnterprise(Long enterpriseId, Authentication principal) throws IOException, EnterpriseNotFoundByUserIdException {
        UserDetails userDetails = (MdmUserDetailsImplementer) principal.getPrincipal();
        MdmUserDetails mappedUser = userDetailsService.loadUserByUsername(userDetails.getUsername());

        Optional<Enterprise> enterprise = enterpriseRepository.findByIdAndUserId(enterpriseId, mappedUser.getId());
        enterprise.orElseThrow(() -> new EnterpriseNotFoundByUserIdException("Enterprise not found with user id: " + mappedUser.getId()));
        Enterprise mdmEnterprise = enterprise.get();

        com.google.api.services.androidmanagement.v1.model.Enterprise androidEnterprise =
                androidManager.getEnterprise(mdmEnterprise.getName());

        EnterpriseResponse enterpriseResponse = new EnterpriseResponse();
        enterpriseResponse.setId(mdmEnterprise.getId());
        enterpriseResponse.setName(androidEnterprise.getName());
        enterpriseResponse.setDisplayName(androidEnterprise.getEnterpriseDisplayName());
        enterpriseResponse.setEmail(androidEnterprise.getContactInfo().getContactEmail());

        return enterpriseResponse;
    }

    @Override
    public Empty deleteEnterprise(Long enterpriseId, Authentication principal)
            throws IOException, EnterpriseNotFoundByUserIdException {
        UserDetails userDetails = (MdmUserDetailsImplementer) principal.getPrincipal();
        MdmUserDetails mappedUser = userDetailsService.loadUserByUsername(userDetails.getUsername());

        Optional<Enterprise> enterprise = enterpriseRepository.findByIdAndUserId(enterpriseId, mappedUser.getId());
        enterprise.orElseThrow(() -> new EnterpriseNotFoundByUserIdException("Enterprise not found with user id: " + mappedUser.getId()));
        Enterprise mdmEnterprise = enterprise.get();
        deviceUserRepository.deleteAll();
        enterpriseRepository.deleteById(mdmEnterprise.getId());

        return androidManager.deleteEnterprise(mdmEnterprise.getName());
    }
}
