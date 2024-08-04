package uz.mu.autotest.dto.group;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record GroupLabAssignmentRequest(@NotNull Long groupId, @NotNull Long labId, @NotNull Long courseId, @NotNull LocalDateTime deadline) {
}
