package org.unidue.ub.unidue.almaregister.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value="/secure")
public class SecuredController {

    @PostMapping("review")
    public String getReviewPage(ModelAndView modelAndView, HttpServletRequest request) {
        return "review";
    }

}
