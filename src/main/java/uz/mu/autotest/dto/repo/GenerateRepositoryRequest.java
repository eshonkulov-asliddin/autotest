package uz.mu.autotest.dto.repo;

import jakarta.validation.constraints.NotNull;

public record GenerateRepositoryRequest(@NotNull Long labId) {
}
