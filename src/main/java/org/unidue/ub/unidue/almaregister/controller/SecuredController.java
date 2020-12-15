package org.unidue.ub.unidue.almaregister.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.unidue.ub.alma.shared.user.AlmaUser;
import org.unidue.ub.unidue.almaregister.model.RegistrationRequest;
import org.unidue.ub.unidue.almaregister.service.AlmaConnectionException;
import org.unidue.ub.unidue.almaregister.service.AlmaUserService;
import org.unidue.ub.unidue.almaregister.service.MissingHisDataException;
import org.unidue.ub.unidue.almaregister.service.MissingShibbolethDataException;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * the main controller for all Shibboleth-secured controller. The request mapping (e.g. '/secure') needs to be adapted to the shibboleth settings of the shibd service
 */
@Controller
@RequestMapping(value = "/secure")
public class SecuredController {

    private final AlmaUserService almaUserService;

    @Value("${alma.redirect.url:https://www.uni-due.de/ub}")
    private String redirectUrl;

    @GetMapping("/success")
    public String getSuccessPage(Model model) {
        model.addAttribute("redirectUrl", redirectUrl);
        return "success";
    }

    /**
     * constructor based autowiring of the Alma user service
     *
     * @param almaUserService the Alma user service, responsible for retrieving the registration request object from the Shibboleth request attributes and to submit the corresponding AlmaUser object to the Alma Users API
     */
    SecuredController(AlmaUserService almaUserService) {
        this.almaUserService = almaUserService;
    }

    /**
     * the controller for reviewing the data as obtained by the Shibboleth login procedure
     * @param model the model object binding the registration request object to the web form
     * @return returns the review page
     * @throws MissingShibbolethDataException thrown if the necessary data could not be read from the Shibboleth request attributes
     */
    @GetMapping("/review")
    public String getReviewPage(Model model) throws MissingShibbolethDataException {
        RegistrationRequest registrationRequest = this.almaUserService.generateRegistrationRequest();
        AlmaUser almaUser = this.almaUserService.checkExistingUser(registrationRequest.primaryId);
        if (almaUser == null) {
            model.addAttribute("registrationRequest", registrationRequest);
            return "review";
        } else {
            model.addAttribute("redirectUrl", redirectUrl);
            return "alreadyExists";
        }
    }

    /**
     * submission controller accepting the registration request object as generated from the Shibboleth data.
     * @param registrationRequest the registration request object as obtained from the Shibboleth request data
     * @param result the result to display rejection if the privacy conditions or the terms of use have not been accepted
     * @return the review page with errors, if the terms or the privacy was not accepted, otherwise a redirect to the success page
     * @throws AlmaConnectionException thrown if no connection to the alma Users API could be established
     */
    @PostMapping("/review")
    public RedirectView confirmCreation(@ModelAttribute RegistrationRequest registrationRequest, BindingResult result, Locale locale) throws AlmaConnectionException {
        boolean error = false;
        if (!registrationRequest.privacyAccepted) {
            result.rejectValue("privacyAccepted", "error.privacyAccepted");
            error = true;
        }
        if (!registrationRequest.termsAccepted) {
            result.rejectValue("termsAccepted", "error.termsAccepted");
            error = true;
        }
        if (error) {
            return new RedirectView("review");
        }
        AlmaUser almaUser = registrationRequest.getAlmaUser(locale.getLanguage());
        this.almaUserService.createAlmaUser(almaUser, false);
        //return new RedirectView("redirect: https://" + redirectUrl);
        return new RedirectView("success");
    }

    /**
     * endpoint for reviewing the current user and the corresponding authorities (for debugging purposes or as source for the user object within a single page application
     * @param principal the principal object
     * @return a map holding the name of the principal and the corresponding roles-
     */
    @GetMapping(value = "/activeuser", produces = MediaType.APPLICATION_JSON_VALUE)
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

    /**
     * Exception handling if one of the following exceptions is thrown: MissingShibbolethDataException, MissingHisDataException, AlmaConnectionException
     * @param ex Exception to be handled
     * @return the error page bound to the error message.
     */
    @ExceptionHandler({MissingShibbolethDataException.class, MissingHisDataException.class, AlmaConnectionException.class})
    public ModelAndView handleException(Exception ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error");
        modelAndView.addObject("message", ex.getMessage());
        return modelAndView;
    }
}
