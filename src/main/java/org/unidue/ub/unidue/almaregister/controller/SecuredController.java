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
        RegistrationRequest registrationRequest = this.almaUserService.generateRegistrationRequest();
        AlmaUser almaUser = this.almaUserService.checkExistingUser(registrationRequest.primaryId);
        if (almaUser == null) {
            model.addAttribute("registrationRequest", registrationRequest);
            return "review";
        } else {
            model.addAttribute("almaUser", almaUser);
            return "update";
        }
    }

    @PostMapping("/review")
    public RedirectView confirmCreation(@ModelAttribute RegistrationRequest registrationRequest, BindingResult result) throws AlmaConnectionException {
        boolean error = false;
        if(!registrationRequest.privacyAccepted){
            result.rejectValue("privacyAccepted", "error.privacyAccepted");
            error = true;
        }
        if(!registrationRequest.termsAccepted){
            result.rejectValue("termsAccepted", "error.termsAccepted");
            error = true;
        }
        if(error) {
            return  new RedirectView("review");
        }
        AlmaUser almaUser = registrationRequest.getAlmaUser();
        this.almaUserService.createAlmaUser(almaUser, false);
        //return new RedirectView("redirect: https://" + redirectUrl);
        return new RedirectView("success");
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

    @ExceptionHandler({MissingShibbolethDataException.class, MissingHisDataException.class, AlmaConnectionException.class})
    public ModelAndView handleException(Exception ex)
    {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error");
        modelAndView.addObject("message", ex.getMessage());
        return modelAndView;
    }
}
