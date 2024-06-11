package uz.mu.autotest.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.mu.autotest.client.GithubClient;
import uz.mu.autotest.utils.GithubUrlParser;
import uz.mu.autotest.exception.GetLastActionRunException;
import uz.mu.autotest.model.Attempt;
import uz.mu.autotest.model.UserTakenLab;
import uz.mu.autotest.websocket.SubmitTaskRequest;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Slf4j
public class GithubTaskSubmissionService {

    private final UserTakenLabService userTakenLabService;
    private final GithubClient githubClient;
    private final RetryService retryService;

    @Transactional
    public Attempt submitTask(SubmitTaskRequest request, String owner, String accessToken) {

        Optional<UserTakenLab> userTakenLab = userTakenLabService.getById(request.labId());
        log.info("Retrieved Started Lab: {}", userTakenLab.get());

        String repo = GithubUrlParser.extractRepoName(userTakenLab.get().getGithubUrl());

        ResponseEntity<String> response = githubClient.triggerWorkflow(owner, repo, accessToken);
        log.info("Trigger Workflow Response: {}", response);

        try {
            CompletableFuture<Attempt> attemptCompletableFuture = retryService.retryAction(owner, repo, accessToken, userTakenLab.get());
            Attempt attempt = attemptCompletableFuture.get();
            log.info("Last Attempt: {}", attempt);
            return attempt;
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
