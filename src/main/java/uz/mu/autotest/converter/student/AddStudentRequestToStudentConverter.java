package uz.mu.autotest.converter.student;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import uz.mu.autotest.dto.student.AddStudentRequest;
import uz.mu.autotest.model.Student;

@Component
@RequiredArgsConstructor
public class AddStudentRequestToStudentConverter implements Converter<AddStudentRequest, Student> {
    @Override
    public Student convert(AddStudentRequest source) {
        Student student = new Student();
        student.setFirstName(source.firstName());
        student.setLastName(source.lastName());
        return student;
    }
}
