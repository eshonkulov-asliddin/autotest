package uz.mu.autotest.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import uz.mu.autotest.model.AuthProvider;
import uz.mu.autotest.model.CustomOAuth2User;
import uz.mu.autotest.repository.UserRepository;
import uz.mu.autotest.service.UserService;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String loginName = oAuth2User.getLogin();

        if (userRepository.findByUsername(loginName).isEmpty()) {

            // convert from CustomOAuth2User to User
            userService.createUserAfterSuccessfullOAuth2Login(oAuth2User.getLogin(), oAuth2User.getName(), AuthProvider.GITHUB);
            log.info("Created new user with username: {}", loginName);
        }else{
            log.info("User {} is old user", loginName);
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }

}

