package org.example.mysecurity.services;

import org.example.mysecurity.models.Person;
import org.example.mysecurity.repository.PersonRepository;
import org.example.mysecurity.security.PersonDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PersonDetailsService implements UserDetailsService {
    private final PersonRepository personRepository;

    @Autowired
    public PersonDetailsService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person = personRepository.findByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found with username: " + username)
        );
        return new PersonDetails(person);
    }

    public Person findByEmail(String email) {
        return personRepository.findByEmail(email).orElse(null);
    }
}
