package org.example.mysecurity.services;

import jakarta.transaction.Transactional;
import org.example.mysecurity.models.Img;
import org.example.mysecurity.models.Person;
import org.example.mysecurity.repository.PersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class PersonService {
    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Optional<Person> findByEmail(String email) {
        return personRepository.findByEmail(email);
    }

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public Person save(Person person) {
        return personRepository.save(person);
    }

    public Person findById(int id) {
        Optional<Person> personOptional = personRepository.findById(id);
        return personOptional.orElse(null);
    }

    public Person update(int id, Person updatePerson, MultipartFile file) {

        Person existingPerson = personRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Person not found"));

        existingPerson.setFirstname(updatePerson.getFirstname());
        existingPerson.setLastname(updatePerson.getLastname());
        existingPerson.setEmail(updatePerson.getEmail());
        existingPerson.setRole(updatePerson.getRole());
        existingPerson.setEnabled(updatePerson.isEnabled());

        if (file != null && !file.isEmpty()) {
            try {
                String uploadDir = "uploads/";
                Files.createDirectories(Paths.get(uploadDir));

                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

                Path path = Paths.get(uploadDir, fileName);
                Files.write(path, file.getBytes());

                Img image = new Img();
                image.setPath(fileName);
                image.setPerson_img(existingPerson);

                if (existingPerson.getImages() == null) {
                    existingPerson.setImages(new ArrayList<>());
                }

                existingPerson.getImages().clear();
                existingPerson.getImages().add(image);

            } catch (IOException e) {
                throw new RuntimeException("Error saving file", e);
            }
        }

        return personRepository.save(existingPerson);
    }


    @Transactional
    public void delete(int id) {
        personRepository.deleteById(id);
    }
}
