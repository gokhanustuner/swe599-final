package com.swe599final.mdm.application.controller;

import com.google.api.services.androidmanagement.v1.model.Device;
import com.swe599final.mdm.domain.exception.EnterpriseNotFoundByUserIdException;
import com.swe599final.mdm.domain.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.List;

@Controller
public final class DeviceController {

    @Autowired
    DeviceService deviceService;

    @GetMapping("/devices/list")
    public String listDevices(Authentication principal, Model model) throws IOException, EnterpriseNotFoundByUserIdException {
        List<Device> devices = deviceService.listDevices(principal);
        model.addAttribute("devices", devices);

        return "device_list";
    }
}
