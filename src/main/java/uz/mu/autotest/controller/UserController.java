package uz.mu.autotest.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import uz.mu.autotest.model.Course;
import uz.mu.autotest.service.CourseService;

import java.util.List;

@Controller
@AllArgsConstructor
public class UserController {

    private final CourseService courseService;

    @GetMapping("/login")
    public String login(){
        return "login.html";
    }

    @GetMapping
    public String home(Model model) {
        List<Course> courses = courseService.getAllCourses();
         model.addAttribute("courses", courses);
         return "home.html";
    }
}
