package uz.mu.autotest.service;

import uz.mu.autotest.dto.repo.GenerateRepositoryRequest;
import uz.mu.autotest.dto.student.StudentTakenLabDto;

public interface RepositoryService {
    StudentTakenLabDto generateRepository(GenerateRepositoryRequest request, String labOwner, String accessToken);
}
