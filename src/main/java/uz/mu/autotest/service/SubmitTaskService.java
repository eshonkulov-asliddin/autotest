package uz.mu.autotest.service;

import uz.mu.autotest.dto.lab.SubmitTaskRequest;

public interface SubmitTaskService {
    void submitTask(SubmitTaskRequest request, String owner, String accessToken);
}
