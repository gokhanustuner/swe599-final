package com.swe599final.mdm.application.controller;

import com.swe599final.mdm.domain.exception.EnterpriseNotFoundByUserIdException;
import com.swe599final.mdm.domain.service.EnterpriseService;
import com.swe599final.mdm.infrastructure.model.EnterpriseDto;
import com.swe599final.mdm.infrastructure.model.EnterpriseResponse;
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
public class EnterpriseController {

    @Autowired
    private EnterpriseService enterpriseService;

    @GetMapping("/enterprises/new")
    public String getNewEnterprisePage(Model model) {
        model.addAttribute("enterpriseDto", new EnterpriseDto());
        return "new_enterprise";
    }

    @PostMapping("/enterprises/create")
    public RedirectView postCreateEnterpriseForm(EnterpriseDto enterpriseDto, Authentication principal, RedirectAttributes redirectAttributes) {
        try {
            EnterpriseResponse enterpriseResponse = enterpriseService.create(enterpriseDto, principal);
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
        model.addAttribute("success", success);
        if (enterprise.getId() != null) {
            model.addAttribute("enterprise", enterprise);
        } else {
            enterprise = enterpriseService.get(Long.valueOf(enterpriseId), principal);
            model.addAttribute("enterprise", enterprise);
        }

        return "enterprise_detail";
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
