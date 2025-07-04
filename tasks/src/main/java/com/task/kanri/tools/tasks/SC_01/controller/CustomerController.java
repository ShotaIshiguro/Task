package com.task.kanri.tools.tasks.SC_01.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.task.kanri.tools.tasks.SC_01.form.SC_01_CustomerListForm;
import com.task.kanri.tools.tasks.SC_01.service.SC_01_SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/customers")
public class CustomerController {
    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private SC_01_SearchService searchService;

    @GetMapping
    @RequestMapping("/init")
    public String init(Model model) {
        logger.info("init");
        // Formの初期化
        SC_01_CustomerListForm form = new SC_01_CustomerListForm();
        model.addAttribute("pageTitle", "取引先一覧");

        // 一覧検索処理
        form = searchService.doExecute(form);
        model.addAttribute("form", form);
        return "customers/list";
    }
}