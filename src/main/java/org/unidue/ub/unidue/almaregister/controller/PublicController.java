package org.unidue.ub.unidue.almaregister.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.unidue.ub.alma.shared.user.AlmaUser;
import org.unidue.ub.alma.shared.user.UserStatus;
import org.unidue.ub.unidue.almaregister.model.RegistrationRequest;
import org.unidue.ub.unidue.almaregister.service.exceptions.AlmaConnectionException;
import org.unidue.ub.unidue.almaregister.service.AlmaUserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

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
    public String getIndexPage(Model model) {
        model.addAttribute("module", "index");
        return "index";
    }

    /**
     * the controller for the actions page, which provides additional information to the user
     *
     * @return the actions page
     */
    @GetMapping("/actions")
    public String getActionsPage(Model model) {
        model.addAttribute("module", "actions");
        return "actions";
    }

    /**
     * the controller for the confirmation page, that the user was successfully created
     *
     * @return the public success page
     */
    @GetMapping("/success")
    public String getSuccessPage(Model model, @ModelAttribute("userGroup") final String userGroup) {
        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("userGroup", userGroup);
        model.addAttribute("module", "success");
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
        model.addAttribute("module", "register");
        model.addAttribute("registrationRequest", new RegistrationRequest());
        return "register";
    }

    /**
     * controller for the registration request submission (POST).
     *
     * @param registrationRequest the registration request object, from which the AlmaUser object can be retreived.
     * @param locale the selected locale (currently 'en' and 'de' supported)
     * @param model the model to be used for the display of the results
     * @return the registration page with errors, if the terms or the privacy was not accepted, otherwise a redirect to the success page
     */
    @PostMapping("/register")
    public String registerAlmaUser(@ModelAttribute RegistrationRequest registrationRequest, Locale locale, Model model, HttpServletRequest httpServletRequest) {
        if (this.almaUserService.userExists(registrationRequest)) {
            model.addAttribute("registrationRequest", registrationRequest);
            model.addAttribute("redirectUrl", redirectUrl);
            return "alreadyExists";
        } else {
            try {
                AlmaUser almaUser = this.almaUserService.createAlmaUser(registrationRequest.getAlmaUser(locale.getLanguage(), true), true);
                log.info(String.format("User %s %s sucessfully registered. primaryId: %s, userGroup: %s, remoteAddress: %s, userAgent; %s",
                        almaUser.getFirstName(),
                        almaUser.getLastName(),
                        almaUser.getPrimaryId(),
                        almaUser.getUserGroup().getValue(),
                        httpServletRequest.getRemoteAddr(),
                        httpServletRequest.getHeader("User-Agent")));
                return "nearlyFinished";
            } catch (Exception e) {
                log.warn("An error occurred", e);
                throw new AlmaConnectionException("could not create user");
            }
        }
    }

    @GetMapping("/alreadyExists")
    public String showAlreadyExistPage() {
        return "alreadyExists";
    }

    @PostMapping("/confirmRegister")
    public String registerAlmaUserAnyway(@ModelAttribute RegistrationRequest registrationRequest, Locale locale) {
        log.info(registrationRequest.firstName + " " + registrationRequest.lastName);
        log.info("Privacy: " + registrationRequest.privacyAccepted);
        log.info("Terms: " + registrationRequest.termsAccepted);
        AlmaUser almaUser = this.almaUserService.createAlmaUser(registrationRequest.getAlmaUser(locale.getLanguage(), false), true);
        almaUser.setStatus(new UserStatus().value("INACTIVE"));
        log.info(String.format("User %s %s sucessfully registered with new id %s",
                almaUser.getFirstName(), almaUser.getLastName(), almaUser.getPrimaryId()));
        return "nearlyFinished";
    }
}
