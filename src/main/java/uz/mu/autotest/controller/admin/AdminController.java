package uz.mu.autotest.controller.admin;

import lombok.RequiredArgsConstructor;
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
import uz.mu.autotest.service.AdminService;

import static uz.mu.autotest.controller.admin.util.ApiConst.ADMINS_PREFIX;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = ADMINS_PREFIX)
public class AdminController {

    private final String ADMINS_HTML_PAGE = "/admin/admins.html";
    private final String ADMINS = "admins";
    private final AdminService adminService ;

    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin/dashboard.html";
    }

    @GetMapping
    public String getAllAdmins(Model model) {
        model.addAttribute(ADMINS, adminService.getAll());
        return ADMINS_HTML_PAGE;
    }

    @PostMapping
    public String addAdmin(@RequestParam("firstName") String  firstName,
                         @RequestParam("lastName") String lastName) {
        adminService.addAdmin(new AddAdminRequest(firstName, lastName));
        return ADMINS_HTML_PAGE;
    }

    @PutMapping("/{id}")
    public String update(@PathVariable("id") Long id,
                       @ModelAttribute Admin admin) {
        admin.setId(id);
        adminService.updateAdmin(admin);
        return ADMINS_HTML_PAGE;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        adminService.deleteById(id);
        return ADMINS_HTML_PAGE;
    }

    @GetMapping("/test")
    public String test() {
        return "test.html";
    }


}
