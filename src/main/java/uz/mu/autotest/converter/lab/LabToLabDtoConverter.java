package uz.mu.autotest.converter.lab;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import uz.mu.autotest.dto.course.CourseDto;
import uz.mu.autotest.dto.lab.LabDto;
import uz.mu.autotest.model.Lab;

@Component
@RequiredArgsConstructor
public class LabToLabDtoConverter implements Converter<Lab, LabDto> {

    private final ConversionService conversionService;

    @Override
    public LabDto convert(Lab source) {
        return new LabDto(source.getId(), source.getLabName(), source.getGithubUrl(), source.getStatus(), conversionService.convert(source.getCourse(), CourseDto.class));
    }
}
