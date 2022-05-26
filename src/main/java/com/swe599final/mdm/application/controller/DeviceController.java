package com.swe599final.mdm.application.controller;

import com.google.api.services.androidmanagement.v1.model.Device;
import com.swe599final.mdm.domain.exception.EnterpriseNotFoundByUserIdException;
import com.swe599final.mdm.domain.service.DashboardService;
import com.swe599final.mdm.domain.service.DeviceService;
import com.swe599final.mdm.infrastructure.model.Enterprise;
import com.swe599final.mdm.infrastructure.model.EnterpriseDto;
import com.swe599final.mdm.infrastructure.model.MdmUserDetailsImplementer;
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
    private DeviceService deviceService;

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/devices/list")
    public String listDevices(Authentication principal, Model model) throws IOException, EnterpriseNotFoundByUserIdException {
        Enterprise applicationUsersEnterprise =
                dashboardService.getApplicationUsersEnterprise((MdmUserDetailsImplementer) principal.getPrincipal());

        List<Device> devices = deviceService.listDevices(principal);
        model.addAttribute("devices", devices)
            .addAttribute("enterpriseDto", new EnterpriseDto())
            .addAttribute("principalName", principal.getName())
            .addAttribute("applicationUserHasEnterprise", applicationUsersEnterprise != null)
            .addAttribute("applicationUsersEnterprise", applicationUsersEnterprise)
            .addAttribute("principalId", ((MdmUserDetailsImplementer) principal.getPrincipal()).getId());

        return "device_list";
    }
}
