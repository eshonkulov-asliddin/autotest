package uz.mu.autotest.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import uz.mu.autotest.model.Role;

import java.io.IOException;
import java.util.function.Predicate;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final HttpSession session;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        session.setAttribute("username", securityUser.getUsername());

        log.info("Authorities: {}", authentication.getAuthorities());

        Predicate<GrantedAuthority> isAdmin = grantedAuthority ->
                grantedAuthority.getAuthority().equals(Role.SUPER_ADMIN.getAuthority()) ||
                grantedAuthority.getAuthority().equals(Role.ADMIN.getAuthority());

        Predicate<GrantedAuthority> isTeacher = grantedAuthority ->
                grantedAuthority.getAuthority().equals(Role.TEACHER.getAuthority());

        Predicate<GrantedAuthority> isStudent = grantedAuthority ->
                grantedAuthority.getAuthority().equals(Role.STUDENT.getAuthority());

        if (authentication.getAuthorities().stream().anyMatch(isAdmin)) {
            response.sendRedirect("/admins/dashboard");
        } else if (authentication.getAuthorities().stream().anyMatch(isTeacher)) {
            response.sendRedirect("/teachers/dashboard");
        } else if (authentication.getAuthorities().stream().anyMatch(isStudent)) {
            response.sendRedirect("/oauth2/authorization/github");
        } else {
            throw new IllegalStateException("Proper roles doesn't exists");
        }

    }
}
