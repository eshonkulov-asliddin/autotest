package uz.mu.autotest.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.mu.autotest.extractor.AbstractTestSuite;
import uz.mu.autotest.model.TestResults;
import uz.mu.autotest.repository.TestResultsRepository;

@Service
@AllArgsConstructor
public class TestSuiteEntityService {

    private final TestResultsRepository testResultsRepository;
    private final ConversionService conversionService;

    @Transactional
    public void add(AbstractTestSuite testSuite) {
        TestResults testResults = conversionService.convert(testSuite, TestResults.class);
        testResultsRepository.save(testResults);
    }
}
