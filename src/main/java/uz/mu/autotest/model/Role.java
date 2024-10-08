package uz.mu.autotest.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    SUPER_ADMIN,
    ADMIN,
    TEACHER,
    STUDENT;

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }
}
