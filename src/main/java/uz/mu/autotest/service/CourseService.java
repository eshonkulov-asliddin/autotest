package uz.mu.autotest.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import uz.mu.autotest.model.Course;
import uz.mu.autotest.repository.CourseRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class CourseService {

    private final CourseRepository courseRepository;

    public List<Course> getAllCourses() {
        List<Course> allCourses = courseRepository.findAll();
        log.info("Retrieved all courses: {}", allCourses);
        return allCourses;
    }

    public Optional<Course> getByName(String courseName) {
        Optional<Course> course = courseRepository.findByCourseName(courseName);
        log.info("Retrieved course by name: {}", course);
        return course;
    }
}
