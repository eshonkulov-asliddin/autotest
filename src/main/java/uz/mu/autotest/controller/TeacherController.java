package uz.mu.autotest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uz.mu.autotest.dto.lab.StudentLabResult;
import uz.mu.autotest.dto.group.GroupDto;
import uz.mu.autotest.dto.group.GroupLabAssignmentRequest;
import uz.mu.autotest.dto.lab.AddLabRequest;
import uz.mu.autotest.dto.lab.LabDto;
import uz.mu.autotest.dto.teacher.AddTeacherRequest;
import uz.mu.autotest.model.Course;
import uz.mu.autotest.model.Group;
import uz.mu.autotest.model.Teacher;
import uz.mu.autotest.service.GroupLabAssignmentService;
import uz.mu.autotest.service.impl.CourseService;
import uz.mu.autotest.service.impl.GroupService;
import uz.mu.autotest.service.impl.LabService;
import uz.mu.autotest.service.impl.StudentLabResultsGenerator;
import uz.mu.autotest.service.impl.TeacherService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static uz.mu.autotest.controller.util.ApiConst.TEACHERS_PREFIX;
import static uz.mu.autotest.controller.util.KeyNames.COURSES;
import static uz.mu.autotest.controller.util.KeyNames.GROUPS;
import static uz.mu.autotest.controller.util.KeyNames.TEACHERS;
import static uz.mu.autotest.controller.util.KeyNames.TEACHER_COURSES;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = TEACHERS_PREFIX)
@Slf4j
public class TeacherController {

    @Value("${app.teacher.dashboardHtmlPage}")
    private String dashboardHtmlPage;
    @Value("${app.teacher.labStatistics}")
    private String labStatistics;
    @Value("${app.admin.teachersHtmlPage}")
    private String teachersHtmlPage;

    private final TeacherService teacherService;
    private final GroupService groupService;
    private final CourseService courseService;
    private final LabService labService;
    private final GroupLabAssignmentService groupLabAssignmentService;
    private final StudentLabResultsGenerator studentLabResultsGenerator;

    @PreAuthorize("hasRole('TEACHER')")
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


    @PreAuthorize("hasRole('TEACHER')")
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

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/courses/{courseId}/labs")
    public String addLab(@PathVariable("courseId") Long courseId, @RequestBody AddLabRequest request, Principal principal, Model model) {
        String username = principal.getName();
        labService.addLab(username, courseId, request);
        return dashboardHtmlPage;
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/group-lab-assignment")
    public String createGroupLabAssignment(@RequestBody GroupLabAssignmentRequest request) {
        groupLabAssignmentService.createGroupLabAssignment(request);
        return labStatistics;
    }

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/courses/{courseId}/labs")
    public String getLabsByGroupId(@PathVariable("courseId") Long courseId,
                                   @RequestParam(value = "groupId") Long groupId,
                                   Model model) {
        Group group = groupService.getGroupById(groupId);
        List<LabDto> labsAssignedToGroup = labService.getLabsByCourseIdAndGroupId(courseId, groupId);
        List<StudentLabResult> studentLabResults = studentLabResultsGenerator.generateStudentsLabResults(group.getStudents(), labsAssignedToGroup, courseId);
        model.addAttribute("studentLabResults", studentLabResults);
        return labStatistics;
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @GetMapping
    public String getAllTeachers(Model model) {
        model.addAttribute(TEACHERS, teacherService.getAllTeachers());
        return teachersHtmlPage;
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @PostMapping
    public String addTeacher(@RequestParam("firstName") String firstName,
                             @RequestParam("lastName") String lastName,
                             Model model) {
        teacherService.addTeacher(new AddTeacherRequest(firstName, lastName));
        model.addAttribute(TEACHERS, teacherService.getAllTeachers());
        return teachersHtmlPage;
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @PutMapping("/{id}")
    public String update(@PathVariable("id") Long id,
                         @ModelAttribute Teacher teacher,
                         Model model) {
        teacher.setId(id);
        teacherService.updateTeacher(teacher);
        model.addAttribute(TEACHERS, teacherService.getAllTeachers());
        return teachersHtmlPage;
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id,
                         Model model) {
        teacherService.deleteById(id);
        model.addAttribute(TEACHERS, teacherService.getAllTeachers());
        return teachersHtmlPage;
    }

}
