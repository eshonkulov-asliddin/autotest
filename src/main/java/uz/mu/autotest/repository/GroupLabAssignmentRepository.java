package uz.mu.autotest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.mu.autotest.model.GroupLabAssignment;

import java.util.List;

public interface GroupLabAssignmentRepository extends JpaRepository<GroupLabAssignment, Long> {
    @Query("SELECT gla FROM GroupLabAssignment gla WHERE gla.group.id = :groupId AND gla.course.id = :courseId")
    List<GroupLabAssignment> findFor(Long groupId, Long courseId);
}
