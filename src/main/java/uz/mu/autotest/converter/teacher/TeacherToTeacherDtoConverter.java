package uz.mu.autotest.converter.teacher;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import uz.mu.autotest.converter.group.GroupToGroupDtoConverter;
import uz.mu.autotest.dto.teacher.TeacherDto;
import uz.mu.autotest.model.Teacher;

@Component
@RequiredArgsConstructor
public class TeacherToTeacherDtoConverter implements Converter<Teacher, TeacherDto> {

    private final GroupToGroupDtoConverter groupToGroupDtoConverter;
    @Override
    public TeacherDto convert(Teacher source) {
        return new TeacherDto(
                source.getId(),
                source.getFirstName(),
                source.getLastName(),
                source.getUsername()
        );
    }
}
