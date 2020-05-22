package org.unidue.ub.unidue.almaregister.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.unidue.ub.alma.shared.user.AlmaUser;
import org.unidue.ub.unidue.almaregister.model.AlmaUserRequest;
import org.unidue.ub.unidue.almaregister.service.AlmaUserService;
import org.unidue.ub.unidue.almaregister.service.MissingHisDataException;
import org.unidue.ub.unidue.almaregister.service.MissingShibbolethDataException;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequestMapping(value="/secure")
public class SecuredController {

    private final AlmaUserService almaUserService;

    @Value("${alma.redirect.url}")
    private String redirectUrl;

    SecuredController(AlmaUserService almaUserService) {
        this.almaUserService = almaUserService;
    }

    @GetMapping("/review")
    public String getReviewPage(Model model) throws MissingShibbolethDataException {
        AlmaUserRequest almaUserRequest = this.almaUserService.generateAlmaUserRequestFromShibbolethData();
        model.addAttribute("almaUser", almaUserRequest);
        return "review";
    }

    @PostMapping("/review")
    public String confirmCreation(@ModelAttribute AlmaUserRequest almaUserRequest, BindingResult result, SessionStatus status) {
        boolean error = false;
        if(!almaUserRequest.isPrivacyAccepted){
            result.rejectValue("isPrivacyAccepted", "error.isPrivacyAccepted");
            error = true;
        }
        if(!almaUserRequest.isTermsAccepted){
            result.rejectValue("isTermsAccepted", "error.isTermsAccepted");
            error = true;
        }
        if(error) {
            return "review";
        }
        boolean success = this.almaUserService.createAlmaUser(almaUserRequest.almaUser);
        if (success)
            return "redirect: " + redirectUrl;
        else
            return "error";
    }

    @GetMapping(value = "/activeuser", produces=MediaType.APPLICATION_JSON_VALUE )
    @ResponseBody
    public Map<String, Object> user(Principal principal) {
        try {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("name", principal.getName());
            map.put("roles", AuthorityUtils.authorityListToSet(((Authentication) principal)
                    .getAuthorities()));
            return map;
        } catch (Exception e) {
            return null;
        }
    }

    @ExceptionHandler({MissingShibbolethDataException.class, MissingHisDataException.class})
    public ModelAndView handleException(MissingShibbolethDataException ex)
    {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error");
        modelAndView.addObject("message", ex.getMessage());
        return modelAndView;
    }
}
