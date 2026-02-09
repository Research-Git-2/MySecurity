package org.example.mysecurity.repository;

import org.example.mysecurity.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Period;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Period, Integer> {
    Optional<Person> findByEmail(String email);
}
