package uz.mu.autotest.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.ConversionService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import uz.mu.autotest.dto.WorkflowResultsPayload;
import uz.mu.autotest.dto.attempt.AttemptDto;
import uz.mu.autotest.model.Attempt;
import uz.mu.autotest.publisher.impl.TestResultsPublisherImpl;
import uz.mu.autotest.service.UserSessionData;
import uz.mu.autotest.service.impl.InMemoryCacheService;
import uz.mu.autotest.utils.GithubUrlParser;

@Service
@RequiredArgsConstructor
public class WorkflowResultProcessor {

    @Value("${queue.attempts}")
    private String studentSubscribedQueue;

    private final ConversionService conversionService;
    private final InMemoryCacheService inMemoryCacheService;
    private final TestResultsProcessor testResultsProcessor;
    private final TestResultsPublisherImpl testResultsPublisherImpl;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ObjectMapper objectMapper;


    public void process(WorkflowResultsPayload testResults) {
        String owner = testResults.getRepository().getOwner();
        String url = testResults.getRepository().getName();
        long runId = testResults.getRunId();
        String repo = GithubUrlParser.extractRepoName(url);
        UserSessionData userSessionData = inMemoryCacheService.retrieve(owner);
        inMemoryCacheService.remove(owner);
        if (userSessionData == null || userSessionData.accessToken() == null || userSessionData.studentTakenLab() == null) {
            throw new IllegalArgumentException(String.format("User session is not valid: %s", userSessionData));
        }
        try {
            Attempt attempt = testResultsProcessor.processTestResults(owner, repo, userSessionData.accessToken(), userSessionData.studentTakenLab(), runId);
            AttemptDto attemptDto = conversionService.convert(attempt, AttemptDto.class);
            String body = objectMapper.writeValueAsString(attemptDto);
            testResultsPublisherImpl.publish(owner, studentSubscribedQueue, body);
        }catch (Exception e) {
            String errorMessage = String.format("{\"status\": \"error\", \"message\": \"%s\"}", e.getMessage());
            testResultsPublisherImpl.publish(owner, studentSubscribedQueue, errorMessage);


        }
    }


}
