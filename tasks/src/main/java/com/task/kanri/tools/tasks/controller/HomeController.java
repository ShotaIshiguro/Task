package com.task.kanri.tools.tasks.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("message", "Spring Boot + MyBatis + NEON DB + Tailwind CSS アプリケーションが正常に動作しています！");
        return "home";
    }
}