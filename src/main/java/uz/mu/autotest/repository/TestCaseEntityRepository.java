package uz.mu.autotest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.mu.autotest.model.TestCaseEntity;

public interface TestCaseEntityRepository extends JpaRepository<TestCaseEntity, Long> {
}
