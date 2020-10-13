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

/**
 * the main controller for the unprotected web pages (creation of Alma User for external users)
 */
@Controller
public class PublicController {

    @Value("${alma.redirect.url:https://www.uni-due.de/ub}")
    private String redirectUrl;

    private final AlmaUserService almaUserService;

    private final Logger log = LoggerFactory.getLogger(PublicController.class);

    /**
     * constructor based autowiring of the Alma user service
     *
     * @param almaUserService saves AlmaUser objects to Alma
     */
    PublicController(AlmaUserService almaUserService) {
        this.almaUserService = almaUserService;
    }

    /**
     * the controller for the general landing page
     *
     * @return the index page
     */
    @GetMapping({"/", "/index"})
    public String getIndexPage() {
        return "index";
    }

    /**
     * the controller for the actions page, which provides additional information to the user
     *
     * @return the actions page
     */
    @GetMapping("/actions")
    public String getActionsPage() {
        return "actions";
    }

    /**
     * the controller for the confirmation page, that the user was successfully created
     *
     * @return the public success page
     */
    @GetMapping("/success")
    public String getSuccessPage(Model model) {
        model.addAttribute("redirectUrl", redirectUrl);
        return "success";
    }

    /**
     * the controller for the form for the relevant user details
     *
     * @param model the model holding the registration request information bound to the form
     * @return the register page
     */
    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("registrationRequest", new RegistrationRequest());
        return "register";
    }

    /**
     * controller for the registration request submission (POST).
     *
     * @param registrationRequest the registration request object, from which the AlmaUser object can be retreived.
     * @param result              the result to display rejection if the privacy conditions or the terms of use have not been accepted
     * @return the registration page with errors, if the terms or the privacy was not accepted, otherwise a redirect to the success page
     */
    @PostMapping("/register")
    public RedirectView registerAlmaUser(@ModelAttribute RegistrationRequest registrationRequest, BindingResult result) {
        boolean error = false;
        log.info(registrationRequest.firstName + " " + registrationRequest.lastName);
        log.info("Privacy: " + registrationRequest.privacyAccepted);
        log.info("Terms: " + registrationRequest.termsAccepted);
        if (!registrationRequest.privacyAccepted) {
            result.rejectValue("privacyAccepted", "error.privacyAccepted");
            log.warn("error.privacyAccepted");
            error = true;
        }
        if (!registrationRequest.termsAccepted) {
            result.rejectValue("termsAccepted", "error.termsAccepted");
            log.warn("error.termsAccepted");
            error = true;
        }
        if (error)
            return new RedirectView("register");
        try {
            this.almaUserService.createAlmaUser(registrationRequest.getAlmaUser(), true);
            return new RedirectView("success");
        } catch (Exception e) {
            throw new AlmaConnectionException("could not create user");
        }
    }
}
