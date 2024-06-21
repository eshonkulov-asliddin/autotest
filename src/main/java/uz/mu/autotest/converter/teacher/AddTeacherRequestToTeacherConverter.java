package uz.mu.autotest.converter.teacher;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import uz.mu.autotest.dto.teacher.AddTeacherRequest;
import uz.mu.autotest.model.Teacher;

@Component
@RequiredArgsConstructor
public class AddTeacherRequestToTeacherConverter implements Converter<AddTeacherRequest, Teacher> {
    @Override
    public Teacher convert(AddTeacherRequest source) {
        Teacher teacher = new Teacher();
        teacher.setFirstName(source.firstName());
        teacher.setLastName(source.lastName());
        return teacher;
    }
}
