package uz.mu.autotest.dto;

import jakarta.validation.constraints.NotNull;

public record GenerateRepositoryRequest(@NotNull Long labId) {
}
