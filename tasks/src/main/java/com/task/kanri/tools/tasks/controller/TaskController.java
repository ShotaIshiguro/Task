package com.task.kanri.tools.tasks.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    @GetMapping
    public String list(Model model) {
        model.addAttribute("pageTitle", "タスク一覧");
        return "tasks/list";
    }
}