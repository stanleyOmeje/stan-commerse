package com.stan.stancommerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    @RequestMapping("/")
    public String index(){
        return "index";
    }

    @RequestMapping("/message")
    public String sayHello(Model model){
        model.addAttribute("name", "Stan");
        return "index";
    }
}
