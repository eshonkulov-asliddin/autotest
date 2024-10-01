package uz.mu.autotest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.mu.autotest.model.StudentTakenLab;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentTakenLabRepository extends JpaRepository<StudentTakenLab, Long> {

    @Query("""
    SELECT utl FROM StudentTakenLab utl
    JOIN utl.user u
    JOIN utl.course c
    WHERE c.id = :courseId
    AND u.login = :login
    """)
    List<StudentTakenLab> findByCourseIdAndLogin(Long courseId, String login);

    @Query("""
    SELECT utl FROM StudentTakenLab utl
    JOIN utl.user u
    JOIN utl.course c
    WHERE c.id = :courseId
    AND u.username = :username
    """)
    List<StudentTakenLab> findByCourseIdAndUsername(Long courseId, String username);

    Optional<StudentTakenLab> findById(Long id);

    @Query("""
    SELECT stl FROM StudentTakenLab stl 
    JOIN stl.user u
    JOIN u.groups g 
    WHERE stl.lab.id = :labId AND g.id = :groupId
    """)
    List<StudentTakenLab> findStudentTakenLabByLabIdAndGroupId(@Param("labId") Long labId, @Param("groupId") Long groupId);

}
