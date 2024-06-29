package uz.mu.autotest.service;

import uz.mu.autotest.dto.SubmitTaskRequest;
import uz.mu.autotest.dto.UserTakenLabDto;

public interface RepositoryService {
    UserTakenLabDto generateRepository(SubmitTaskRequest request, String labOwner, String accessToken);
}
