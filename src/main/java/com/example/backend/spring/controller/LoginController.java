package com.example.backend.spring.controller;

import com.example.backend.spring.entity.Token;
import com.example.backend.spring.entity.User;
import com.example.backend.spring.implement.ImplementUserDetailsService;
import com.example.backend.spring.repository.TokenRepository;
import com.example.backend.spring.security.MailVerification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.regex.Pattern;

@Controller
@RestController
@RequestMapping
public class LoginController {

    @Autowired
    private ImplementUserDetailsService userDetailsService;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private JavaMailSender mailSender;

    // Display of login page
    @GetMapping("/authentication")
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("authentication");

        return modelAndView;
    }

    /**
     *  Credentials passing for user registration
     *      - success if email validation success
     * */
    @PostMapping(value = "/register")
    public void registration(@RequestBody User user, Model model) throws MessagingException, UnsupportedEncodingException, SQLIntegrityConstraintViolationException {

        String emailRExpression = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

        boolean result = Pattern.compile(emailRExpression).matcher((user.getEmail())).matches();

        if (result) {
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            userDetailsService.save(user);
            Token token = new Token(user);
            tokenRepository.save(token);

            MailVerification emailVerification = new MailVerification();
            emailVerification.verificationRequest(user.getUsername(), user.getEmail(), token.getToken(), mailSender);
        }

    }

    @GetMapping("/verify")
    public ModelAndView verification(@Param("token") String token) {
        ModelAndView modelAndView = new ModelAndView();

        if (userDetailsService.verification(token)) {

            modelAndView.setViewName("authentication");
            return modelAndView;

        } else {
            modelAndView.setViewName("authentication");
            return modelAndView;
        }

    }

}
