package uz.mu.autotest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.mu.autotest.model.Lab;

import java.util.List;

public interface LabRepository extends JpaRepository<Lab, Long> {

    List<Lab> findLabsByCourseId(Long courseId);
}
