package uz.mu.autotest.service;

import uz.mu.autotest.dto.SubmitTaskRequest;
import uz.mu.autotest.model.UserTakenLab;

public interface RepositoryService {
    UserTakenLab generateRepository(SubmitTaskRequest request, String labOwner, String accessToken);
}
