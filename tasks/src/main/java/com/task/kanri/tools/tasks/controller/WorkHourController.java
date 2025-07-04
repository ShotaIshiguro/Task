package com.task.kanri.tools.tasks.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/work-hours")
public class WorkHourController {

    @GetMapping
    public String list(Model model) {
        model.addAttribute("pageTitle", "稼働管理");
        return "work-hours/list";
    }
}