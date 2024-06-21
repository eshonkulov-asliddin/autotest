package uz.mu.autotest.dto.student;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record AddStudentRequest(@NotEmpty String firstName, @NotEmpty String lastName, @NotNull Long groupIds) {
}
