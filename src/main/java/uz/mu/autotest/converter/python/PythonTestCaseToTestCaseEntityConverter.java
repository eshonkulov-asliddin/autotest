package uz.mu.autotest.converter.python;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import uz.mu.autotest.extractor.python.Failure;
import uz.mu.autotest.extractor.python.TestCase;
import uz.mu.autotest.model.FailureEntity;
import uz.mu.autotest.model.TestCaseEntity;

@Component
public class PythonTestCaseToTestCaseEntityConverter implements Converter<TestCase, TestCaseEntity> {

    @Override
    public TestCaseEntity convert(TestCase source) {
        TestCaseEntity testCaseEntity = new TestCaseEntity();
        testCaseEntity.setClassname(source.getClassname());
        testCaseEntity.setName(source.getName());
        testCaseEntity.setTime(source.getTime());

        Failure failure = source.getFailure();
        if (failure != null) {
            testCaseEntity.setFailureEntity(new FailureEntity(failure.getMessage(), failure.getContent()));
        }

        return testCaseEntity;
    }
}