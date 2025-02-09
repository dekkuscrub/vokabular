package com.practice.vokabular.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String search() {
        return "layout/main";
    }

    @GetMapping("/words")
    public String wordList() {
        return "layout/main";
    }

    @GetMapping("/practice")
    public String practice() {
        return "layout/main";
    }
} 
