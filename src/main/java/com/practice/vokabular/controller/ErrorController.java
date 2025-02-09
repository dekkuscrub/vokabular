package com.practice.vokabular.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @GetMapping("/error")
    public String handleError(HttpServletRequest request) {
        // Check if it's an HTMX request
        String htmxRequest = request.getHeader("HX-Request");
        
        if ("true".equals(htmxRequest)) {
            // Return error fragment for HTMX requests
            return "fragments/error";
        }
        
        // Return full error page for regular requests
        return "error";
    }
} 
