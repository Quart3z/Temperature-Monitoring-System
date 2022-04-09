package com.example.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RestController
@RequestMapping
public class LoginController {

    // Display of login page
    @GetMapping("/authentication")
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("authentication");

        return modelAndView;
    }

}
