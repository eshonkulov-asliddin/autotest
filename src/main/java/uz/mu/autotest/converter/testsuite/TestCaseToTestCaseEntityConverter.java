package uz.mu.autotest.converter.testsuite;

import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import uz.mu.autotest.extractor.attempt.model.TestCase;
import uz.mu.autotest.model.TestCaseEntity;

@Component
@AllArgsConstructor
public class TestCaseToTestCaseEntityConverter implements Converter<TestCase, TestCaseEntity> {

    private final FailureToFailureEntityConverter failureToFailureEntityConverter;

    @Override
    public TestCaseEntity convert(TestCase source) {
        TestCaseEntity testCaseEntity = new TestCaseEntity();
        testCaseEntity.setName(source.getName());
        testCaseEntity.setClassname(source.getClassname());
        testCaseEntity.setTime(source.getTime());
        testCaseEntity.setFailure(failureToFailureEntityConverter.convert(source.getFailure()));
        return testCaseEntity;
    }
}
