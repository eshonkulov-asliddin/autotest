package uz.mu.autotest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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
import uz.mu.autotest.dto.admin.AddAdminRequest;
import uz.mu.autotest.dto.course.AddCourseRequest;
import uz.mu.autotest.dto.course.CourseDto;
import uz.mu.autotest.dto.course.UpdateCourseRequest;
import uz.mu.autotest.dto.group.GroupDto;
import uz.mu.autotest.dto.student.AddStudentRequest;
import uz.mu.autotest.dto.student.StudentDto;
import uz.mu.autotest.dto.teacher.AddTeacherRequest;
import uz.mu.autotest.dto.teacher.TeacherDto;
import uz.mu.autotest.model.Admin;
import uz.mu.autotest.model.Course;
import uz.mu.autotest.model.Group;
import uz.mu.autotest.model.Student;
import uz.mu.autotest.model.Teacher;
import uz.mu.autotest.service.AdminService;
import uz.mu.autotest.service.CourseService;
import uz.mu.autotest.service.GroupService;
import uz.mu.autotest.service.StudentService;
import uz.mu.autotest.service.TeacherService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static uz.mu.autotest.controller.util.ApiConst.ADMIN_PREFIX;
import static uz.mu.autotest.controller.util.KeyNames.ADMINS;
import static uz.mu.autotest.controller.util.KeyNames.COURSES;
import static uz.mu.autotest.controller.util.KeyNames.GROUPS;
import static uz.mu.autotest.controller.util.KeyNames.STUDENTS;
import static uz.mu.autotest.controller.util.KeyNames.TEACHERS;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = ADMIN_PREFIX)
public class AdminController {

    @Value("${app.admin.adminsHtmlPage}")
    private String adminsHtmlPage;
    @Value("${app.admin.teachersHtmlPage}")
    private String teachersHtmlPage;
    @Value("${app.admin.coursesAssignGroupFrom}")
    private String coursesAssignGroupFrom;
    @Value("${app.admin.coursesUnassignGroupForm}")
    private String coursesUnassignGroupForm;
    @Value("${app.admin.studentsHtmlPage}")
    private String studentsHtmlPage;
    @Value("${app.admin.groupsHtmlPage}")
    private String groupsHtmlPage;
    @Value("${app.admin.coursesHtmlPage}")
    private String coursesHtmlPage;

