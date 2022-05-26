package com.swe599final.mdm.application.controller;

import com.swe599final.mdm.domain.exception.EnrollmentTokenNotFoundByIdAndEnterpriseIdException;
import com.swe599final.mdm.domain.exception.EnterpriseNotFoundByUserIdException;
import com.swe599final.mdm.domain.repository.EnterpriseRepository;
import com.swe599final.mdm.domain.repository.PolicyRepository;
import com.swe599final.mdm.domain.service.DashboardService;
import com.swe599final.mdm.domain.service.EnrollmentTokenService;
import com.swe599final.mdm.domain.service.MdmUserDetailsService;
import com.swe599final.mdm.infrastructure.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
final public class EnrollmentTokenController {

    @Autowired
    private EnrollmentTokenService enrollmentTokenService;

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private EnterpriseRepository enterpriseRepository;

    @Autowired
    private MdmUserDetailsService userDetailsService;

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/enrollment-tokens/list")
    public String listEnrollmentTokens(Authentication principal) {

        return "enrollment_tokens_list";
    }

    @GetMapping("/enrollment-tokens/{enrollmentTokenId}")
    public String getEnrollmentToken(
        Model model,
        Authentication principal,
        @PathVariable(value = "enrollmentTokenId") String enrollmentTokenId,
        @ModelAttribute("enrollmentToken") EnrollmentTokenResponse enrollmentToken,
        @ModelAttribute("success") String success
    ) throws IOException, EnterpriseNotFoundByUserIdException, EnrollmentTokenNotFoundByIdAndEnterpriseIdException {
        Enterprise applicationUsersEnterprise =
            dashboardService.getApplicationUsersEnterprise((MdmUserDetailsImplementer) principal.getPrincipal());

        model.addAttribute("success", success);
        DeleteEnrollmentTokenDto deleteEnrollmentTokenDto = new DeleteEnrollmentTokenDto();

        if (enrollmentToken.getId() != null) {
            deleteEnrollmentTokenDto.setId(enrollmentToken.getId());
            deleteEnrollmentTokenDto.setName(enrollmentToken.getName());
            model.addAttribute("enrollmentToken", enrollmentToken)
                .addAttribute("principalName", principal.getName())
                .addAttribute("applicationUserHasEnterprise", applicationUsersEnterprise != null)
                .addAttribute("applicationUsersEnterprise", applicationUsersEnterprise)
                .addAttribute("principalId", ((MdmUserDetailsImplementer) principal.getPrincipal()).getId())
                .addAttribute("deleteEnrollmentToken", deleteEnrollmentTokenDto);
        } else {
            enrollmentToken = enrollmentTokenService.get(Long.valueOf(enrollmentTokenId), principal);
            deleteEnrollmentTokenDto.setId(enrollmentToken.getId());
            deleteEnrollmentTokenDto.setName(enrollmentToken.getName());
            model.addAttribute("enrollmentToken", enrollmentToken)
                .addAttribute("principalName", principal.getName())
                .addAttribute("applicationUserHasEnterprise", applicationUsersEnterprise != null)
                .addAttribute("applicationUsersEnterprise", applicationUsersEnterprise)
                .addAttribute("principalId", ((MdmUserDetailsImplementer) principal.getPrincipal()).getId())
                .addAttribute("deleteEnrollmentToken", deleteEnrollmentTokenDto);
        }

        return "enrollment_token_detail";
    }

    @GetMapping("/enrollment-tokens/new")
    public String newEnrollmentToken(Model model, Authentication principal) throws EnterpriseNotFoundByUserIdException {
        EnrollmentTokenDto enrollmentTokenDto = new EnrollmentTokenDto();
        UserDetails userDetails = (MdmUserDetailsImplementer) principal.getPrincipal();
        MdmUserDetails mappedUser = userDetailsService.loadUserByUsername(userDetails.getUsername());
        Optional<Enterprise> enterprise = enterpriseRepository.findByUserId(mappedUser.getId());
        enterprise.orElseThrow(() -> new EnterpriseNotFoundByUserIdException("Enterprise not found with user id: " + mappedUser.getId()));
        List<Policy> policies = policyRepository.findByEnterpriseId(enterprise.get().getId());
        enrollmentTokenDto.setPolicies(policies);
        model.addAttribute("enrollmentToken", enrollmentTokenDto);
        return "enrollment_token_new";
    }

    @PostMapping("/enrollment-tokens/create")
    public RedirectView createEnrollmentToken(EnrollmentTokenDto enrollmentTokenDto, Authentication principal, RedirectAttributes redirectAttributes) {
        try {
            EnrollmentTokenResponse enrollmentTokenResponse = enrollmentTokenService.create(enrollmentTokenDto, principal);
            redirectAttributes.addFlashAttribute("enrollmentToken", enrollmentTokenResponse);
            redirectAttributes.addFlashAttribute("success", true);

            return new RedirectView("/enrollment-tokens/" + enrollmentTokenResponse.getId());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("error", e.getMessage());

            return new RedirectView("/enrollment-tokens/creation-failed");
        }
    }

    @PostMapping("/enrollment-tokens/delete")
    public RedirectView deleteEnrollmentToken(
        DeleteEnrollmentTokenDto deleteEnrollmentTokenDto,
        Authentication principal,
        RedirectAttributes redirectAttributes
    ) {
        try {
            enrollmentTokenService.delete(deleteEnrollmentTokenDto.getId(), deleteEnrollmentTokenDto.getName());
            redirectAttributes.addFlashAttribute("success", true);

            return new RedirectView("/dashboard");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("error", e.getMessage());

            return new RedirectView("/dashboard");
        }
    }

    @PostMapping("/enrollment-tokens/creation-failed")
    public String failCreatePolicy(
        Model model,
        @ModelAttribute("success") String success,
        @ModelAttribute("error") String error
    ) {
        model.addAttribute("success", success);
        model.addAttribute("error", error);

        return "enrollment_token_fail";
    }
}
