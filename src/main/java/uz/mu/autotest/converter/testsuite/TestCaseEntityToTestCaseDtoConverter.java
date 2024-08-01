package uz.mu.autotest.converter.testsuite;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import uz.mu.autotest.dto.testsuite.FailureDto;
import uz.mu.autotest.dto.testsuite.TestCaseDto;
import uz.mu.autotest.model.FailureEntity;
import uz.mu.autotest.model.TestCaseEntity;

@Component
@RequiredArgsConstructor
public class TestCaseEntityToTestCaseDtoConverter implements Converter<TestCaseEntity, TestCaseDto> {

    private final FailureEntityToFailureDtoConverter failureEntityToFailureDtoConverter;

    @Override
    public TestCaseDto convert(TestCaseEntity source) {
        FailureEntity failure = source.getFailure();
        FailureDto failureDto = null;
        if (failure != null) {
             failureDto = failureEntityToFailureDtoConverter.convert(failure);
        }
        return new TestCaseDto(source.getId(), source.getName(), source.getClassname(), source.getTime(), failureDto);
    }
}
