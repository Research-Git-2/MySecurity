package org.example.mysecurity.services;

import org.example.mysecurity.models.Person;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PasswordResetService {
    private final Map<String, TokenData> tokenStorage = new ConcurrentHashMap<>();

    public static class TokenData {
        Person person;
        LocalDateTime expiryAt;

        TokenData(Person person, LocalDateTime expiryAt) {
            this.person = person;
            this.expiryAt = expiryAt;
        }
    }

    public String createToken(Person person) {
        String token = UUID.randomUUID().toString();
        tokenStorage.put(token, new TokenData(person, LocalDateTime.now().plusMinutes(15)));

        return token;
    }

    public Person validToken(String token) {
        TokenData tokenData = tokenStorage.get(token);

        if (tokenData == null || tokenData.expiryAt.isBefore(LocalDateTime.now())) {
            return null;
        }

        return tokenData.person;
    }

    public void removeToken(String token) {
        tokenStorage.remove(token);
    }
}