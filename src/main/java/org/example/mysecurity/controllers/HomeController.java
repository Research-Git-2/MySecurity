package org.example.mysecurity.controllers;

import org.example.mysecurity.models.Person;
import org.example.mysecurity.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {
    private final PersonService personService;

    @Autowired
    public HomeController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("")
    public String home() {
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
    public String postPerson(@PathVariable("id") int id, Person person) {
        personService.update(id, person);
        return "redirect:/home";
    }

    @PostMapping("/admin/{id}/delete")
    public String deletePerson(@PathVariable("id") int id) {
        personService.delete(id);
        return "redirect:/home";
    }
}
