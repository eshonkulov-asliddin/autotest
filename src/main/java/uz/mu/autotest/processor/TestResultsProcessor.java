package uz.mu.autotest.processor;

import jakarta.xml.bind.JAXBException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import uz.mu.autotest.client.GithubClient;
import uz.mu.autotest.extractor.Runtime;
import uz.mu.autotest.extractor.java.JavaTestSuite;
import uz.mu.autotest.extractor.python.PythonTestSuite;
import uz.mu.autotest.model.Attempt;
import uz.mu.autotest.model.JavaTestResults;
import uz.mu.autotest.model.LabStatus;
import uz.mu.autotest.model.PythonTestResults;
import uz.mu.autotest.model.StudentTakenLab;
import uz.mu.autotest.model.TestResults;
import uz.mu.autotest.processor.impl.JavaArtifactProcessor;
import uz.mu.autotest.processor.impl.PythonArtifactProcessor;
import uz.mu.autotest.service.impl.AttemptService;
import uz.mu.autotest.service.impl.StudentTakenLabService;
import uz.mu.autotest.utils.GithubUrlParser;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class TestResultsProcessor {

    @Value("${testResults.destinationFolder}")
    private String destinationFolder;
    @Value("${testResults.zipFileName}")
    private String zipFileName;
    @Value("${testResults.xmlFileName}")
    private String xmlFileName;

    private final GithubClient githubClient;
    private final JavaArtifactProcessor javaArtifactProcessor;
    private final PythonArtifactProcessor pythonArtifactProcessor;
    private final AttemptService attemptService;
    private final ConversionService conversionService;
    private final StudentTakenLabService studentTakenLabService;


    private List<? extends TestResults> processDependingOnRuntime(String downloadUrl, String accessToken, String destinationFolder, String zipFileName, String xmlFileName, Runtime runtime) throws JAXBException, IOException {
        List<? extends TestResults> testResults;
        switch (runtime) {
            case JAVA -> {
                List<JavaTestSuite> javaTestSuites = javaArtifactProcessor.processArtifact(downloadUrl, accessToken, destinationFolder, zipFileName, xmlFileName);
                testResults = javaTestSuites.stream()
                        .map(javaTestSuite -> conversionService.convert(javaTestSuite, JavaTestResults.class))
                        .toList();
                return testResults;
            }
            case PYTHON -> {
                List<PythonTestSuite> pythonTestSuites = pythonArtifactProcessor.processArtifact(downloadUrl, accessToken, destinationFolder, zipFileName, xmlFileName);
                testResults = pythonTestSuites.stream()
                        .map(pythonTestSuite -> conversionService.convert(pythonTestSuite, PythonTestResults.class))
                        .toList();
                return testResults;
            }
           default -> throw new UnsupportedOperationException(String.format("Runtime %s is not supported", runtime));

        }
    }

    @SneakyThrows
    @Transactional
    public Attempt processTestResults(String owner, String repo, String accessToken, StudentTakenLab studentTakenLab, Long runId) {
        Optional<Attempt> lastAttempt = githubClient.getLastActionRunByRunId(owner, repo, accessToken, runId);
        Optional<String> downloadUrl = githubClient.getLastActionRunDownloadArtifactUrl(owner, repo, accessToken);
        Runtime runtime = GithubUrlParser.getRuntime(repo);

        List<? extends TestResults> testResults = processDependingOnRuntime(downloadUrl.get(), accessToken, destinationFolder, zipFileName, xmlFileName, runtime);
        log.info("Test results: {}", testResults);
        Attempt attempt = lastAttempt.get();
        testResults.forEach(testResult -> {
            testResult.getTestCaseEntities().forEach(testCaseEntity -> testCaseEntity.setTestResults(testResult));
            attempt.addTestResults(testResult);
        });
        attempt.setStudentTakenLab(studentTakenLab);
        attemptService.addAttempt(attempt);
        log.info("Successfully added new attempt {}", attempt);
        updateLabStatusDependingOnResult(testResults, studentTakenLab);
        studentTakenLabService.save(studentTakenLab);
        return attempt;
    }

    void updateLabStatusDependingOnResult(List<? extends TestResults> testResults, StudentTakenLab studentTakenLab) {
        LabStatus status = LabStatus.COMPLETED;
        for (TestResults testResult : testResults) {
            if (testResult.getFailures() > 0 || testResult.getErrors() > 0) {
                status = LabStatus.STARTED;
                break;
            }
        }
        studentTakenLab.setStatus(status);
    }

}

