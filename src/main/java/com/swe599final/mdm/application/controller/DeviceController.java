package com.swe599final.mdm.application.controller;

import com.google.api.services.androidmanagement.v1.model.Device;
import com.swe599final.mdm.domain.exception.EnterpriseNotFoundByUserIdException;
import com.swe599final.mdm.domain.service.DashboardService;
import com.swe599final.mdm.domain.service.DeviceService;
import com.swe599final.mdm.infrastructure.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

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
            .addAttribute("principalName", principal.getName())
            .addAttribute("applicationUserHasEnterprise", applicationUsersEnterprise != null)
            .addAttribute("applicationUsersEnterprise", applicationUsersEnterprise)
            .addAttribute("principalId", ((MdmUserDetailsImplementer) principal.getPrincipal()).getId())
            .addAttribute("deleteDevice", new DeleteDeviceDto());

        return "device_list";
    }

    @PostMapping("/devices/delete")
    public RedirectView deleteDevice(DeleteDeviceDto deleteDeviceDto, Authentication principal, RedirectAttributes redirectAttributes) {
        try {
            deviceService.deleteDevice(deleteDeviceDto.getName());
            redirectAttributes.addFlashAttribute("success", true);

            return new RedirectView("/dashboard");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("error", e.getMessage());

            return new RedirectView("/dashboard");
        }
    }
}
