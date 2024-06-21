package uz.mu.autotest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.mu.autotest.model.Group;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {
    @Query("SELECT g FROM Group g WHERE g.id IN :groupIds")
    List<Group> findByIdIn(List<Long> groupIds);

    @Query("SELECT g FROM Group g WHERE g.id NOT IN (SELECT tg.id FROM Teacher t JOIN t.groups tg WHERE t.id = :id)")
    List<Group> findGroupsNotAssignedToTeacher(Long id);

    @Query("SELECT g FROM Group g WHERE g.id IN (SELECT tg.id FROM Teacher t JOIN t.groups tg WHERE t.id = :id)")
    List<Group> findGroupsAssignedToTeacher(Long id);
}
