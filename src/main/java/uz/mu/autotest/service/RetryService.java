package uz.mu.autotest.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import uz.mu.autotest.client.GithubClient;
import uz.mu.autotest.exception.GetLastActionRunException;
import uz.mu.autotest.model.Attempt;
import uz.mu.autotest.model.UserTakenLab;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class RetryService {
    private final GithubClient gitHubApiService;
    private final AttemptService attemptService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ObjectMapper objectMapper;

    @Async
    public CompletableFuture<Attempt> retryAction(String owner, String repo, String accessToken, UserTakenLab userTakenLab, String simpSessionId) {
        final int maxAttempts = 5;
        int attempts = 0;
        long delayMillis = 30000; // Initial delay of 1 second

        while (attempts < maxAttempts) {
            try {
                Thread.sleep(delayMillis);

                Optional<Attempt> lastAttempt = gitHubApiService.getLastActionRun(owner, repo, accessToken);
                if (lastAttempt.isPresent()) {
                    Attempt attempt = lastAttempt.get();
                    log.info("Last attempt {}", attempt);
                    attempt.setUserTakenLab(userTakenLab);
                    attemptService.addAttempt(attempt);
                    log.info("Successfully added new attempt {}", attempt);
                    return CompletableFuture.completedFuture(attempt);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                log.error("Error occurred during attempt: {}", e.getMessage());
            }
            attempts++;
        }

        throw new GetLastActionRunException("Failed to retrieve last attempt after " + maxAttempts + " attempts");
    }
}