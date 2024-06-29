package uz.mu.autotest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.mu.autotest.model.TestSuiteEntity;

public interface TestSuiteEntityRepository extends JpaRepository<TestSuiteEntity, Long> {
}
