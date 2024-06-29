package uz.mu.autotest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.mu.autotest.model.Teacher;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    @Query("SELECT u FROM User u WHERE u.username = :username")
    Optional<Teacher> findByUsername(String username);
}