    private final AdminService adminService ;
    private final TeacherService teacherService;
    private final GroupService groupService;
    private final StudentService studentService;
    private final CourseService courseService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Long teachersCount = teacherService.getCount();
        Long groupsCount = groupService.getCount();
        Long studentsCount = studentService.getCount();
        Long coursesCount = courseService.getCount();
        Long adminsCount = adminService.getCount();
        model.addAttribute("teachersCount", teachersCount);
        model.addAttribute("groupsCount", groupsCount);
        model.addAttribute("studentsCount", studentsCount);
        model.addAttribute("coursesCount", coursesCount);
        model.addAttribute("adminsCount", adminsCount);
        return "admin/dashboard.html";
    }

    @GetMapping("/admins")
    public String getAllAdmins(Model model) {
        model.addAttribute(ADMINS, adminService.getAll());
        return adminsHtmlPage;
    }

    @PostMapping("/admins")
    public String addAdmin(@RequestParam("firstName") String  firstName,
                         @RequestParam("lastName") String lastName) {
        adminService.addAdmin(new AddAdminRequest(firstName, lastName));
        return adminsHtmlPage;
    }

    @PutMapping("/admins/{id}")
    public String update(@PathVariable("id") Long id,
                       @ModelAttribute Admin admin) {
        admin.setId(id);
        adminService.updateAdmin(admin);
        return adminsHtmlPage;
    }

    @DeleteMapping("/admins/{id}")
    public String delete(@PathVariable("id") Long id) {
        adminService.deleteById(id);
        return adminsHtmlPage;
    }

    /*****************************************TEACHERS*********************************/

    @GetMapping("/teachers")
    public String getAllTeachers(Model model) {
        model.addAttribute(TEACHERS, teacherService.getAllTeachers());
        return teachersHtmlPage;
    }

    @PostMapping("/teachers")
    public String addTeacher(@RequestParam("firstName") String firstName,
                             @RequestParam("lastName") String lastName,
                             Model model) {
        teacherService.addTeacher(new AddTeacherRequest(firstName, lastName));
        model.addAttribute(TEACHERS, teacherService.getAllTeachers());
        return teachersHtmlPage;
    }

    @PutMapping("/teachers/{id}")
    public String update(@PathVariable("id") Long id,
                         @ModelAttribute Teacher teacher,
                         Model model) {
        teacher.setId(id);
        teacherService.updateTeacher(teacher);
        model.addAttribute(TEACHERS, teacherService.getAllTeachers());
        return teachersHtmlPage;
    }

    @DeleteMapping("/teachers/{id}")
    public String delete(@PathVariable("id") Long id,
                         Model model) {
        teacherService.deleteById(id);
        model.addAttribute(TEACHERS, teacherService.getAllTeachers());
        return teachersHtmlPage;
    }

    /*****************************************STUDENTS*********************************/

    @GetMapping("/students")
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

    @PostMapping("/students")
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

    @PutMapping("/students/{id}")
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

    @DeleteMapping("/students/{id}")
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

    /*****************************************GROUP*********************************/

    @GetMapping("/groups")
    public String getAllGroups(Model model) {
        model.addAttribute(GROUPS, groupService.getAllGroups());
        return groupsHtmlPage;
    }

    @PostMapping("/groups")
    public String addGroup(@RequestParam("groupName") String groupName, Model model) {
        groupService.addGroup(groupName);
        model.addAttribute(GROUPS, groupService.getAllGroups());
        return groupsHtmlPage;
    }

    @PutMapping("/groups/{id}")
    public String updateGroup(@PathVariable("id") Long groupId, @RequestParam("name") String groupName, Model model) {
        Group group = groupService.getGroupById(groupId);
        group.setName(groupName);
        groupService.updateGroup(group);
        model.addAttribute(GROUPS, groupService.getAllGroups());
        return groupsHtmlPage;
    }

    @DeleteMapping("/groups/{id}")
    public String deleteGroup(@PathVariable("id") Long groupId, Model model) {
        groupService.deleteGroup(groupId);
        model.addAttribute(GROUPS, groupService.getAllGroups());
        return groupsHtmlPage;
    }

    /*****************************************COURSE*********************************/

    @GetMapping("/courses")
    public String getAllCourses(Model model) {
        List<CourseDto> allCourses = courseService.getAllCourses();
        List<TeacherDto> allTeachers = teacherService.getAllTeachers();
        model.addAttribute(COURSES, allCourses);
        model.addAttribute(TEACHERS, allTeachers);
        return coursesHtmlPage;
    }

    @PostMapping("/courses")
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

    @PutMapping("/courses/{id}")
    public String updateCourse(@PathVariable("id") Long id,
                               @RequestBody UpdateCourseRequest request) {
        courseService.update(id, request);
        return coursesHtmlPage;
    }

    @DeleteMapping("/courses/{id}")
    public String deleteCourse(@PathVariable("id") Long id) {
        courseService.delete(id);
        return coursesHtmlPage;
    }

    @GetMapping("/courses/{courseName}")
    public ResponseEntity<Optional<Course>> getByCourseName(@PathVariable(name = "courseName") String courseName) {
        Optional<Course> byName = courseService.getByName(courseName);
        return ResponseEntity.ok(byName);
    }

    @GetMapping("/courses/{id}/assign-groups")
    public String assignGroupsForm(@PathVariable("id") Long id, Model model) {
        CourseDto course = courseService.getCourseById(id);
        List<GroupDto> groups = groupService.getGroupsNotAssignedToCourse(id);
        model.addAttribute("course", course);
        model.addAttribute("groups", groups);
        return coursesAssignGroupFrom;
    }

    @PostMapping("/courses/{courseId}/assign-groups")
    public String assignGroups(@PathVariable("courseId") Long courseId,
                               @RequestBody Map<String, List<Long>> requestBody,
                               Model model) {
        List<Long> groupIds = requestBody.get("groupIds");
        courseService.assignGroupsFor(courseId, groupIds);
        return coursesHtmlPage;
    }

    @GetMapping("/courses/{courseId}/unassign-groups")
    public String unassignGroupsForm(@PathVariable("courseId") Long courseId, Model model) {
        CourseDto course = courseService.getCourseById(courseId);
        List<GroupDto> groups = groupService.getGroupsAssignedToCourse(courseId);
        model.addAttribute("course", course);
        model.addAttribute("groups", groups);
        return coursesUnassignGroupForm;
    }

    @PostMapping("/courses/{courseId}/unassign-groups")
    public String unassignGroups(@PathVariable("courseId") Long courseId,
                                 @RequestBody Map<String, List<Long>> requestBody,
                                 Model model) {
        List<Long> groupIds = requestBody.get("groupIds");
        courseService.unassignGroupsFor(courseId, groupIds);
        return coursesHtmlPage;
    }
}
