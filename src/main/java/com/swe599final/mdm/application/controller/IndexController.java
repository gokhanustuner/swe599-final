package com.swe599final.mdm.application.controller;

import com.swe599final.mdm.MdmApplication;
import com.swe599final.mdm.domain.service.AndroidManager;
import com.swe599final.mdm.domain.service.PubsubOutboundGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
final public class IndexController {
    @Autowired
    private AndroidManager androidManager;

    @GetMapping("/")
    public String getIndexPage() throws IOException {
        return "index";
    }
}
