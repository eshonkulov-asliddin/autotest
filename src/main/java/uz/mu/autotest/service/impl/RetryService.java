//package uz.mu.autotest.service.impl;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.convert.ConversionService;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Service;
//import uz.mu.autotest.client.GithubClient;
//import uz.mu.autotest.exception.GetLastActionRunException;
//import uz.mu.autotest.extractor.Runtime;
//import uz.mu.autotest.model.Attempt;
//import uz.mu.autotest.model.JavaTestResults;
//import uz.mu.autotest.model.PythonTestResults;
//import uz.mu.autotest.model.TestResults;
//import uz.mu.autotest.model.StudentTakenLab;
//import uz.mu.autotest.processor.impl.JavaArtifactProcessor;
//import uz.mu.autotest.processor.impl.PythonArtifactProcessor;
//import uz.mu.autotest.utils.GithubUrlParser;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.concurrent.CompletableFuture;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class RetryService {
//
//    @Value("${testResults.destinationFolder}")
//    private String destinationFolder;
//    @Value("${testResults.zipFileName}")
//    private String zipFileName;
//    @Value("${testResults.xmlFileName}")
//    private String xmlFileName;
//
//    private final GithubClient gitHubApiService;
//    private final AttemptService attemptService;
//    private final StudentTakenLabService studentTakenLabService;
//    private final ConversionService conversionService;
//    private final JavaArtifactProcessor javaArtifactProcessor;
//    private final PythonArtifactProcessor pythonArtifactProcessor;
//
//    @Async
//    public CompletableFuture<Attempt> retryAction(String owner, String repo, String accessToken, StudentTakenLab studentTakenLab) {
//        log.info("Started retry action....");
//        final int maxAttempts = 5;
//        int attempts = 0;
//        long delayMillis = 30000;
//
//        while (attempts < maxAttempts) {
//            try {
//                Thread.sleep(delayMillis);
//
//                Optional<Attempt> lastAttempt = gitHubApiService.getLastActionRun(owner, repo, accessToken);
//                Optional<String> downloadUrl = gitHubApiService.getLastActionRunDownloadArtifactUrl(owner, repo, accessToken);
//                Runtime runtime = GithubUrlParser.getRuntime(repo);
//
//                if (lastAttempt.isPresent() && downloadUrl.isPresent()) {
//                    // artifact processor
//                    if (Runtime.JAVA == runtime) {
//                        var testSuite = javaArtifactProcessor.processArtifact(downloadUrl.get(), accessToken, destinationFolder, zipFileName, xmlFileName);
//                        log.info("TestSuite: {}", testSuite);
//                        Attempt attempt = lastAttempt.get();
//                        List<JavaTestResults> javaTestResults = testSuite.stream()
//                                .map(suite -> conversionService.convert(suite, JavaTestResults.class))
//                                .toList();
//
//                        javaTestResults.getTestCaseEntities().forEach(testCaseEntity -> testCaseEntity.setTestResults(javaTestResults));
//                        attempt.setTestResults(javaTestResults);
//                        javaTestResults.setAttempt(attempt);
//                        attempt.setStudentTakenLab(studentTakenLab);
//                        attemptService.addAttempt(attempt);
//                        log.info("Successfully added new attempt {}", attempt);
//                        return CompletableFuture.completedFuture(attempt);
//                    }else if (Runtime.PYTHON == runtime) {
//                        var testSuite = pythonArtifactProcessor.processArtifact(downloadUrl.get(), accessToken, destinationFolder, zipFileName, xmlFileName);
//                        log.info("TestSuite: {}", testSuite);
//                        Attempt attempt = lastAttempt.get();
//                        TestResults pythonTestResults = conversionService.convert(testSuite, PythonTestResults.class);
//                        pythonTestResults.getTestCaseEntities().forEach(testCaseEntity -> testCaseEntity.setTestResults(pythonTestResults));
//                        attempt.setTestResults(pythonTestResults);
//                        pythonTestResults.setAttempt(attempt);
//                        attempt.setStudentTakenLab(studentTakenLab);
//                        attemptService.addAttempt(attempt);
//                        log.info("Successfully added new attempt {}", attempt);
//                        return CompletableFuture.completedFuture(attempt);
//                    }else {
//                        throw new UnsupportedOperationException(String.format("Runtime %s is not supported", runtime));
//                    }
//
//
//                }
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            } catch (Exception e) {
//                log.error("Error occurred during attempt: {}", e.getMessage());
//            }
//            attempts++;
//        }
//
//        throw new GetLastActionRunException("Failed to retrieve last attempt after " + maxAttempts + " attempts");
//    }
//}