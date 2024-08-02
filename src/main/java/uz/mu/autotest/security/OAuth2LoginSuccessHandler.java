package uz.mu.autotest.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import uz.mu.autotest.model.CustomOAuth2User;
import uz.mu.autotest.model.Role;
import uz.mu.autotest.model.Student;
import uz.mu.autotest.repository.StudentRepository;
import uz.mu.autotest.service.impl.AuthService;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final StudentRepository studentRepository;
    private final AuthService authService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String loginName = oAuth2User.getLogin();
        String oAuth2Name = oAuth2User.getName();

        // Assuming you have a way to get the current user
        String currentlyAuthorizedUserUsername = authService.getCurrentlyAuthorizedUserUsername();
        Optional<Student> currentUser =  studentRepository.findByUsername(currentlyAuthorizedUserUsername);

        if (currentUser.isPresent() && currentUser.get().getRoles().contains(Role.STUDENT)) {
            Student student = currentUser.get();
            boolean updated = false;
            if (student.getLogin() == null || student.getLogin().isEmpty()) {
                student.setLogin(loginName);
                updated = true;
            }
// TODO think about validation
//            if (student.getLoginName() == null || student.getLoginName().isEmpty()) {
//                student.setLoginName(oAuth2Name);
//                updated = true;
//            }
            if (updated) {
                studentRepository.save(student);
                log.info("Updated student with OAuth2 login: {}", loginName);
            } else {
                log.info("Student already has OAuth2 login and name set.");
            }
        }else {

            log.info("there is no authenticated student");
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }

}

