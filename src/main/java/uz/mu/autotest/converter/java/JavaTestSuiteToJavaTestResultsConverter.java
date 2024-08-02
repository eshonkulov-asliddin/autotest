package uz.mu.autotest.converter.java;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import uz.mu.autotest.extractor.java.JavaTestSuite;
import uz.mu.autotest.model.JavaTestResults;
import uz.mu.autotest.model.TestCaseEntity;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JavaTestSuiteToJavaTestResultsConverter implements Converter<JavaTestSuite, JavaTestResults> {

    private final JavaTestCaseToTestCaseEntityConverter javaTestCaseToTestCaseEntityConverter;

    @Override
    public JavaTestResults convert(JavaTestSuite source) {
        JavaTestResults javaTestResults = new JavaTestResults();
        javaTestResults.setName(source.getName());
        javaTestResults.setErrors(source.getErrors());
        javaTestResults.setFailures(source.getFailures());
        javaTestResults.setSkipped(source.getSkipped());
        javaTestResults.setTests(source.getTests());
        javaTestResults.setTime(Double.parseDouble(source.getTime()));

        List<TestCaseEntity> testCaseEntities = source.getTestCases().stream()
                .map(javaTestCaseToTestCaseEntityConverter::convert)
                .toList();

        javaTestResults.setTestCaseEntities(testCaseEntities);

        return javaTestResults;
    }
}
