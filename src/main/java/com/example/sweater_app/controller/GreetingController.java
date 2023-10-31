package com.example.sweater_app.controller;

import com.example.sweater_app.constants.ViewName;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class GreetingController {

    @GetMapping
    public ModelAndView greetingPage(){
        return new ModelAndView(ViewName.GREETING);
    }
}
