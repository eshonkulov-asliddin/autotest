package uz.mu.autotest.converter.testsuite;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import uz.mu.autotest.dto.testsuite.FailureDto;
import uz.mu.autotest.model.FailureEntity;

@Component
public class FailureEntityToFailureDtoConverter implements Converter<FailureEntity, FailureDto> {
    @Override
    public FailureDto convert(FailureEntity source) {
        return new FailureDto(source.getMessage(), source.getContent());
    }
}
