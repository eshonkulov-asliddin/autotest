package uz.mu.autotest.dto.testsuite;

import java.util.List;

public record TestResultDto(
        Long id,
        String name,
        int errors,
        int failures,
        int skipped,
        int tests,
        double time,
        List<TestCaseDto> testCases) { }
