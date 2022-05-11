package com.swe599final.mdm.application.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
final public class IndexController {
    @GetMapping("/")
    public String getIndexPage() {
        return "index";
    }
}
