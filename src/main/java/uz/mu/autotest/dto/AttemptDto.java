package uz.mu.autotest.dto;

import uz.mu.autotest.model.TestSuiteEntity;

public record AttemptDto (
        Long id,
        Long runNumber,
        String detailsUrl,
        String result,
        Long userTakenLabId,
        TestSuiteEntity testSuiteEntity ) {
}
