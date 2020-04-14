package org.unidue.ub.unidue.almaregister.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PublicController {

    @GetMapping("/index")
    public String getIndexPage() {
        return "index";
    }

    @GetMapping("/actions")
    public String getActionsPage() {
        return "actions";
    }

}
