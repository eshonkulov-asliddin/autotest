package uz.mu.autotest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class StudentController {

    @GetMapping("/dashboard")
    public String dashboard() {
        return "student/dashboard.html";
    }


}
