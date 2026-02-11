package org.example.mysecurity.controllers;

import org.example.mysecurity.models.Person;
import org.example.mysecurity.repository.ImgRepository;
import org.example.mysecurity.services.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final RegisterService registerService;
    private final ImgRepository imgRepository;

    private static final String UPLOAD_DIR = "uploads/";

    @Autowired
    public AuthController(RegisterService registerService, ImgRepository imgRepository) {
        this.registerService = registerService;
        this.imgRepository = imgRepository;

        try {
            Files.createDirectories(Paths.get(UPLOAD_DIR));
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory", e);
        }
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model, @ModelAttribute("person") Person person) {
        model.addAttribute("person", person);
        return "auth/register";
    }

    @PostMapping("/register")
    public String performRegistration(
            @ModelAttribute("people") Person person,
            @RequestParam("profileImage") MultipartFile file) {

        if (!file.isEmpty()) {
            try {
                String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

                Path filePath = Paths.get(UPLOAD_DIR + fileName);
                Files.write(filePath, file.getBytes());

                registerService.register(person, fileName);

            } catch (IOException e) {
                throw new RuntimeException("Failed to save image", e);
            }
        } else {
            registerService.register(person, null);
        }

        return "redirect:/auth/login";
    }
}
