package uz.mu.autotest.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import uz.mu.autotest.dto.student.AddStudentRequest;
import uz.mu.autotest.dto.student.StudentDto;
import uz.mu.autotest.model.Student;
import uz.mu.autotest.service.GroupService;
import uz.mu.autotest.service.StudentService;

import static uz.mu.autotest.controller.admin.util.ApiConst.ADMINS_PREFIX;
import static uz.mu.autotest.controller.admin.util.ApiConst.STUDENTS_PREFIX;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = ADMINS_PREFIX+STUDENTS_PREFIX)
public class StudentController {

    public static final String STUDENTS_HTML_PAGE = "student/students.html";
    public static final String STUDENTS = "students";
    public static final String GROUPS = "groups";
    private final StudentService studentService;
    private final GroupService groupService;

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
        return STUDENTS_HTML_PAGE;
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "student/dashboard.html";
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
        return STUDENTS_HTML_PAGE;
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
        return STUDENTS_HTML_PAGE; // Redirect to the students list page
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
        return STUDENTS_HTML_PAGE; // Redirect to the students list page
    }
}
