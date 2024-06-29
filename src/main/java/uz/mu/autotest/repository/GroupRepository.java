package uz.mu.autotest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.mu.autotest.model.Group;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {
    @Query("SELECT g FROM Group g WHERE g.id IN :groupIds")
    List<Group> findByIdIn(List<Long> groupIds);

    @Query("SELECT g FROM Group g WHERE g.id NOT IN (SELECT g.id FROM Course c JOIN c.groups g WHERE c.id = :courseId)")
    List<Group> findGroupsNotAssignedToCourseId(Long courseId);

    @Query("SELECT g FROM Group g WHERE g.id IN (SELECT g.id FROM Course c JOIN c.groups g WHERE c.id = :courseId)")
    List<Group> findGroupsAssignedToCourseId(Long courseId);

}
