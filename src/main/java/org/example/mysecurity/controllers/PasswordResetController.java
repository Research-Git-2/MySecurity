package org.example.mysecurity.controllers;

import org.example.mysecurity.models.Person;
import org.example.mysecurity.services.EmailService;
import org.example.mysecurity.services.PasswordResetService;
import org.example.mysecurity.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auth")
public class PasswordResetController {
    private final PersonService personService;
    private final PasswordResetService passwordResetService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PasswordResetController(PersonService personService, PasswordResetService passwordResetService, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.personService = personService;
        this.passwordResetService = passwordResetService;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "auth/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam("email") String email, Model model) {
        Person person = personService.findByEmail(email).orElse(null);

        if (person != null) {
            String token = passwordResetService.createToken(person);
            String link = "http://localhost:8080/auth/reset-password?token=" + token;
            System.out.println(link);

            emailService.send(person.getEmail(), "Сброс пароля", "Перейдите по ссылке для сброса пароля:\n" + link);
        }
        model.addAttribute("message", "Если пользователь существует, ссылка отправлена на почту.");
        return "auth/check-email";
    }

    @GetMapping("/reset-password")
    public String resetPasswordForm(@RequestParam("token") String token, Model model) {
        Person person = passwordResetService.validToken(token);

        if (person == null) {
            model.addAttribute("error", "Ссылка недействительна или истекла");
            return "auth/error";
        }

        model.addAttribute("token", token);

        return "auth/reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("token") String token,
                                @RequestParam("password") String password, Model model) {
        Person person = passwordResetService.validToken(token);

        if (person == null) {
            model.addAttribute("error", "Ссылка недействительна или истекла");
            return "auth/error";
        }

        person.setPassword(passwordEncoder.encode(password));
        personService.save(person);

        passwordResetService.removeToken(token);
        return "redirect:/auth/login";
    }
}
