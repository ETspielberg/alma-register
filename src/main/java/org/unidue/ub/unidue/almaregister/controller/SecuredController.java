package org.unidue.ub.unidue.almaregister.controller;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.unidue.ub.alma.shared.user.AlmaUser;
import org.unidue.ub.unidue.almaregister.model.AlmaUserRequest;
import org.unidue.ub.unidue.almaregister.service.AlmaUserService;
import org.unidue.ub.unidue.almaregister.service.MissingShibbolethDataException;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequestMapping(value="/secure")
public class SecuredController {

    private final AlmaUserService almaUserService;

    SecuredController(AlmaUserService almaUserService) {
        this.almaUserService = almaUserService;
    }

    @PostMapping("/review")
    public String getReviewPage(Model model) throws MissingShibbolethDataException {
        AlmaUserRequest almaUserRequest = this.almaUserService.generateAlmaUserRequestFromShibbolethData();
        model.addAttribute("almaUser", almaUserRequest);
        return "review";
    }

    @PostMapping("/reviewUser")
    @ResponseBody
    public AlmaUser getUserObject() throws MissingShibbolethDataException {
        AlmaUserRequest almaUserRequest = this.almaUserService.generateAlmaUserRequestFromShibbolethData();
        return this.almaUserService.generateFromAlmaUserRequest(almaUserRequest);
    }


    @PostMapping("/create")
    public String confirmCreation(ModelAndView modelAndView, @RequestBody AlmaUserRequest almaUserRequest) {
        AlmaUser almaUser = this.almaUserService.generateFromAlmaUserRequest(almaUserRequest);
        almaUser = this.almaUserService.createAlmaUser(almaUser);
        modelAndView.addObject("userCreated", almaUser);
        return "success";
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

    @ExceptionHandler(MissingShibbolethDataException.class)
    public ModelAndView handleException(MissingShibbolethDataException ex)
    {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error");
        modelAndView.addObject("message", ex.getMessage());
        return modelAndView;
    }
}
