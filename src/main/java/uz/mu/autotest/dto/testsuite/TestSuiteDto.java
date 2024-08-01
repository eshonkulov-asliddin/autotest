package uz.mu.autotest.dto.testsuite;

import java.util.List;

public record TestSuiteDto (
        Long id,
        String name,
        int errors,
        int failures,
        int skipped,
        int tests,
        double time,
        String timestamp,
        String hostname,
        List<TestCaseDto> testCases) { }
