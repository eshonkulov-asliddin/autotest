package uz.mu.autotest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uz.mu.autotest.dto.course.CourseDto;
import uz.mu.autotest.dto.student.AddStudentRequest;
import uz.mu.autotest.dto.student.StudentDto;
import uz.mu.autotest.model.Student;
import uz.mu.autotest.service.impl.CourseService;
import uz.mu.autotest.service.impl.GroupService;
import uz.mu.autotest.service.impl.StudentService;

import java.security.Principal;
import java.util.List;

import static uz.mu.autotest.controller.util.KeyNames.GROUPS;
import static uz.mu.autotest.controller.util.KeyNames.STUDENTS;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/students")
@PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
public class StudentController {

    @Value("${app.admin.studentsHtmlPage}")
    private String studentsHtmlPage;
    @Value("${app.student.dashboardHtmlPage}")
    private String studentsDashboardHtmlPage;

    private final StudentService studentService;
    private final GroupService groupService;
    private final CourseService courseService;

    @PreAuthorize("hasRole('STUDENT') or hasAuthority('OAUTH2_USER')")
    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        String username = principal.getName();
        List<CourseDto> courses = courseService.getStudentAssignedCourses(username);
        model.addAttribute("courses", courses);
        return studentsDashboardHtmlPage;
    }

    @GetMapping
    public String getStudentsByGroup(
            @RequestParam(required = false) Long groupId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size, Model model) {
        Pageable pageable = PageRequest.of(page-1, size);
        Page<StudentDto> students;
        if (groupId != null) {
            students = studentService.getStudentsByGroupId(groupId, pageable);
        } else {
            students = studentService.getAllStudents(pageable);
        }
        model.addAttribute(GROUPS, groupService.getAllGroups());
        model.addAttribute(STUDENTS, students);
        model.addAttribute("currentPage", students.getNumber() + 1);
        model.addAttribute("totalItems", students.getTotalElements());
        model.addAttribute("totalPages", students.getTotalPages());
        model.addAttribute("pageSize", size);
        return studentsHtmlPage;
    }

    @PostMapping
    public String addStudent(@RequestParam("firstName") String firstName,
                             @RequestParam("lastName") String lastName,
                             @RequestParam("groupId") Long groupId,
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int size, Model model) {

        Pageable pageable = PageRequest.of(page, size);
        studentService.addStudent(new AddStudentRequest(firstName, lastName, groupId));
        Page<StudentDto> allStudents = studentService.getAllStudents(pageable);
        model.addAttribute(STUDENTS, allStudents);
        return studentsHtmlPage;
    }

    @PutMapping("/{id}")
    public String updateStudent(@PathVariable("id") Long id,
                                @ModelAttribute Student student,
                                @RequestParam(defaultValue = "1") int page,
                                @RequestParam(defaultValue = "10") int size, Model model) {
        Pageable pageable = PageRequest.of(page-1, size);
        student.setId(id); // Set the ID from the path variable
        studentService.updateStudent(student); // Update the student in the database
        Page<StudentDto> allStudents = studentService.getAllStudents(pageable);
        model.addAttribute(GROUPS, groupService.getAllGroups());
        model.addAttribute(STUDENTS, allStudents);
        return studentsHtmlPage;
    }

    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable("id") Long id,
                                @RequestParam(defaultValue = "1") int page,
                                @RequestParam(defaultValue = "10") int size, Model model) {
        Pageable pageable = PageRequest.of(page-1, size);
        studentService.deleteStudent(id); // Delete the student from the database
        Page<StudentDto> allStudents = studentService.getAllStudents(pageable);
        model.addAttribute(GROUPS, groupService.getAllGroups());
        model.addAttribute(STUDENTS, allStudents);
        return studentsHtmlPage;
    }

}
