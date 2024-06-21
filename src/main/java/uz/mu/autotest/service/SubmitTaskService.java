package uz.mu.autotest.service;

import uz.mu.autotest.dto.SubmitTaskRequest;
import uz.mu.autotest.model.Attempt;

public interface SubmitTaskService {
    Attempt submitTask(SubmitTaskRequest request, String owner, String accessToken);
}
