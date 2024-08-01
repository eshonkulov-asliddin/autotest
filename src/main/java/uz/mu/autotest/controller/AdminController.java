package uz.mu.autotest.controller;

import lombok.RequiredArgsConstructor;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uz.mu.autotest.dto.admin.AddAdminRequest;
import uz.mu.autotest.model.Admin;
import uz.mu.autotest.service.impl.AdminServiceImpl;
import uz.mu.autotest.service.impl.CourseService;
import uz.mu.autotest.service.impl.GroupService;
import uz.mu.autotest.service.impl.StudentService;
import uz.mu.autotest.service.impl.TeacherService;

import static uz.mu.autotest.controller.util.ApiConst.ADMIN_PREFIX;
import static uz.mu.autotest.controller.util.KeyNames.ADMINS;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = ADMIN_PREFIX)
public class AdminController {

    @Value("${app.admin.adminsHtmlPage}")
    private String adminsHtmlPage;
    @Value("${app.admin.studentsHtmlPage}")
    private String studentsHtmlPage;

    private final AdminServiceImpl adminServiceImpl;
    private final TeacherService teacherService;
    private final GroupService groupService;
    private final StudentService studentService;
    private final CourseService courseService;

    @PreAuthorize("hasAnyRole({'SUPER_ADMIN', 'ADMIN'})")
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Long teachersCount = teacherService.getCount();
        Long groupsCount = groupService.getCount();
        Long studentsCount = studentService.getCount();
        Long coursesCount = courseService.getCount();
        Long adminsCount = adminServiceImpl.getCount();
        model.addAttribute("teachersCount", teachersCount);
        model.addAttribute("groupsCount", groupsCount);
        model.addAttribute("studentsCount", studentsCount);
        model.addAttribute("coursesCount", coursesCount);
        model.addAttribute("adminsCount", adminsCount);
        return "admin/dashboard.html";
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping
    public String getAllAdmins(Model model) {
        model.addAttribute(ADMINS, adminServiceImpl.getAll());
        return adminsHtmlPage;
    }
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping
    public String addAdmin(@RequestParam("firstName") String  firstName,
                         @RequestParam("lastName") String lastName) {
        adminServiceImpl.addAdmin(new AddAdminRequest(firstName, lastName));
        return adminsHtmlPage;
    }
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PutMapping("/{id}")
    public String update(@PathVariable("id") Long id,
                       @ModelAttribute Admin admin) {
        admin.setId(id);
        adminServiceImpl.updateAdmin(admin);
        return adminsHtmlPage;
    }
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        adminServiceImpl.deleteById(id);
        return adminsHtmlPage;
    }

}
