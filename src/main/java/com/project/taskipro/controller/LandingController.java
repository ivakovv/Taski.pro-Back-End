package com.project.taskipro.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LandingController {

    @GetMapping("/")
    public String landing(Model model) {
        model.addAttribute("title", "TaskiPro - Удобное управление задачами");
        model.addAttribute("currentYear", java.time.Year.now().getValue());
        return "landing";
    }
} 