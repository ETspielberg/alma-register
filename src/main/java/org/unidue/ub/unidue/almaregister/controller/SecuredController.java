package org.unidue.ub.unidue.almaregister.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.unidue.ub.unidue.almaregister.model.AlmaUser;
import org.unidue.ub.unidue.almaregister.service.AlmaUserService;

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
    public String getReviewPage(Model model) {
        AlmaUser almaUser = this.almaUserService.generateAlmaUserFromShibbolethData();
        model.addAttribute("almaUser", almaUser);
        return "review";
    }


    @PostMapping("/create")
    public String confirmCreation(ModelAndView modelAndView, @RequestBody AlmaUser almaUser) {
        AlmaUser almaUserCreated = this.almaUserService.createAlmaUser(almaUser);
        modelAndView.addObject("userCreated", almaUserCreated);
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
}
