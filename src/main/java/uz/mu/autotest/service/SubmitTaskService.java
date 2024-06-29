package uz.mu.autotest.service;

import uz.mu.autotest.dto.AttemptDto;
import uz.mu.autotest.dto.SubmitTaskRequest;

public interface SubmitTaskService {
    AttemptDto submitTask(SubmitTaskRequest request, String owner, String accessToken);
}
