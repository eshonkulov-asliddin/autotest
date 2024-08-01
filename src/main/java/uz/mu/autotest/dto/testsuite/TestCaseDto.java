package uz.mu.autotest.dto.testsuite;

public record TestCaseDto(
        Long id,
        String name,
        String classname,
        double time,
        FailureDto failure) { }
