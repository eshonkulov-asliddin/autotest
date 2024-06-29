package uz.mu.autotest.dto.course;

import jakarta.validation.constraints.NotEmpty;

public record AddCourseRequest(@NotEmpty String name, @NotEmpty String teacherUsername) {
}
