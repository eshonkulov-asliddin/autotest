package uz.mu.autotest.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import uz.mu.autotest.dto.admin.AddAdminRequest;
import uz.mu.autotest.model.Admin;
import uz.mu.autotest.model.Role;
import uz.mu.autotest.repository.AdminRepository;
import uz.mu.autotest.security.credential.CredentialGenerator;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final CredentialGenerator credentialGenerator;
    private final ConversionService conversionService;

    public List<Admin> getAll() {
        return adminRepository.findAll();
    }

    public void addAdmin(AddAdminRequest request) {
        String username = credentialGenerator.generateUsername(request.firstName(), request.lastName());
        String password = credentialGenerator.generatePassword();

        Admin admin = conversionService.convert(request, Admin.class);
        admin.setUsername(username);
        admin.setPassword(password);
        admin.setRoles(Set.of(Role.ADMIN));
        admin.setActive(true);
        adminRepository.save(admin);
    }

    public void updateAdmin(Admin admin) {
        Admin adminById = getById(admin.getId());
        // update firstName and lastName
        adminById.setFirstName(admin.getFirstName());
        adminById.setLastName(admin.getLastName());
        adminRepository.save(adminById);
    }

    public void deleteById(Long id) {
        Admin admin = getById(id);
        adminRepository.delete(admin);
    }

    private Admin getById(Long id) {
        Optional<Admin> adminById = adminRepository.findById(id);
        if (adminById.isEmpty()) {
            throw new EntityNotFoundException("Admin not found with id: " + id);
        }
        return adminById.get();
    }

    public Long getCount() {
        return adminRepository.count();
    }
}
