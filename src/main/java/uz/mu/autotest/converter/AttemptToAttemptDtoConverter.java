package uz.mu.autotest.converter;


import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import uz.mu.autotest.converter.testsuite.TestResultEntityToTestResultDtoConverter;
import uz.mu.autotest.dto.AttemptDto;
import uz.mu.autotest.dto.testsuite.TestResultDto;
import uz.mu.autotest.model.Attempt;
import uz.mu.autotest.model.TestResults;

@Component
@RequiredArgsConstructor
public class AttemptToAttemptDtoConverter implements Converter<Attempt, AttemptDto> {

    private final TestResultEntityToTestResultDtoConverter testResultEntityToTestResultDtoConverter;

    @Override
    public AttemptDto convert(Attempt source) {
        TestResults testResults = source.getTestResults();
        TestResultDto testResultDto = testResultEntityToTestResultDtoConverter.convert(testResults);
        return new AttemptDto(source.getId(), source.getRunNumber(), source.getDetailsUrl(), source.getResult().toString(), source.getStudentTakenLab().getId(), testResultDto);
    }
}
