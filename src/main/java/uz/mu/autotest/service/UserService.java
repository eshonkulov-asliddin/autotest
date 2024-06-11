package uz.mu.autotest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.mu.autotest.model.AuthProvider;
import uz.mu.autotest.model.User;
import uz.mu.autotest.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void createUserAfterSuccessfullOAuth2Login(String login, String displayName, AuthProvider authProvider) {
        User user = new User();
        user.setUsername(login);
        user.setName(displayName);
        user.setEnabled(true);
        user.setAuthProvider(authProvider);

        userRepository.save(user);
    }

    public Optional<User> getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}
