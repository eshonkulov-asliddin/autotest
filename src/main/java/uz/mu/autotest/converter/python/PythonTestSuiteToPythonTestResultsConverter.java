package uz.mu.autotest.converter.python;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import uz.mu.autotest.extractor.python.TestSuite;
import uz.mu.autotest.model.PythonTestResults;
import uz.mu.autotest.model.TestCaseEntity;

import java.util.List;



@Component
@RequiredArgsConstructor
public class PythonTestSuiteToPythonTestResultsConverter implements Converter<TestSuite, PythonTestResults> {

    private final PythonTestCaseToTestCaseEntityConverter pythonTestCaseToTestCaseEntityConverter;

    @Override
    public PythonTestResults convert(TestSuite source) {
        PythonTestResults pythonTestResults = new PythonTestResults();
        pythonTestResults.setName(source.getName());
        pythonTestResults.setErrors(source.getErrors());
        pythonTestResults.setFailures(source.getFailures());
        pythonTestResults.setSkipped(source.getSkipped());
        pythonTestResults.setTests(source.getTests());
        pythonTestResults.setTime(source.getTime());

        List<TestCaseEntity> testCaseEntities = source.getTestCases().stream()
                .map(testCase -> pythonTestCaseToTestCaseEntityConverter.convert(testCase))
                .toList();

        pythonTestResults.setTestCaseEntities(testCaseEntities);

        return pythonTestResults;
    }
}