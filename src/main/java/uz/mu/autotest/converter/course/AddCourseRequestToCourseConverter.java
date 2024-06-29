package uz.mu.autotest.converter.course;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import uz.mu.autotest.dto.course.AddCourseRequest;
import uz.mu.autotest.model.Course;

@Component
public class AddCourseRequestToCourseConverter implements Converter<AddCourseRequest, Course> {
    @Override
    public Course convert(AddCourseRequest source) {
        Course course = new Course();
        course.setName(source.name());
        return course;
    }
}
