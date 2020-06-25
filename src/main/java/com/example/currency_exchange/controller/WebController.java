package com.example.currency_exchange.controller;

import com.example.currency_exchange.model.UserForm;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/web")
public class WebController {
    /**
     * http://localhost:8080/web
     * @return
     */
    @GetMapping
    public ModelAndView index() {
        return new ModelAndView("index");
    }

    /**
     * http://localhost:8080/web/register
     * @return
     */
    @GetMapping("/register")
    public ModelAndView register() {
        return new ModelAndView("registration");
    }

    @PostMapping("/register")
    public RedirectView register(@Validated @ModelAttribute("form") UserForm userForm){
        return new RedirectView("/web/login");
    }
}
