package uz.mu.autotest.converter.attempt;


import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import uz.mu.autotest.converter.testsuite.TestResultEntityToTestResultDtoConverter;
import uz.mu.autotest.dto.attempt.AttemptDto;
import uz.mu.autotest.dto.testsuite.TestResultDto;
import uz.mu.autotest.model.Attempt;
import uz.mu.autotest.model.TestResults;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AttemptToAttemptDtoConverter implements Converter<Attempt, AttemptDto> {

    private final TestResultEntityToTestResultDtoConverter testResultEntityToTestResultDtoConverter;

    @Override
    public AttemptDto convert(Attempt source) {
        List<TestResults> testResults = source.getTestResults();
        List<TestResultDto> testResultsDto = testResults.stream()
                .map(testResultEntityToTestResultDtoConverter::convert)
                .toList();
        return new AttemptDto(source.getId(), source.getRunNumber(), source.getDetailsUrl(), source.getResult().toString(), source.getStudentTakenLab().getId(), testResultsDto);
    }
}
