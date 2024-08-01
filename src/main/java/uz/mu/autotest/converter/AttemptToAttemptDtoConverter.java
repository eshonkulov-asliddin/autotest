package uz.mu.autotest.converter;


import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import uz.mu.autotest.converter.testsuite.TestSuiteEntityToTestSuiteDtoConverter;
import uz.mu.autotest.dto.AttemptDto;
import uz.mu.autotest.dto.testsuite.TestSuiteDto;
import uz.mu.autotest.model.Attempt;
import uz.mu.autotest.model.TestSuiteEntity;

@Component
@RequiredArgsConstructor
public class AttemptToAttemptDtoConverter implements Converter<Attempt, AttemptDto> {

    private final TestSuiteEntityToTestSuiteDtoConverter testSuiteEntityToTestSuiteDtoConverter;

    @Override
    public AttemptDto convert(Attempt source) {
        TestSuiteEntity testSuite = source.getTestSuite();
        TestSuiteDto testSuiteDto = testSuiteEntityToTestSuiteDtoConverter.convert(testSuite);
        return new AttemptDto(source.getId(), source.getRunNumber(), source.getDetailsUrl(), source.getResult().toString(), source.getStudentTakenLab().getId(), testSuiteDto);
    }
}
