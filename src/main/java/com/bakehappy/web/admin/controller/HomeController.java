package com.bakehappy.web.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String root() {
        return "redirect:/admin/login";
    }

    @GetMapping("/index")
    public String mostrarIndex(Model model) {
        model.addAttribute("title", "Pastelería");
        return "index";
    }
}
