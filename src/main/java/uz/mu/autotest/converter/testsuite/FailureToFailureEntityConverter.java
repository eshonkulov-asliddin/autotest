package uz.mu.autotest.converter.testsuite;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import uz.mu.autotest.extractor.python.Failure;
import uz.mu.autotest.model.FailureEntity;

@Component
@RequiredArgsConstructor
public class FailureToFailureEntityConverter implements Converter<Failure, FailureEntity> {

    @Override
    public FailureEntity convert(Failure source) {
        FailureEntity failureEntity = new FailureEntity();
        failureEntity.setMessage(source.getMessage());
        failureEntity.setDetails(source.getContent());
        return failureEntity;
    }
}
