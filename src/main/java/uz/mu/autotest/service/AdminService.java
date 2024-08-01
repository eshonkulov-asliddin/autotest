package uz.mu.autotest.service;

import uz.mu.autotest.dto.admin.AddAdminRequest;
import uz.mu.autotest.model.Admin;

import java.util.List;

public interface AdminService {
    List<Admin> getAll();
    void addAdmin(AddAdminRequest request);
    void updateAdmin(Admin admin);
    void deleteById(Long id);
    Long getCount();
}
