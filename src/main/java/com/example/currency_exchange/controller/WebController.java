package com.example.currency_exchange.controller;

import com.example.currency_exchange.entity.User;
import com.example.currency_exchange.model.LoginUser;
import com.example.currency_exchange.model.UserForm;
import com.example.currency_exchange.service.CurrencyService;
import com.example.currency_exchange.service.UserService;
import com.example.currency_exchange.util.ConvertUtil;
import com.example.currency_exchange.validator.LoginUserValidator;
import com.example.currency_exchange.validator.RegistrationFormValidator;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Log4j2
@Controller
@RequestMapping("/web")
public class WebController {
    private final UserService userService;
    private final CurrencyService currencyService;
    private final RegistrationFormValidator registrationFormValidator;
    private final LoginUserValidator loginUserValidator;

    public WebController(UserService userService, CurrencyService currencyService,
                         RegistrationFormValidator registrationFormValidator,
                         LoginUserValidator loginUserValidator) {
        this.userService = userService;
        this.currencyService = currencyService;
        this.registrationFormValidator = registrationFormValidator;
        this.loginUserValidator = loginUserValidator;
    }


    @InitBinder
    protected void initBinder(WebDataBinder dataBinder) {

        Object target = dataBinder.getTarget();

        if (target != null && target.getClass() == UserForm.class) {
            dataBinder.setValidator(registrationFormValidator);
        }
        if (target != null && target.getClass() == LoginUser.class) {
            dataBinder.setValidator(loginUserValidator);
        }
    }

    /**
     * http://localhost:8080/web/register
     */
    @GetMapping("/register")
    public ModelAndView register() {
        final ModelAndView modelAndView = new ModelAndView("registration");
        modelAndView.addObject("form", new UserForm());
        return modelAndView;
    }

    @PostMapping("/register")
    public ModelAndView register(@ModelAttribute("form")
                                 @Validated UserForm userForm, BindingResult result, RedirectAttributes ra) {
        final ModelAndView mav = new ModelAndView();
        if (result.hasErrors()) {
            mav.setViewName("registration");
        } else {
            final User user = ConvertUtil.convert(userForm);
            log.info(user);
            userService.save(user);
            ra.addFlashAttribute("message", String.format("%s! successfully registered", userForm.getFullName()));
            mav.setViewName("redirect:/web/login");
        }
        return mav;
    }

    /**
     * http://localhost:8080/web/login
     */
    @GetMapping("/login")
    public ModelAndView loginPage(HttpSession session) {
        session.invalidate();
        final ModelAndView mav = new ModelAndView("index");
        mav.addObject("loginUser", new LoginUser());
        return mav;
    }

    @PostMapping("/login")
    public ModelAndView postLogin(@ModelAttribute("loginUser") @Validated LoginUser loginUser,
                                  BindingResult result, HttpSession session) {

        final ModelAndView mav = new ModelAndView();
        if (result.hasErrors()) {
            mav.setViewName("index");
        } else {
            final Optional<User> user = userService.getUserByEmail(loginUser);
            user.ifPresent(u->{
                session.setAttribute("user", user.get());
            });
            mav.setViewName("redirect:/web/main-page");
        }

        return mav;
    }

    @GetMapping("/main-page")
    public ModelAndView mainPage(HttpSession session) {
        final ModelAndView mav = new ModelAndView();
        final User user = (User) session.getAttribute("user");
        if (user != null) {
            mav.addObject("fullName",user.getFullName());
            mav.setViewName("main-page-authorized");
        } else mav.setViewName("main-page");
        return mav;
    }
}
