package uz.mu.autotest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uz.mu.autotest.model.Group;
import uz.mu.autotest.service.impl.GroupService;

import static uz.mu.autotest.controller.util.KeyNames.GROUPS;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
public class GroupController {

    @Value("${app.admin.groupsHtmlPage}")
    private String groupsHtmlPage;

    private final GroupService groupService;

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

}
