package uz.mu.autotest.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.mu.autotest.client.GithubClient;
import uz.mu.autotest.dto.AttemptDto;
import uz.mu.autotest.dto.SubmitTaskRequest;
import uz.mu.autotest.exception.GetLastActionRunException;
import uz.mu.autotest.model.Attempt;
import uz.mu.autotest.model.UserTakenLab;
import uz.mu.autotest.service.RetryService;
import uz.mu.autotest.service.SubmitTaskService;
import uz.mu.autotest.service.UserTakenLabService;
import uz.mu.autotest.utils.GithubUrlParser;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Slf4j
public class GithubSubmitTaskService implements SubmitTaskService {

    private final UserTakenLabService userTakenLabService;
    private final GithubClient githubClient;
    private final RetryService retryService;

    public AttemptDto submitTask(SubmitTaskRequest request, String owner, String accessToken) {
        log.info("Started submit task....");

        log.info("Retrieving Started Lab by id: {}", request.labId());
        Optional<UserTakenLab> userTakenLab = userTakenLabService.getById(request.labId());
        log.info("Retrieved Started Lab: {}", userTakenLab.get());

        String repo = GithubUrlParser.extractRepoName(userTakenLab.get().getGithubUrl());
        log.info("Extracted Repo Name: {}", repo);
        ResponseEntity<String> response = githubClient.triggerWorkflow(owner, repo, accessToken);
        log.info("Trigger Workflow Response: {}", response);

        try {
            CompletableFuture<Attempt> attemptCompletableFuture = retryService.retryAction(owner, repo, accessToken, userTakenLab.get());
            Attempt attempt = attemptCompletableFuture.get();
            log.info("Last Attempt: {}", attempt);
            return new AttemptDto(attempt.getId(), attempt.getRunNumber(), attempt.getDetailsUrl(), String.valueOf(attempt.getResult()), attempt.getUserTakenLab().getId(), attempt.getTestSuite().getId());
        }catch (GetLastActionRunException e){
            throw e;
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}
