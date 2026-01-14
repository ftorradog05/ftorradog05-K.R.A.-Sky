package com.krasky.krasky.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller // OJO: @Controller, NO @RestController
@RequestMapping("/")
public class HomeController {

    @GetMapping
    public String index() {
        return "index"; // Esto carga templates/index.html
    }
}