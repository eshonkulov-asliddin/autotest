package uz.mu.autotest.service;

import uz.mu.autotest.dto.GenerateRepositoryRequest;
import uz.mu.autotest.dto.StudentTakenLabDto;

public interface RepositoryService {
    StudentTakenLabDto generateRepository(GenerateRepositoryRequest request, String labOwner, String accessToken);
}
