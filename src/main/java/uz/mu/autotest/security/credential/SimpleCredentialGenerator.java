package uz.mu.autotest.security.credential;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
@RequiredArgsConstructor
@Slf4j
public class SimpleCredentialGenerator implements CredentialGenerator {

    public static final String DELIMITER = ".";
    private final EntityManagerFactory entityManagerFactory;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String generateUsername(String firstName, String lastName) {
        String baseUsername = firstName + DELIMITER + lastName;
        HashSet<String> usernameSet = new HashSet<>();
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            String query = "SELECT u.username FROM User u WHERE u.username LIKE :username";
            usernameSet = new HashSet<>(entityManager.createQuery(query, String.class)
                    .setParameter("username", baseUsername + "%")
                    .getResultList());
        } catch (PersistenceException e) {
            log.error("Error while generating username", e);
            e.printStackTrace();
        }
        int count = 0;
        String generatedUsername = baseUsername;
        while (usernameSet.contains(generatedUsername)) {
            count++;
            generatedUsername = baseUsername + count;
        }

        return generatedUsername;
    }

    @Override
    public String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

}
