package uz.mu.autotest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.mu.autotest.model.User;
import uz.mu.autotest.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void createUserAfterSuccessfullOAuth2Login(String login, String displayName) {
        User user = new User();
        user.setUsername(login);
        user.setFirstName(displayName);
        user.setActive(true);
        userRepository.save(user);
    }

    public Optional<User> getByUsername(String username) {
        return userRepository.findByUsernameWithRoles(username);
    }
}

