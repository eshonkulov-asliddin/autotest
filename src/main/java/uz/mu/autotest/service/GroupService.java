package uz.mu.autotest.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import uz.mu.autotest.dto.group.GroupDto;
import uz.mu.autotest.exception.NotFoundException;
import uz.mu.autotest.model.Group;
import uz.mu.autotest.repository.GroupRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final ConversionService conversionService;

    public void addGroup(String groupName) {
        Group group = new Group();
        group.setName(groupName);
        groupRepository.save(group);
    }

    public List<GroupDto> getAllGroups() {
        List<Group> groups = groupRepository.findAll();
        return groups.stream()
                .map(group -> conversionService.convert(group, GroupDto.class))
                .toList();
    }

    public Group getGroupById(Long id) {
        Optional<Group> group = groupRepository.findById(id);
        if (group.isEmpty()){
            throw new NotFoundException("Group not found with id: " + id);
        }
        return group.get();
    }

    public void updateGroup(Group group) {
        groupRepository.save(group);
    }

    public void deleteGroup(Long groupId) {
        Optional<Group> groupById = groupRepository.findById(groupId);
        if (groupById.isEmpty()) {
            throw new EntityNotFoundException("Group not found with id:" + groupId);
        }
        Group group = groupById.get();
        group.getStudents().forEach(student -> student.getGroups().remove(group));
        group.getTeachers().forEach(teacher -> teacher.getGroups().remove(group));
        group.getCourses().forEach(course -> course.getGroups().remove(group));
        groupRepository.delete(group);
    }

    public List<Group> getGroupsByIds(List<Long> groupIds) {
        return groupRepository.findByIdIn(groupIds);
    }

    public List<GroupDto> getGroupsNotAssignedToTeacher(Long id) {
        List<Group> groups = groupRepository.findGroupsNotAssignedToTeacher(id);
        return groups.stream()
                .map(group -> conversionService.convert(group, GroupDto.class))
                .toList();
    }

    public List<GroupDto> getGroupsAssignedToTeacher(Long id) {
        List<Group> groups = groupRepository.findGroupsAssignedToTeacher(id);
        return groups.stream()
                .map(group -> conversionService.convert(group, GroupDto.class))
                .toList();
    }
}
