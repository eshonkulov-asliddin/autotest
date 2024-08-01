package uz.mu.autotest.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.mu.autotest.dto.GroupLabAssignmentRequest;
import uz.mu.autotest.exception.NotFoundException;
import uz.mu.autotest.model.Course;
import uz.mu.autotest.model.Group;
import uz.mu.autotest.model.GroupLabAssignment;
import uz.mu.autotest.model.Lab;
import uz.mu.autotest.repository.CourseRepository;
import uz.mu.autotest.repository.GroupLabAssignmentRepository;
import uz.mu.autotest.repository.GroupRepository;
import uz.mu.autotest.repository.LabRepository;
import uz.mu.autotest.service.GroupLabAssignmentService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupLabAssignmentServiceImpl implements GroupLabAssignmentService {

    private final GroupRepository groupRepository;
    private final CourseRepository courseRepository;
    private final LabRepository labRepository;
    private final GroupLabAssignmentRepository groupLabAssignmentRepository;


    @Override
    public void createGroupLabAssignment(GroupLabAssignmentRequest request) {
        GroupLabAssignment assignment = new GroupLabAssignment();
        Long groupId = request.groupId();
        Long courseId = request.courseId();
        Long labId = request.labId();

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new NotFoundException(String.format("Group not found with id: %s", groupId)));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException(String.format("Course not found with id: %s", courseId)));

        Lab lab = labRepository.findById(labId)
                .orElseThrow(() -> new NotFoundException(String.format("Lab not found with id: %s", labId)));

        verifyGroupHasCourse(group, courseId);
        verifyCourseHasLab(course, labId);

        assignment.setGroup(group);
        assignment.setCourse(course);
        assignment.setLab(lab);
        assignment.setDeadline(request.deadline());
        groupLabAssignmentRepository.save(assignment);
    }

    @Override
    public List<GroupLabAssignment> getLabsByGroupIdAndCourseId(Long groupId, Long courseId) {
        // TODO verify groupId and courseId

        return groupLabAssignmentRepository.findFor(groupId, courseId);
    }

    private void verifyGroupHasCourse(Group group, Long courseId) {

    }

    private void verifyCourseHasLab(Course course, Long labId) {

    }

}
