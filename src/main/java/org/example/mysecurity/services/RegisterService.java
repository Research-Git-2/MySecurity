package org.example.mysecurity.services;

import jakarta.transaction.Transactional;
import org.example.mysecurity.enums.Role;
import org.example.mysecurity.models.Img;
import org.example.mysecurity.models.Person;
import org.example.mysecurity.repository.ImgRepository;
import org.example.mysecurity.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImgRepository imgRepository;

    @Autowired
    public RegisterService(PersonRepository personRepository, PasswordEncoder passwordEncoder, ImgRepository imgRepository) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
        this.imgRepository = imgRepository;
    }

    @Transactional
    public void register(Person person, String fileName) {
        String password = passwordEncoder.encode(person.getPassword());
        person.setPassword(password);
        person.setRole(Role.ROLE_USER);

        if (fileName != null && !fileName.isEmpty()) {
            Img img = new Img();
            img.setPath(fileName);
            img.setPerson_img(person);
            imgRepository.save(img);
        }
        personRepository.save(person);
    }
}
