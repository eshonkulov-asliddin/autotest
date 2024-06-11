package uz.mu.autotest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.mu.autotest.model.Course;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findByCourseName(String courseName);
}
