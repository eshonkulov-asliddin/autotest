package uz.mu.autotest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.mu.autotest.model.UserTakenLab;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserTakenLabRepository extends JpaRepository<UserTakenLab, Long> {

    List<UserTakenLab> findByCourseIdAndUserUsername(Long courseId, String username);
    Optional<UserTakenLab> findById(Long id);
}
