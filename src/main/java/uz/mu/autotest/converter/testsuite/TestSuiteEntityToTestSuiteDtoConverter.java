package uz.mu.autotest.converter.testsuite;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import uz.mu.autotest.dto.testsuite.TestCaseDto;
import uz.mu.autotest.dto.testsuite.TestSuiteDto;
import uz.mu.autotest.model.TestSuiteEntity;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TestSuiteEntityToTestSuiteDtoConverter implements Converter<TestSuiteEntity, TestSuiteDto> {

    private final TestCaseEntityToTestCaseDtoConverter testCaseEntityToTestCaseDtoConverter;

    @Override
    public TestSuiteDto convert(TestSuiteEntity source) {
        List<TestCaseDto> testCaseDtos = source.getTestCases()
                .stream()
                .map(testCaseEntityToTestCaseDtoConverter::convert)
                .toList();
        return new TestSuiteDto(source.getId(), source.getName(), source.getErrors(), source.getFailures(), source.getSkipped(), source.getTests(), source.getTime(), source.getTimestamp(), source.getHostname(), testCaseDtos);
    }
}
