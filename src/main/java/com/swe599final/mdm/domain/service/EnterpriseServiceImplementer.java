package com.swe599final.mdm.domain.service;

import com.google.api.services.androidmanagement.v1.model.ContactInfo;
import com.swe599final.mdm.domain.exception.EnterpriseNotFoundByUserIdException;
import com.swe599final.mdm.domain.repository.EnterpriseRepository;
import com.swe599final.mdm.domain.repository.UserRepository;
import com.swe599final.mdm.infrastructure.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class EnterpriseServiceImplementer implements EnterpriseService {
    @Autowired
    AndroidManager androidManager;

    @Autowired
    EnterpriseRepository enterpriseRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public EnterpriseResponse create(EnterpriseDto enterpriseDto, Authentication principal) throws IOException {
        com.google.api.services.androidmanagement.v1.model.Enterprise androidEnterprise =
                androidManager.createEnterprise(enterpriseDto.getDisplayName(), enterpriseDto.getEmail());

        UserDetails userDetails = (MdmUserDetails) principal.getPrincipal();
        Optional<User> user = userRepository.findByEmail(userDetails.getUsername());
        user.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + userDetails.getUsername()));
        MdmUserDetails mappedUser = user.map(MdmUserDetails::new).get();

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
    public EnterpriseResponse get(Long enterpriseId, Authentication principal) throws IOException, EnterpriseNotFoundByUserIdException {
        UserDetails userDetails = (MdmUserDetails) principal.getPrincipal();
        Optional<User> user = userRepository.findByEmail(userDetails.getUsername());
        user.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + userDetails.getUsername()));
        MdmUserDetails mappedUser = user.map(MdmUserDetails::new).get();
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
    public Enterprise update(EnterpriseId enterpriseId, String displayName, ContactInfo contactInfo) {
        return null;
    }

    @Override
    public void delete(EnterpriseId enterpriseId) {

    }
}
