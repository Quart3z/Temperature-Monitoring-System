package com.example.backend.spring.controller;

import com.example.backend.spring.entity.Token;
import com.example.backend.spring.entity.User;
import com.example.backend.spring.implement.ImplementUserDetailsService;
import com.example.backend.spring.repository.TokenRepository;
import com.example.backend.spring.security.MailVerification;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${spring.mail.username}")
    private String sender;

    /**
     * Return authentication view to frontend
     * User is redirected to this view if not authenticated
     */
    @GetMapping("/authentication")
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("authentication");

        return modelAndView;
    }

    /**
     * Credentials passing for user registration
     * Once success, send verification email to user registered email
     *
     * @param body request body from frontend
     *             username: user registered username
     *             password: user registered password
     *             email: user registered email
     */
    @PostMapping(value = "/register")
    public void registration(@RequestBody String body) throws MessagingException, UnsupportedEncodingException, SQLIntegrityConstraintViolationException {

        String emailRExpression = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

        JsonObject deserializedBody = JsonParser.parseString(body).getAsJsonObject();

        boolean result = Pattern.compile(emailRExpression).matcher((deserializedBody.get("email").getAsString())).matches();

        if (result) {

            User user = new User(
                    deserializedBody.get("password").getAsString(),
                    deserializedBody.get("username").getAsString(),
                    deserializedBody.get("email").getAsString(),
                    0
            );
            userDetailsService.save(user);
            Token token = new Token(user);
            tokenRepository.save(token);

            MailVerification emailVerification = new MailVerification();
            emailVerification.verificationRequest(sender, user.getUsername(), user.getEmail(), token.getToken(), mailSender);
        }

    }

    /**
     * Verify the user once the verification link is clicked
     *  Redirect user to login form after the verification is done
     *
     * @param token individual token generated to determine authenticity of the user
     */
    @GetMapping("/verify")
    public ModelAndView verification(@Param("token") String token) {
        ModelAndView modelAndView = new ModelAndView();
        userDetailsService.verification(token);

        modelAndView.setViewName("authentication");
        return modelAndView;
    }

}
