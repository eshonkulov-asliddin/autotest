package uz.mu.autotest.converter.java;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import uz.mu.autotest.extractor.java.Error;
import uz.mu.autotest.extractor.java.Failure;
import uz.mu.autotest.extractor.java.TestCase;
import uz.mu.autotest.model.FailureEntity;
import uz.mu.autotest.model.TestCaseEntity;

@Component
public class JavaTestCaseToTestCaseEntityConverter implements Converter<TestCase, TestCaseEntity> {
    @Override
    public TestCaseEntity convert(TestCase source) {
        TestCaseEntity testCaseEntity = new TestCaseEntity();
        testCaseEntity.setClassname(source.getClassname());
        testCaseEntity.setName(source.getName());
        testCaseEntity.setTime(Double.parseDouble(source.getTime()));

        Error error = source.getError();
        if (error != null) {
            testCaseEntity.setFailureEntity(new FailureEntity(error.getType(), error.getMessage()));
        }

        Failure failure = source.getFailure();
        if (failure != null) {
            testCaseEntity.setFailureEntity(new FailureEntity(failure.getType(), failure.getMessage()));
        }

        return testCaseEntity;
    }
}
