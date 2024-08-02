package uz.mu.autotest.dto;

import uz.mu.autotest.dto.testsuite.TestResultDto;

public record AttemptDto (
        Long id,
        Long runNumber,
        String detailsUrl,
        String result,
        Long userTakenLabId,
        TestResultDto testResultDto) {
}
