package org.unidue.ub.unidue.almaregister.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.support.SessionStatus;
import org.unidue.ub.unidue.almaregister.model.RegistrationRequest;
import org.unidue.ub.unidue.almaregister.service.AlmaUserService;

@Controller
public class PublicController {

    @Value("${alma.redirect.url}")
    private String redirectUrl;

    private final AlmaUserService almaUserService;

    PublicController(AlmaUserService almaUserService) {
        this.almaUserService = almaUserService;
    }

    @GetMapping("/index")
    public String getIndexPage() {
        return "index";
    }

    @GetMapping("/actions")
    public String getActionsPage() {
        return "actions";
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("registrationRequest", new RegistrationRequest());
        return "register";
    }

    @PostMapping("/register")
    public String registerAlmaUser(@ModelAttribute RegistrationRequest registrationRequest, BindingResult result, SessionStatus status) {
        boolean error = false;
        if(!registrationRequest.isPrivacyAccepted){
            result.rejectValue("isPrivacyAccepted", "error.isPrivacyAccepted");
            error = true;
        }
        if(!registrationRequest.isTermsAccepted){
            result.rejectValue("isTermsAccepted", "error.isTermsAccepted");
            error = true;
        }
        if(error) {
            return "review";
        }boolean success = this.almaUserService.createAlmaUser(registrationRequest.getAlmaUser());
        if (success)
            return "redirect: " + redirectUrl;
        else
            return "error";
    }

}
