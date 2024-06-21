package uz.mu.autotest.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import uz.mu.autotest.model.Student;

import java.util.Optional;

public interface StudentRepository extends PagingAndSortingRepository<Student, Long> {
    void save(Student student);
    Optional<Student> findById(Long id);
    void delete(Student student);
    @Query("SELECT s FROM Student s JOIN s.groups g WHERE g.id = :groupId")
    Page<Student> findStudentsByGroupId(Long groupId, Pageable pageable);

    @Query("SELECT s FROM Student s WHERE s.oAuth2Login != NULL AND s.oAuth2Login = :loginName")
    Optional<Student> findByOAuth2Login(String loginName);

    Optional<Student> findByUsername(String currentlyAuthorizedUserUsername);
}
