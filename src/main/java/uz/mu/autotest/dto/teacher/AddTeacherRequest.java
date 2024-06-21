package uz.mu.autotest.dto.teacher;

import jakarta.validation.constraints.NotEmpty;

public record AddTeacherRequest(@NotEmpty String firstName, @NotEmpty String lastName) { }
