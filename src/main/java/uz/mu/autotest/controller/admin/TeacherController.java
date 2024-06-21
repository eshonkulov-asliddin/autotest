package uz.mu.autotest.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import uz.mu.autotest.dto.group.GroupDto;
import uz.mu.autotest.dto.teacher.AddTeacherRequest;
import uz.mu.autotest.dto.teacher.TeacherDto;
import uz.mu.autotest.model.Teacher;
import uz.mu.autotest.service.GroupService;
import uz.mu.autotest.service.TeacherService;

import java.util.List;
import java.util.Map;

import static uz.mu.autotest.controller.admin.util.ApiConst.ADMINS_PREFIX;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = ADMINS_PREFIX+"/teachers")
@Slf4j
public class TeacherController {

    private final String TEACHERS_HTML_PAGE = "/teacher/teachers.html";
    private final String TEACHERS_ASSIGN_GROUPS_FORM = "/teacher/assign-groups-form.html";
    private final String TEACHERS_UNASSIGN_GROUPS_FORM = "/teacher/unassign-groups-form.html";
    private final String TEACHERS = "teachers";
    private final TeacherService teacherService;
    private final GroupService groupService;

    @GetMapping("/dashboard")
    public String dashboard() {
        return "teacher/dashboard.html";
    }

    @GetMapping
    public String getAllTeachers(Model model) {
        model.addAttribute(TEACHERS, teacherService.getAllTeachers());
        return TEACHERS_HTML_PAGE;
    }

    @PostMapping
    public String addTeacher(@RequestParam("firstName") String firstName,
                             @RequestParam("lastName") String lastName,
                             Model model) {
        teacherService.addTeacher(new AddTeacherRequest(firstName, lastName));
        model.addAttribute(TEACHERS, teacherService.getAllTeachers());
        return TEACHERS_HTML_PAGE;
    }

    @GetMapping("{id}/assign-groups")
    public String assignGroupsForm(@PathVariable("id") Long id, Model model) {
        TeacherDto teacher = teacherService.getTeacherById(id);
        List<GroupDto> assignedGroups = groupService.getGroupsNotAssignedToTeacher(id);
        model.addAttribute("teacher", teacher);
        model.addAttribute("groups", assignedGroups);
        return TEACHERS_ASSIGN_GROUPS_FORM;
    }

    @PostMapping("/{teacherId}/assign-groups")
    public String assignGroups(@PathVariable("teacherId") Long id,
                               @RequestBody Map<String, List<Long>> requestBody,
                               Model model) {
        log.info(requestBody.toString());
        List<Long> groupIds = requestBody.get("groupIds");
        teacherService.assignGroupsFor(id, groupIds);
        return TEACHERS_HTML_PAGE;
    }

    @GetMapping("{id}/unassign-groups")
    public String unassignGroupsForm(@PathVariable("id") Long id, Model model) {
        TeacherDto teacher = teacherService.getTeacherById(id);
        List<GroupDto> unassignedGroups = groupService.getGroupsAssignedToTeacher(id);
        model.addAttribute("teacher", teacher);
        model.addAttribute("groups", unassignedGroups);
        return TEACHERS_UNASSIGN_GROUPS_FORM;
    }

    @PostMapping("/{teacherId}/unassign-groups")
    public String unassignGroups(@PathVariable("teacherId") Long id,
                               @RequestBody Map<String, List<Long>> requestBody,
                               Model model) {
        log.info(requestBody.toString());
        List<Long> groupIds = requestBody.get("groupIds");
        teacherService.unassignGroupsFor(id, groupIds);
        return TEACHERS_HTML_PAGE;
    }

    @PutMapping("/{id}")
    public String update(@PathVariable("id") Long id,
                         @ModelAttribute Teacher teacher,
                         Model model) {
        teacher.setId(id);
        teacherService.updateTeacher(teacher);
        model.addAttribute(TEACHERS, teacherService.getAllTeachers());
        return TEACHERS_HTML_PAGE;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id,
                         Model model) {
        teacherService.deleteById(id);
        model.addAttribute(TEACHERS, teacherService.getAllTeachers());
        return TEACHERS_HTML_PAGE;
    }

}
