package uz.mu.autotest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.mu.autotest.model.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}
