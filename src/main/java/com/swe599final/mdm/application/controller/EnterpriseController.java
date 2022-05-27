package com.swe599final.mdm.application.controller;

import com.swe599final.mdm.domain.exception.EnterpriseNotFoundByUserIdException;
import com.swe599final.mdm.domain.service.DashboardService;
import com.swe599final.mdm.domain.service.EnterpriseService;
import com.swe599final.mdm.infrastructure.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

@Controller
final public class EnterpriseController {

    @Autowired
    private EnterpriseService enterpriseService;

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/enterprises/new")
    public String getNewEnterprisePage(Model model, Authentication principal) {
        Enterprise applicationUsersEnterprise =
                dashboardService.getApplicationUsersEnterprise((MdmUserDetailsImplementer) principal.getPrincipal());

        model.addAttribute("enterpriseDto", new EnterpriseDto())
            .addAttribute("principalName", principal.getName())
            .addAttribute("applicationUserHasEnterprise", applicationUsersEnterprise != null)
            .addAttribute("applicationUsersEnterprise", applicationUsersEnterprise)
            .addAttribute("principalId", ((MdmUserDetailsImplementer) principal.getPrincipal()).getId());

        return "new_enterprise";
    }

    @PostMapping("/enterprises/create")
    public RedirectView postCreateEnterpriseForm(EnterpriseDto enterpriseDto, Authentication principal, RedirectAttributes redirectAttributes) {
        try {
            EnterpriseResponse enterpriseResponse = enterpriseService.createEnterprise(enterpriseDto, principal);
            redirectAttributes.addFlashAttribute("enterprise", enterpriseResponse);
            redirectAttributes.addFlashAttribute("success", true);

            return new RedirectView("/enterprises/" + enterpriseResponse.getId());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("error", e.getMessage());

            return new RedirectView("/enterprises/creation-failed");
        }
    }

    @GetMapping("/enterprises/{enterpriseId}")
    public String getEnterprise(
        Model model,
        Authentication principal,
        @PathVariable(value = "enterpriseId") String enterpriseId,
        @ModelAttribute("enterprise") EnterpriseResponse enterprise,
        @ModelAttribute("success") String success
    ) throws IOException, EnterpriseNotFoundByUserIdException {
        Enterprise applicationUsersEnterprise =
                dashboardService.getApplicationUsersEnterprise((MdmUserDetailsImplementer) principal.getPrincipal());

        DeleteEnterpriseDto deleteEnterpriseDto = new DeleteEnterpriseDto();

        model.addAttribute("success", success)
            .addAttribute("principalName", principal.getName())
            .addAttribute("applicationUserHasEnterprise", applicationUsersEnterprise != null)
            .addAttribute("applicationUsersEnterprise", applicationUsersEnterprise)
            .addAttribute("principalId", ((MdmUserDetailsImplementer) principal.getPrincipal()).getId());
        if (enterprise.getId() != null) {
            deleteEnterpriseDto.setId(enterprise.getId());
            deleteEnterpriseDto.setName(enterprise.getName());
            model.addAttribute("enterprise", enterprise)
                .addAttribute("deleteEnterprise", deleteEnterpriseDto);
        } else {
            enterprise = enterpriseService.getEnterprise(Long.valueOf(enterpriseId), principal);
            deleteEnterpriseDto.setId(enterprise.getId());
            deleteEnterpriseDto.setName(enterprise.getName());
            model.addAttribute("enterprise", enterprise)
                .addAttribute("deleteEnterprise", deleteEnterpriseDto);
        }

        return "enterprise_detail";
    }

    @PostMapping("/enterprises/delete")
    public RedirectView deleteEnterprise(
        DeleteEnterpriseDto deleteEnterpriseDto,
        RedirectAttributes redirectAttributes,
        Authentication principal
    ) throws IOException, EnterpriseNotFoundByUserIdException {
        try {
            enterpriseService.deleteEnterprise(deleteEnterpriseDto.getId(), principal);
            redirectAttributes.addFlashAttribute("success", true);

            return new RedirectView("/dashboard");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("error", e.getMessage());

            return new RedirectView("/dashboard");
        }
    }

    @GetMapping("/enterprises/creation-failed")
    public String failCreateEnterprise(
        Model model,
        @ModelAttribute("success") String success,
        @ModelAttribute("error") String error
    ) {
        model.addAttribute("success", success);
        model.addAttribute("error", error);

        return "enterprise_fail";
    }
}
