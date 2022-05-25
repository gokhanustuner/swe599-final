package com.swe599final.mdm.application.controller;

import com.google.api.services.androidmanagement.v1.model.ApplicationPolicy;
import com.swe599final.mdm.domain.exception.EnterpriseNotFoundByUserIdException;
import com.swe599final.mdm.domain.exception.PolicyNotFoundByIdAndEnterpriseIdException;
import com.swe599final.mdm.domain.service.DashboardService;
import com.swe599final.mdm.domain.service.PolicyService;
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
final public class PolicyController {

    @Autowired
    private PolicyService policyService;

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/policies/list")
    public String listPolicies(Model model, Authentication principal) {

        return "policies_list";
    }

    @GetMapping("/policies/{policyId}")
    public String getPolicy(
        Model model,
        Authentication principal,
        @PathVariable(value = "policyId") String policyId,
        @ModelAttribute("policy") PolicyResponse policy,
        @ModelAttribute("success") String success
    ) throws IOException, EnterpriseNotFoundByUserIdException, PolicyNotFoundByIdAndEnterpriseIdException {
        model.addAttribute("success", success);

        if (policy.getId() != null) {
            model.addAttribute("policy", policy);
        } else {
            policy = policyService.get(Long.valueOf(policyId), principal);
            model.addAttribute("policy", policy);
        }

        return "policy_detail";
    }

    @GetMapping("/policies/new")
    public String newPolicy(Model model, Authentication principal) {
        PolicyDto policyDto = new PolicyDto();
        policyDto.addApplication(new ApplicationPolicy());
        Enterprise applicationUsersEnterprise =
                dashboardService.getApplicationUsersEnterprise((MdmUserDetailsImplementer) principal.getPrincipal());
        model.addAttribute("policy", policyDto)
            .addAttribute("principalName", principal.getName())
            .addAttribute("applicationUserHasEnterprise", applicationUsersEnterprise != null)
            .addAttribute("applicationUsersEnterprise", applicationUsersEnterprise)
            .addAttribute("principalId", ((MdmUserDetailsImplementer) principal.getPrincipal()).getId());

        return "policy_new";
    }

    @PostMapping("/policies/create")
    public RedirectView createPolicy(PolicyDto policyDto, Authentication principal, RedirectAttributes redirectAttributes) {
        try {
            PolicyResponse policyResponse = policyService.create(policyDto, principal);
            redirectAttributes.addFlashAttribute("policy", policyResponse);
            redirectAttributes.addFlashAttribute("success", true);

            return new RedirectView("/policies/" + policyResponse.getId());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("error", e.getMessage());

            return new RedirectView("/policies/creation-failed");
        }
    }

    @PostMapping("/policies/creation-failed")
    public String failCreatePolicy(
        Model model,
        @ModelAttribute("success") String success,
        @ModelAttribute("error") String error
    ) {
        model.addAttribute("success", success);
        model.addAttribute("error", error);

        return "policy_fail";
    }

}
