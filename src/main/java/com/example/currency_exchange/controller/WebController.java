package com.example.currency_exchange.controller;

import com.example.currency_exchange.api.send_to_frontend.RateSaver;
import com.example.currency_exchange.entity.Person;
import com.example.currency_exchange.entity.RegistrationToken;
import com.example.currency_exchange.ex.PersonNotFoundEx;
import com.example.currency_exchange.model.CurrencyNames;
import com.example.currency_exchange.model.LoginPerson;
import com.example.currency_exchange.model.PersonForm;
import com.example.currency_exchange.repository.RegistrationTokenRepo;
import com.example.currency_exchange.service.CurrencyService;
import com.example.currency_exchange.service.EmailService;
import com.example.currency_exchange.service.PersonService;
import com.example.currency_exchange.util.ConvertUtil;
import com.example.currency_exchange.validator.LoginPersonValidator;
import com.example.currency_exchange.validator.RegistrationFormValidator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Controller
@RequestMapping({"/web", "/"})
public class WebController {
    @Value("${spring.mail.username}")
    private String ourEmailAddress;
    private final PersonService personService;
    private final CurrencyService currencyService;
    private final RegistrationFormValidator registrationFormValidator;
    private final LoginPersonValidator loginPersonValidator;
    private final EmailService emailService;
    private final RegistrationTokenRepo registrationTokenRepo;

    public WebController(PersonService personService, CurrencyService currencyService,
                         RegistrationFormValidator registrationFormValidator,
                         LoginPersonValidator loginPersonValidator, EmailService emailService, RegistrationTokenRepo registrationTokenRepo) {
        this.personService = personService;
        this.currencyService = currencyService;
        this.registrationFormValidator = registrationFormValidator;
        this.loginPersonValidator = loginPersonValidator;
        this.emailService = emailService;
        this.registrationTokenRepo = registrationTokenRepo;
    }


    @InitBinder
    protected void initBinder(WebDataBinder dataBinder) {

        Object target = dataBinder.getTarget();

        if (target != null && target.getClass() == PersonForm.class) {
            dataBinder.setValidator(registrationFormValidator);
        }
        if (target != null && target.getClass() == LoginPerson.class) {
            dataBinder.setValidator(loginPersonValidator);
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
        modelAndView.addObject("form", new PersonForm());
        return modelAndView;
    }

    @PostMapping("/register")
    public ModelAndView register(@ModelAttribute("form")
                                 @Validated PersonForm personForm, BindingResult result, RedirectAttributes ra) {
        final ModelAndView mav = new ModelAndView();
        if (result.hasErrors()) {
            mav.setViewName("registration");
        } else {
            final Person person = ConvertUtil.convert(personForm);
            log.info(person);
            final Person insertedPerson = personService.save(person);


            RegistrationToken registrationToken = new RegistrationToken(insertedPerson);
            registrationTokenRepo.save(registrationToken);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(ourEmailAddress);
            message.setTo(insertedPerson.getEmail());
            message.setSubject("Confirmation of registration");
            message.setText("To confirm your account, please click here : "
                    + "http://localhost:8080/web/confirm-account?token=" + registrationToken.getToken());
            emailService.send(message);

            mav.addObject("email", insertedPerson.getEmail());
            mav.setViewName("successfulRegistration");


//
//            ra.addFlashAttribute("message", String.format("%s! successfully registered", userForm.getFullName()));
//            mav.setViewName("redirect:/web/login");
        }
        return mav;
    }

    @RequestMapping(value = "/confirm-account", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView confirmPersonAccount(ModelAndView modelAndView, @RequestParam("token") String confirmationToken) {
        RegistrationToken token = registrationTokenRepo.findByToken(confirmationToken);

        if (token != null) {
            Person person = personService.getPersonByEmail(token.getPerson().getEmail());
            person.setEnabled(true);
            personService.save(person);
            modelAndView.setViewName("accountVerified");
        } else {
            modelAndView.addObject("message", "The link is invalid or broken!");
            modelAndView.setViewName("error");
        }

        return modelAndView;
    }

    /**
     * http://localhost:8080/web/login
     */
    @GetMapping("/login")
    public ModelAndView loginPage(HttpSession session) {
        session.invalidate();
        final ModelAndView mav = new ModelAndView("index");
        mav.addObject("loginPerson", new LoginPerson());
        return mav;
    }

    @PostMapping("/login")
    public ModelAndView postLogin(@ModelAttribute("loginPerson") @Validated LoginPerson loginPerson,
                                  BindingResult result, HttpSession session) {
        ModelAndView mav = new ModelAndView();
        if (result.hasErrors()) {
            mav.setViewName("index");
        } else {
            final Person person = personService.getPersonByEmail(loginPerson.getEmail());
            session.setAttribute("person", person);
            mav.setViewName("redirect:/web/main-page-auth");
        }

        return mav;
    }

    @GetMapping("/main-page-auth")
    public ModelAndView mainPage(HttpSession session) {
        final ModelAndView mav = new ModelAndView("main-page-authorized");
        final List<String> currencyNames = Arrays.stream(CurrencyNames.values())
                .map(Enum::name).collect(Collectors.toList());
        mav.addObject("values", currencyNames);
        final Person person = (Person) session.getAttribute("person");
        mav.addObject("fullName", person.getFullName());
        return mav;
    }

    @GetMapping("/main-page-guest")
    public ModelAndView mainPageGuest() {

        final ModelAndView mav = new ModelAndView("main-page");
        List<String> collected = Arrays.stream(CurrencyNames.values()).map(Enum::name).collect(Collectors.toList());
        mav.addObject("values",collected);
        return mav;
    }

    @GetMapping("/rates")
    public ModelAndView rates(HttpSession session, @RequestParam("fromDate") String fromDate, @RequestParam("toDate") String toDate,@RequestParam("baseFrom") String baseFrom, @RequestParam("baseTo") String baseTo){
        final ModelAndView mav = new ModelAndView("rates");
        Person person = (Person) session.getAttribute("person");
        mav.addObject("fullName", person.getFullName());
        mav.addObject("fromDate",fromDate);
        mav.addObject("toDate",toDate);
        List<RateSaver> rateSavers = currencyService.convertWithHistory(fromDate, "10 July", baseFrom, baseTo);
        mav.addObject("rates",rateSavers);
        mav.addObject("baseFrom",baseFrom);
        mav.addObject("baseTo",baseTo);
        return mav;
    }

    @GetMapping("/logout")
    public RedirectView logout(HttpSession session) {
        session.invalidate();
        return new RedirectView("/web/login");
    }

    @ExceptionHandler({PersonNotFoundEx.class})
    public ModelAndView personNotFoundHandle() {
        log.info("caught: PersonNotFoundEx");
        final ModelAndView mav = new ModelAndView("index");
        mav.addObject("exMessage", "email or password is incorrect");
        mav.addObject("loginPerson", new LoginPerson());
        return mav;
    }
}
