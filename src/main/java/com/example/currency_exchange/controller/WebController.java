package com.example.currency_exchange.controller;

import com.example.currency_exchange.entity.User;
import com.example.currency_exchange.ex.UserNotFoundEx;
import com.example.currency_exchange.model.CurrencyResponse;
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
import java.math.BigDecimal;

@Log4j2
@Controller
@RequestMapping({"/web", "/"})
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

    @GetMapping("/")
    public String redirect() {
        return "redirect:/web/login";
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
            final User insertedUser = userService.save(user);
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
        ModelAndView mav = new ModelAndView();
        if (result.hasErrors()) {
            mav.setViewName("index");
        } else {
            final User user = userService.getUserByEmail(loginUser);
            session.setAttribute("user", user);
            mav.setViewName("redirect:/web/main-page-auth");
        }

        return mav;
    }

    @GetMapping("/main-page-auth")
    public ModelAndView mainPage(HttpSession session) {
        final ModelAndView mav = new ModelAndView("main-page-authorized");
        final BigDecimal usd = currencyService.convert("USD", "EUR", BigDecimal.ONE);
        final BigDecimal eur = currencyService.convert("EUR", "USD", BigDecimal.ONE);
        final CurrencyResponse currencyResponse = new CurrencyResponse(usd, eur);
        final User user = (User) session.getAttribute("user");
        mav.addObject("fullName", user.getFullName());
        mav.addObject("currencyResponse", currencyResponse);
        return mav;
    }

    @GetMapping("/main-page-guest")
    public ModelAndView mainPageGuest() {
        final ModelAndView mav = new ModelAndView("main-page");
        final BigDecimal usd = currencyService.convert("USD", "EUR", BigDecimal.ONE);
        final BigDecimal eur = currencyService.convert("EUR", "USD", BigDecimal.ONE);
        final CurrencyResponse currencyResponse = new CurrencyResponse(usd, eur);
        mav.addObject("currencyResponse", currencyResponse);
        return mav;
    }

    @ExceptionHandler({UserNotFoundEx.class})
    public ModelAndView userNotFoundHandle() {
        log.info("caught: UserNotFoundEx");
        final ModelAndView mav = new ModelAndView("index");
        mav.addObject("exMessage", "email or password is incorrect");
        mav.addObject("loginUser", new LoginUser());
        return mav;
    }
}
