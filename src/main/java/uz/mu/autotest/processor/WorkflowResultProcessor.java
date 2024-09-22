package uz.mu.autotest.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import uz.mu.autotest.dto.WorkflowResultsPayload;
import uz.mu.autotest.dto.attempt.AttemptDto;
import uz.mu.autotest.model.Attempt;
import uz.mu.autotest.publisher.impl.TestResultsPublisherImpl;
import uz.mu.autotest.service.UserSessionData;
import uz.mu.autotest.service.impl.InMemoryCacheService;
import uz.mu.autotest.utils.CacheUtil;
import uz.mu.autotest.utils.GithubUrlParser;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkflowResultProcessor {

    @Value("${queue.attempts}")
    private String studentSubscribedQueue;

    private final ConversionService conversionService;
    private final InMemoryCacheService inMemoryCacheService;
    private final TestResultsProcessor testResultsProcessor;
    private final TestResultsPublisherImpl testResultsPublisherImpl;
    private final ObjectMapper objectMapper;


    public void process(WorkflowResultsPayload testResults) {
        String owner = testResults.getRepository().getOwner();
        String url = testResults.getRepository().getName();
        long runId = testResults.getRunId();
        String repo = GithubUrlParser.extractRepoName(url);
        String key = CacheUtil.getUniqueKey(owner, repo);
        UserSessionData userSessionData = inMemoryCacheService.retrieve(key);
        log.info("user session by key {}: {}", key, userSessionData);
        inMemoryCacheService.remove(key);
        if (userSessionData == null || userSessionData.accessToken() == null || userSessionData.studentTakenLab() == null) {
            throw new IllegalArgumentException(String.format("User session is not valid: %s", userSessionData));
        }
        String studentSubscribedQueueFullPath = getStudentSubscribedQueueWithRepoName(repo);
        try {
            Attempt attempt = testResultsProcessor.processTestResults(owner, repo, userSessionData.accessToken(), userSessionData.studentTakenLab(), runId);
            AttemptDto attemptDto = conversionService.convert(attempt, AttemptDto.class);
            String body = objectMapper.writeValueAsString(attemptDto);
            testResultsPublisherImpl.publish(owner, studentSubscribedQueueFullPath, body);
        }catch (Exception e) {
            String errorMessage = String.format("{\"status\": \"error\", \"message\": \"%s\"}", e.getMessage());
            testResultsPublisherImpl.publish(owner, studentSubscribedQueueFullPath, errorMessage);


        }
    }

    private String getStudentSubscribedQueueWithRepoName(String repoName) {
        return String.format("%s/%s", studentSubscribedQueue, repoName);
    }

}
