package uz.mu.autotest.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.mu.autotest.model.Course;
import uz.mu.autotest.service.CourseService;
import uz.mu.autotest.utils.ApiConst;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping(path = ApiConst.COURSES_ENDPOINT)
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> allCourses = courseService.getAllCourses();
        return ResponseEntity.ok(allCourses);
    }

    @GetMapping("/{courseName}")
    public ResponseEntity<Optional<Course>> getByCourseName(@PathVariable(name = "courseName") String courseName) {
        Optional<Course> byName = courseService.getByName(courseName);
        return ResponseEntity.ok(byName);
    }
}
