package uz.mu.autotest.converter.testsuite;

import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import uz.mu.autotest.extractor.util.Failure;
import uz.mu.autotest.model.FailureEntity;

@Component
@AllArgsConstructor
public class FailureToFailureEntityConverter implements Converter<Failure, FailureEntity> {

    @Override
    public FailureEntity convert(Failure source) {
        FailureEntity failureEntity = new FailureEntity();
        failureEntity.setMessage(source.getMessage());
        failureEntity.setContent(source.getContent());
        return failureEntity;
    }
}
