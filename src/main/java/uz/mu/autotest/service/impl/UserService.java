package uz.mu.autotest.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.mu.autotest.model.Student;
import uz.mu.autotest.model.User;
import uz.mu.autotest.repository.StudentRepository;
import uz.mu.autotest.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;

    public void createUserAfterSuccessfullOAuth2Login(String login, String displayName) {
        User user = new User();
        user.setUsername(login);
        user.setFirstName(displayName);
        user.setActive(true);
        userRepository.save(user);
    }

    public Optional<Student> getByUsername(String username) {
        Optional<Student> byOAuth2Login = studentRepository.findByLogin(username);
        log.info("student byOAuth2login: {}", byOAuth2Login);
        return byOAuth2Login;
    }
}
