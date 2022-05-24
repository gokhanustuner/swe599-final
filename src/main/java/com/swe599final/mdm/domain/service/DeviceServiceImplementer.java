package com.swe599final.mdm.domain.service;

import com.google.api.services.androidmanagement.v1.model.Device;
import com.google.api.services.androidmanagement.v1.model.Empty;
import com.swe599final.mdm.domain.exception.EnterpriseNotFoundByUserIdException;
import com.swe599final.mdm.domain.repository.EnterpriseRepository;
import com.swe599final.mdm.infrastructure.model.Enterprise;
import com.swe599final.mdm.infrastructure.model.MdmUserDetails;
import com.swe599final.mdm.infrastructure.model.MdmUserDetailsImplementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public final class DeviceServiceImplementer implements DeviceService {
    @Autowired
    private AndroidManager androidManager;

    @Autowired
    private EnterpriseRepository enterpriseRepository;

    @Autowired
    private MdmUserDetailsService userDetailsService;

    @Override
    public List<Device> listDevices(Authentication principal) throws IOException, EnterpriseNotFoundByUserIdException {
        UserDetails userDetails = (MdmUserDetailsImplementer) principal.getPrincipal();
        MdmUserDetails mappedUser = userDetailsService.loadUserByUsername(userDetails.getUsername());

        Optional<Enterprise> enterprise = enterpriseRepository.findByUserId(mappedUser.getId());
        enterprise.orElseThrow(() -> new EnterpriseNotFoundByUserIdException("Enterprise not found with user id: " + mappedUser.getId()));
        Enterprise mdmEnterprise = enterprise.get();

        return androidManager.listDevices(mdmEnterprise.getName());
    }

    @Override
    public Empty deleteDevice(String deviceName) throws IOException {
        return androidManager.deleteDevice(deviceName);
    }
}
