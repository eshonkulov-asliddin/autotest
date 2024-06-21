package uz.mu.autotest.converter.student;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import uz.mu.autotest.converter.group.GroupToGroupDtoConverter;
import uz.mu.autotest.dto.student.StudentDto;
import uz.mu.autotest.model.Student;

@Component
@RequiredArgsConstructor
public class StudentToStudentDtoConverter implements Converter<Student, StudentDto> {

    private final GroupToGroupDtoConverter groupToGroupDtoConverter;

    @Override
    public StudentDto convert(Student source) {
        return new StudentDto(
                source.getId(),
                source.getFirstName(),
                source.getLastName(),
                source.getUsername(),
                groupToGroupDtoConverter.convertGroupsToGroupDtos(source.getGroups())
        );
    }
}
