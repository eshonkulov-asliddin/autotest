package uz.mu.autotest.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import uz.mu.autotest.model.Student;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends PagingAndSortingRepository<Student, Long> {
    void save(Student student);
    Optional<Student> findById(Long id);
    void delete(Student student);
    @Query("SELECT s FROM Student s JOIN s.groups g WHERE g.id = :groupId")
    Page<Student> findByGroupId(Long groupId, Pageable pageable);

    @Query("SELECT s FROM Student s JOIN s.groups g WHERE g.id = :groupId")
    List<Student> findByGroupId(Long groupId);

    @Query("SELECT s FROM Student s JOIN FETCH s.groups WHERE s.login = :login")
    Optional<Student> findByLogin(String login);

    Optional<Student> findByUsername(String currentlyAuthorizedUserUsername);

    @Query("SELECT COUNT(s) FROM Student s")
    Long count();
}
