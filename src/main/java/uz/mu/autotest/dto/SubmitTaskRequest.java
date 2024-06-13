package uz.mu.autotest.dto;


import jakarta.validation.constraints.NotNull;

public record SubmitTaskRequest (@NotNull Long labId) { }
