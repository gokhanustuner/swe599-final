package com.swe599final.mdm.domain.service;

import com.google.api.services.androidmanagement.v1.model.Device;
import com.google.api.services.androidmanagement.v1.model.Empty;
import com.swe599final.mdm.domain.exception.EnterpriseNotFoundByUserIdException;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.util.List;

public interface DeviceService {
    List<Device> listDevices(Authentication principal) throws IOException, EnterpriseNotFoundByUserIdException;
    Empty deleteDevice(String deviceName) throws IOException;
}
