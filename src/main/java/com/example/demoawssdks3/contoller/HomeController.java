package com.example.demoawssdks3.contoller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/upload")
    public String upload(){
        return "upload";
    }
}
