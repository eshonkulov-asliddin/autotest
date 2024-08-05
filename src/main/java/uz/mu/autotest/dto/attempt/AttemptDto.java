package uz.mu.autotest.dto.attempt;

import uz.mu.autotest.dto.testsuite.TestResultDto;

import java.util.List;

public record AttemptDto (
        Long id,
        Long runNumber,
        String detailsUrl,
        String result,
        Long userTakenLabId,
        List<TestResultDto> testResultDto) {
}
