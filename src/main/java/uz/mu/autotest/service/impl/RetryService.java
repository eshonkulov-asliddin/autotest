package uz.mu.autotest.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.ConversionService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import uz.mu.autotest.client.GithubClient;
import uz.mu.autotest.exception.GetLastActionRunException;
import uz.mu.autotest.extractor.util.TestSuite;
import uz.mu.autotest.extractor.util.TestSuites;
import uz.mu.autotest.model.Attempt;
import uz.mu.autotest.model.TestSuiteEntity;
import uz.mu.autotest.model.StudentTakenLab;
import uz.mu.autotest.processor.ArtifactProcessor;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class RetryService {

    @Value("${testResults.destinationFolder}")
    private String destinationFolder;
    @Value("${testResults.zipFileName}")
    private String zipFileName;
    @Value("${testResults.xmlFileName}")
    private String xmlFileName;

    private final GithubClient gitHubApiService;
    private final AttemptService attemptService;
    private final StudentTakenLabService studentTakenLabService;
    private final ArtifactProcessor artifactProcessor;
    private final ConversionService conversionService;

    @Async
    public CompletableFuture<Attempt> retryAction(String owner, String repo, String accessToken, StudentTakenLab studentTakenLab) {
        log.info("Started retry action....");
        final int maxAttempts = 5;
        int attempts = 0;
        long delayMillis = 30000;

        while (attempts < maxAttempts) {
            try {
                Thread.sleep(delayMillis);

                Optional<Attempt> lastAttempt = gitHubApiService.getLastActionRun(owner, repo, accessToken);
                Optional<String> downloadUrl = gitHubApiService.getLastActionRunDownloadArtifactUrl(owner, repo, accessToken);
                if (lastAttempt.isPresent() && downloadUrl.isPresent()) {
                    // artifact processor
                    TestSuites testSuites = artifactProcessor.processArtifact(downloadUrl.get(), accessToken, destinationFolder, zipFileName, xmlFileName);
                    List<TestSuite> testsuite = testSuites.getTestsuite();
                    TestSuite testSuite = testsuite.get(0);
                    log.info("TestSuite: {}", testSuite);
                    Attempt attempt = lastAttempt.get();
                    TestSuiteEntity testSuiteEntity = conversionService.convert(testSuite, TestSuiteEntity.class);
                    testSuiteEntity.getTestCases().forEach(testCaseEntity -> testCaseEntity.setTestSuite(testSuiteEntity));
                    attempt.setTestSuite(testSuiteEntity);
                    testSuiteEntity.setAttempt(attempt);
                    attempt.setStudentTakenLab(studentTakenLab);
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