package uz.mu.autotest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.mu.autotest.model.TestResults;

public interface TestResultsRepository extends JpaRepository<TestResults, Long> {
}
