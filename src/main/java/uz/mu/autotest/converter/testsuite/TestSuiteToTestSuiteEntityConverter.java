package uz.mu.autotest.converter.testsuite;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import uz.mu.autotest.extractor.util.TestSuite;
import uz.mu.autotest.model.TestSuiteEntity;

@Component
@RequiredArgsConstructor
public class TestSuiteToTestSuiteEntityConverter implements Converter<TestSuite, TestSuiteEntity> {
    private final TestCaseToTestCaseEntityConverter testCaseToTestCaseEntityConverter;
    @Override
    public TestSuiteEntity convert(TestSuite source) {
        TestSuiteEntity testSuiteEntity = new TestSuiteEntity();
        testSuiteEntity.setName(source.getName());
        testSuiteEntity.setErrors(source.getErrors());
        testSuiteEntity.setFailures(source.getFailures());
        testSuiteEntity.setSkipped(source.getSkipped());
        testSuiteEntity.setTests(source.getTests());
        testSuiteEntity.setTime(source.getTime());
        testSuiteEntity.setTimestamp(source.getTimestamp());
        testSuiteEntity.setHostname(source.getHostname());
        testSuiteEntity.setTestCases(source.getTestCases().stream()
                .map(testCaseToTestCaseEntityConverter::convert).toList()
        );
        return testSuiteEntity;
    }
}
