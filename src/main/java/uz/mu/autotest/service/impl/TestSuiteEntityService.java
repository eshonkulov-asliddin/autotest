package uz.mu.autotest.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.mu.autotest.extractor.util.TestSuite;
import uz.mu.autotest.model.TestSuiteEntity;
import uz.mu.autotest.repository.TestSuiteEntityRepository;

@Service
@AllArgsConstructor
public class TestSuiteEntityService {

    private final TestSuiteEntityRepository testSuiteEntityRepository;
    private final ConversionService conversionService;

    @Transactional
    public void add(TestSuite testSuite) {
        TestSuiteEntity testSuiteEntity = conversionService.convert(testSuite, TestSuiteEntity.class);
        testSuiteEntityRepository.save(testSuiteEntity);
    }
}
