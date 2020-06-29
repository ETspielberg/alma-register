package org.unidue.ub.unidue.almaregister.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import org.unidue.ub.unidue.almaregister.model.RegistrationRequest;
import org.unidue.ub.unidue.almaregister.service.AlmaConnectionException;
import org.unidue.ub.unidue.almaregister.service.AlmaUserService;

@Controller
public class PublicController {

    @Value("${alma.redirect.url:www.uni-due.de/ub}")
    private String redirectUrl;

    private final AlmaUserService almaUserService;

    private final Logger log = LoggerFactory.getLogger(PublicController.class);

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

    @GetMapping("/success")
    public String getSuccessPage() {
        return "success";
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("registrationRequest", new RegistrationRequest());
        return "register";
    }

    @PostMapping("/register")
    public RedirectView registerAlmaUser(@ModelAttribute RegistrationRequest registrationRequest, BindingResult result) {
        boolean error = false;
        log.info(registrationRequest.firstName + " " + registrationRequest.lastName);
        log.info("Privacy: " + registrationRequest.privacyAccepted);
        log.info("Terms: " + registrationRequest.termsAccepted);
        if(!registrationRequest.privacyAccepted){
            result.rejectValue("privacyAccepted", "error.privacyAccepted");
            log.warn("error.privacyAccepted");
            error = true;
        }
        if(!registrationRequest.termsAccepted){
            result.rejectValue("termsAccepted", "error.termsAccepted");
            log.warn("error.termsAccepted");
            error = true;
        }
        if(error)
            return new RedirectView("register");
        try {
            this.almaUserService.createAlmaUser(registrationRequest.getAlmaUser(), true);
            return new RedirectView("success");
        } catch (Exception e) {
            throw new AlmaConnectionException("could not create user");
        }
    }
}
