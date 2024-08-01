package uz.mu.autotest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.mu.autotest.model.Lab;

import java.util.List;

public interface LabRepository extends JpaRepository<Lab, Long> {

    @Query("SELECT l FROM Lab l WHERE l.course.id = :courseId ")
    List<Lab> findLabsByCourseId(@Param("courseId") Long courseId);
}
