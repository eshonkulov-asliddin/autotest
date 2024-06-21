package uz.mu.autotest.dto.admin;

import jakarta.validation.constraints.NotEmpty;

public record AddAdminRequest(@NotEmpty String firstName, @NotEmpty String lastName) {
}
