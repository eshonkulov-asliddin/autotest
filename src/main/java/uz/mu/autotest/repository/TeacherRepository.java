package uz.mu.autotest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.mu.autotest.model.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
}
