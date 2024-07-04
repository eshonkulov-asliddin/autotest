package uz.mu.autotest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uz.mu.autotest.dto.group.GroupDto;
import uz.mu.autotest.dto.lab.AddLabRequest;
import uz.mu.autotest.dto.lab.LabDto;
import uz.mu.autotest.model.Course;
import uz.mu.autotest.model.Lab;
import uz.mu.autotest.service.CourseService;
import uz.mu.autotest.service.GroupService;
import uz.mu.autotest.service.LabService;
import uz.mu.autotest.service.TeacherService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static uz.mu.autotest.controller.util.ApiConst.TEACHERS_PREFIX;
import static uz.mu.autotest.controller.util.KeyNames.COURSES;
import static uz.mu.autotest.controller.util.KeyNames.GROUPS;
import static uz.mu.autotest.controller.util.KeyNames.TEACHER_COURSES;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = TEACHERS_PREFIX)
@Slf4j
public class TeacherController {

    @Value("${app.teacher.dashboardHtmlPage}")
    private String dashboardHtmlPage;

    private final TeacherService teacherService;
    private final GroupService groupService;
    private final CourseService courseService;
    private final LabService labService;

    @GetMapping("/dashboard")
    public String dashboard(@RequestParam(required=false) Long courseId, Principal principal, Model model) {
        String username = principal.getName();
        log.info("current teacher username: {}", username);
        List<Course> allCourses = courseService.getCoursesByTeacherUsername(username);
        List<Course> courses = new ArrayList<>();
        if (courseId != null) {
            courses = courseService.getCourseByTeacherUsernameAndCourseId(username, courseId);
        }
        List<GroupDto> groups = courseService.getGroups(!courses.isEmpty() ? courses : allCourses);
        model.addAttribute(TEACHER_COURSES, allCourses);
        model.addAttribute(COURSES, !courses.isEmpty() ? courses : allCourses);
        model.addAttribute(GROUPS, groups);
        return dashboardHtmlPage;
    }

    @GetMapping("/courses")
    public String getCourses(@RequestParam(required=false) Long courseId, Principal principal, Model model) {
        String username = principal.getName();
        log.info("current teacher username: {}", username);
        List<Course> allCourses = courseService.getCoursesByTeacherUsername(username);
        List<Course> courses = new ArrayList<>();
        if (courseId != null) {
            courses = courseService.getCourseByTeacherUsernameAndCourseId(username, courseId);
        }
        List<GroupDto> groups = courseService.getGroups(!courses.isEmpty() ? courses : allCourses);
        model.addAttribute(TEACHER_COURSES, allCourses);
        model.addAttribute(COURSES, !courses.isEmpty() ? courses : allCourses);
        model.addAttribute(GROUPS, groups);
        return dashboardHtmlPage;
    }

    @PostMapping("/courses/{courseId}/labs")
    public String addLab(@PathVariable("courseId") Long courseId, @RequestBody AddLabRequest request, Principal principal, Model model) {
        String username = principal.getName();
        labService.addLab(username, courseId, request);
        return dashboardHtmlPage;
    }

    @GetMapping("/courses/{courseId}/labs")
    public String seeLabs(@PathVariable("courseId") Long courseId, Principal principal, Model model) {
        String username = principal.getName();
        List<LabDto> labsByCourseId = labService.getLabsByCourseId(username, courseId);
        model.addAttribute("labs", labsByCourseId);
        return dashboardHtmlPage;
    }
}
