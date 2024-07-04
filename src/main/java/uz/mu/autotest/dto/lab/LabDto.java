package uz.mu.autotest.dto.lab;

import uz.mu.autotest.dto.course.CourseDto;
import uz.mu.autotest.model.LabStatus;

public record LabDto(Long id, String name, String githubUrl, LabStatus status, CourseDto course) {
}
