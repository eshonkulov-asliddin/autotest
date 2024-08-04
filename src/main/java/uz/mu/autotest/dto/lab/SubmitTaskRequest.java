package uz.mu.autotest.dto.lab;


import jakarta.validation.constraints.NotNull;

public record SubmitTaskRequest (@NotNull Long labId) { }
