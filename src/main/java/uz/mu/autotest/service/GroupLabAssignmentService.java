package uz.mu.autotest.service;

import uz.mu.autotest.dto.GroupLabAssignmentRequest;
import uz.mu.autotest.model.GroupLabAssignment;

import java.util.List;

public interface GroupLabAssignmentService {

    void createGroupLabAssignment(GroupLabAssignmentRequest request);

    List<GroupLabAssignment> getLabsByGroupIdAndCourseId(Long groupId, Long courseId);

}
