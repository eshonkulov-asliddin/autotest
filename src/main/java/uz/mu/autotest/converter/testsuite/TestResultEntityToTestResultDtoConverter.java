package uz.mu.autotest.converter.testsuite;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import uz.mu.autotest.dto.testsuite.TestCaseDto;
import uz.mu.autotest.dto.testsuite.TestResultDto;
import uz.mu.autotest.model.TestResults;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TestResultEntityToTestResultDtoConverter implements Converter<TestResults, TestResultDto> {

    private final TestCaseEntityToTestCaseDtoConverter testCaseEntityToTestCaseDtoConverter;

    @Override
    public TestResultDto convert(TestResults source) {
        List<TestCaseDto> testCaseDtos = source.getTestCaseEntities()
                .stream()
                .map(testCaseEntityToTestCaseDtoConverter::convert)
                .toList();
        return new TestResultDto(source.getId(), source.getName(), source.getErrors(), source.getFailures(), source.getSkipped(), source.getTests(), source.getTime(), testCaseDtos);
    }
}
