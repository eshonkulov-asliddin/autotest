package uz.mu.autotest.converter.course;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import uz.mu.autotest.dto.course.CourseDto;
import uz.mu.autotest.model.Course;

@Component
public class CourseToCourseDtoConverter implements Converter<Course, CourseDto> {
    @Override
    public CourseDto convert(Course source) {
        return new CourseDto(source.getId(), source.getName(), source.getTeacher().getUsername());
    }
}
