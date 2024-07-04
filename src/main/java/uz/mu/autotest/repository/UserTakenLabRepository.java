package uz.mu.autotest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.mu.autotest.model.UserTakenLab;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserTakenLabRepository extends JpaRepository<UserTakenLab, Long> {

    @Query("SELECT utl FROM UserTakenLab utl " +
            "JOIN utl.user u " +
            "JOIN utl.course c " +
            "WHERE c.id = :courseId " +
            "AND u.loginName = :username")
    List<UserTakenLab> findByCourseIdAndUserUsername(Long courseId, String username);
    Optional<UserTakenLab> findById(Long id);
}
