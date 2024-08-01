package uz.mu.autotest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uz.mu.autotest.dto.course.AddCourseRequest;
import uz.mu.autotest.dto.course.CourseDto;
import uz.mu.autotest.dto.course.UpdateCourseRequest;
import uz.mu.autotest.dto.group.GroupDto;
import uz.mu.autotest.dto.teacher.TeacherDto;
import uz.mu.autotest.model.Course;
import uz.mu.autotest.service.impl.CourseService;
import uz.mu.autotest.service.impl.GroupService;
import uz.mu.autotest.service.impl.TeacherService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static uz.mu.autotest.controller.util.KeyNames.COURSES;
import static uz.mu.autotest.controller.util.KeyNames.TEACHERS;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/courses")
@PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
public class CourseController {

    @Value("${app.admin.coursesHtmlPage}")
    private String coursesHtmlPage;
    @Value("${app.admin.coursesAssignGroupFrom}")
    private String coursesAssignGroupFrom;
    @Value("${app.admin.coursesUnassignGroupForm}")
    private String coursesUnassignGroupForm;

    private final CourseService courseService;
    private final TeacherService teacherService;
    private final GroupService groupService;

    @GetMapping
    public String getAllCourses(Model model) {
        List<CourseDto> allCourses = courseService.getAllCourses();
        List<TeacherDto> allTeachers = teacherService.getAllTeachers();
        model.addAttribute(COURSES, allCourses);
        model.addAttribute(TEACHERS, allTeachers);
        return coursesHtmlPage;
    }

    @PostMapping
    public String addCourse(@RequestParam("name") String name,
                            @RequestParam("teacherUsername") String teacherUsername,
                            Model model) {
        courseService.addCourse(new AddCourseRequest(name, teacherUsername));
        List<CourseDto> allCourses = courseService.getAllCourses();
        List<TeacherDto> allTeachers = teacherService.getAllTeachers();
        model.addAttribute(COURSES, allCourses);
        model.addAttribute(TEACHERS, allTeachers);
        return coursesHtmlPage;
    }

    @PutMapping("/{id}")
    public String updateCourse(@PathVariable("id") Long id,
                               @RequestBody UpdateCourseRequest request) {
        courseService.update(id, request);
        return coursesHtmlPage;
    }

    @DeleteMapping("/{id}")
    public String deleteCourse(@PathVariable("id") Long id) {
        courseService.delete(id);
        return coursesHtmlPage;
    }

    @GetMapping("/{courseName}")
    public ResponseEntity<Optional<Course>> getByCourseName(@PathVariable(name = "courseName") String courseName) {
        Optional<Course> byName = courseService.getByName(courseName);
        return ResponseEntity.ok(byName);
    }

    @GetMapping("/{id}/assign-groups")
    public String assignGroupsForm(@PathVariable("id") Long id, Model model) {
        CourseDto course = courseService.getCourseById(id);
        List<GroupDto> groups = groupService.getGroupsNotAssignedToCourse(id);
        model.addAttribute("course", course);
        model.addAttribute("groups", groups);
        return coursesAssignGroupFrom;
    }

    @PostMapping("/{courseId}/assign-groups")
    public String assignGroups(@PathVariable("courseId") Long courseId,
                               @RequestBody Map<String, List<Long>> requestBody,
                               Model model) {
        List<Long> groupIds = requestBody.get("groupIds");
        courseService.assignGroupsFor(courseId, groupIds);
        return coursesHtmlPage;
    }

    @GetMapping("/{courseId}/unassign-groups")
    public String unassignGroupsForm(@PathVariable("courseId") Long courseId, Model model) {
        CourseDto course = courseService.getCourseById(courseId);
        List<GroupDto> groups = groupService.getGroupsAssignedToCourse(courseId);
        model.addAttribute("course", course);
        model.addAttribute("groups", groups);
        return coursesUnassignGroupForm;
    }

    @PostMapping("/{courseId}/unassign-groups")
    public String unassignGroups(@PathVariable("courseId") Long courseId,
                                 @RequestBody Map<String, List<Long>> requestBody,
                                 Model model) {
        List<Long> groupIds = requestBody.get("groupIds");
        courseService.unassignGroupsFor(courseId, groupIds);
        return coursesHtmlPage;
    }

}
