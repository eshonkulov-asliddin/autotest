package uz.mu.autotest.dto;

public record AttemptDto (
        Long id,
        Long runNumber,
        String detailsUrl,
        String result,
        Long userTakenLabId,
        Long testSuiteEntityId ) {
}
