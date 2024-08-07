package uz.mu.autotest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.mu.autotest.model.Lab;

import java.util.List;

public interface LabRepository extends JpaRepository<Lab, Long> {

    List<Lab> findByCourseId(@Param("courseId") Long courseId);

    @Query("SELECT l FROM Lab l JOIN l.course c JOIN c.groups g WHERE c.id = :courseId AND g.id = :groupId")
    List<Lab> findByCourseIdAndGroupId(@Param("courseId") Long courseId, @Param("groupId") Long groupId);
}
