package uz.mu.autotest.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.mu.autotest.client.GithubClient;
import uz.mu.autotest.converter.testsuite.TestResultEntityToTestResultDtoConverter;
import uz.mu.autotest.dto.AttemptDto;
import uz.mu.autotest.dto.SubmitTaskRequest;
import uz.mu.autotest.dto.testsuite.TestResultDto;
import uz.mu.autotest.exception.GetLastActionRunException;
import uz.mu.autotest.exception.NotFoundException;
import uz.mu.autotest.model.Attempt;
import uz.mu.autotest.model.StudentTakenLab;
import uz.mu.autotest.model.TestResults;
import uz.mu.autotest.service.SubmitTaskService;
import uz.mu.autotest.service.UserSessionData;
import uz.mu.autotest.utils.GithubUrlParser;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Slf4j
public class GithubSubmitTaskService implements SubmitTaskService {

    private final StudentTakenLabService studentTakenLabService;
    private final GithubClient githubClient;
    private final RetryService retryService;
    private final TestResultEntityToTestResultDtoConverter testResultEntityToTestResultDtoConverter;
    private final InMemoryCacheService inMemoryCacheService;

    public void submitTask(SubmitTaskRequest request, String owner, String accessToken) {
        log.info("Started submit task....");

        log.info("Retrieving Started Lab by id: {}", request.labId());
        Optional<StudentTakenLab> studentTakenLab = studentTakenLabService.getById(request.labId());

        if (studentTakenLab.isEmpty()) {
            throw new NotFoundException(String.format("StudentTakenLab with id %s not found", request.labId()));
        }

        String repo = GithubUrlParser.extractRepoName(studentTakenLab.get().getGithubUrl());
        log.info("Extracted Repo Name: {}", repo);
        ResponseEntity<String> response = githubClient.triggerWorkflow(owner, repo, accessToken);
        log.info("Trigger Workflow Response: {}", response);
        inMemoryCacheService.store(owner, new UserSessionData(accessToken, studentTakenLab.get()));
    }
}
