package uz.mu.autotest.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uz.mu.autotest.model.Group;
import uz.mu.autotest.service.GroupService;

import static uz.mu.autotest.controller.admin.util.ApiConst.ADMINS_PREFIX;
import static uz.mu.autotest.controller.admin.util.ApiConst.GROUPS_PREFIX;

@Controller
@RequestMapping(path = ADMINS_PREFIX+GROUPS_PREFIX)
@RequiredArgsConstructor
public class GroupController {

    public static final String GROUPS_HTML_PAGE = "group/groups.html";
    public static final String GROUPS = "groups";
    private final GroupService groupService;

    @GetMapping
    public String getAllGroups(Model model) {
        model.addAttribute(GROUPS, groupService.getAllGroups());
        return GROUPS_HTML_PAGE;
    }

    @PostMapping
    public String addGroup(@RequestParam("groupName") String groupName, Model model) {
        groupService.addGroup(groupName);
        model.addAttribute(GROUPS, groupService.getAllGroups());
        return GROUPS_HTML_PAGE;
    }

    @PutMapping("/{id}")
    public String updateGroup(@PathVariable("id") Long groupId, @RequestParam("name") String groupName, Model model) {
        Group group = groupService.getGroupById(groupId);
        group.setName(groupName);
        groupService.updateGroup(group);
        model.addAttribute(GROUPS, groupService.getAllGroups());
        return GROUPS_HTML_PAGE;
    }

    @DeleteMapping("/{id}")
    public String deleteGroup(@PathVariable("id") Long groupId, Model model) {
        groupService.deleteGroup(groupId);
        model.addAttribute(GROUPS, groupService.getAllGroups());
        return GROUPS_HTML_PAGE;
    }
}
