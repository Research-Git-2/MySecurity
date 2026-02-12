package org.example.mysecurity.controllers;

import org.example.mysecurity.models.Person;
import org.example.mysecurity.services.PersonDetailsService;
import org.example.mysecurity.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {
    private final PersonService personService;
    private final PersonDetailsService personDetailsService;

    @Autowired
    public HomeController(PersonService personService, PersonDetailsService personDetailsService) {
        this.personService = personService;
        this.personDetailsService = personDetailsService;
    }

    @GetMapping("")
    public String home(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Person currentUser = personDetailsService.findByEmail(email);

        if (currentUser == null) {
            return "redirect:/auth/login";
        }

        model.addAttribute("person", currentUser);

        if (currentUser.getImages() != null && !currentUser.getImages().isEmpty()) {
            String imagePath = currentUser.getImages().get(0).getPath();
            String imageUrl = "/uploads/" + imagePath;
            model.addAttribute("profileImageUrl", imageUrl);
        } else {
            model.addAttribute("profileImageUrl", "/images/default-avatar.gif");
        }

        return "home";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("people", personService.findAll());
        return "admin";
    }

    @GetMapping("/admin/{id}/edit")
    public String editPerson(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", personService.findById(id));
        return "profile/edit";
    }

    @PostMapping("/admin/{id}")
    public String postPerson(@PathVariable("id") int id, Person person,
                             @RequestParam("profileImage") MultipartFile file) {
        personService.update(id, person, file);
        return "redirect:/home/admin";
    }

    @PostMapping("/admin/{id}/delete")
    public String deletePerson(@PathVariable("id") int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName();

        Person currentUser = personService.findByEmail(currentEmail)
                .orElse(null);

        boolean deletingSelf = currentUser != null && currentUser.getId_person() == id;

        personService.delete(id);

        if (deletingSelf) {
            SecurityContextHolder.clearContext();
            return "redirect:/auth/register";
        }

        return "redirect:/home/admin";
    }
}
