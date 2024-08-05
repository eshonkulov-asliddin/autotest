package uz.mu.autotest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.mu.autotest.model.Course;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findByName(String name);

    @Query("SELECT c FROM Course c JOIN FETCH c.groups WHERE c.teacher.username = :username")
    List<Course> findByTeacherUsername(@Param("username") String username);

    @Query("SELECT c FROM Course c WHERE c.id = :courseId AND c.teacher.username = :username")
    Optional<Course> findByIdAndTeacherUsername(@Param("courseId") Long courseId, @Param("username") String username);

    @Query("SELECT c FROM Course c JOIN FETCH c.groups g WHERE g.id IN :list")
    List<Course> findByGroupIds(List<Long> list);
}
