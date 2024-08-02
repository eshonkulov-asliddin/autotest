package uz.mu.autotest.processor;

import jakarta.xml.bind.JAXBException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import uz.mu.autotest.client.GithubClient;
import uz.mu.autotest.extractor.Runtime;
import uz.mu.autotest.extractor.java.JavaTestSuite;
import uz.mu.autotest.extractor.python.PythonTestSuite;
import uz.mu.autotest.model.Attempt;
import uz.mu.autotest.model.JavaTestResults;
import uz.mu.autotest.model.PythonTestResults;
import uz.mu.autotest.model.StudentTakenLab;
import uz.mu.autotest.model.TestResults;
import uz.mu.autotest.service.impl.AttemptService;
import uz.mu.autotest.utils.GithubUrlParser;

import java.io.IOException;
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


    private TestResults processDependingOnRuntime(String downloadUrl, String accessToken, String destinationFolder, String zipFileName, String xmlFileName, Runtime runtime) throws JAXBException, IOException {
        TestResults testResults;
        switch (runtime) {
            case JAVA -> {
                JavaTestSuite javaTestSuite = javaArtifactProcessor.processArtifact(downloadUrl, accessToken, destinationFolder, zipFileName, xmlFileName);
                testResults = conversionService.convert(javaTestSuite, JavaTestResults.class);
                return testResults;
            }
            case PYTHON -> {
                PythonTestSuite pythonTestSuite = pythonArtifactProcessor.processArtifact(downloadUrl, accessToken, destinationFolder, zipFileName, xmlFileName);
                testResults = conversionService.convert(pythonTestSuite, PythonTestResults.class);
                return testResults;
            }
           default -> throw new UnsupportedOperationException(String.format("Runtime %s is not supported", runtime));

        }
    }

    @SneakyThrows
    public Attempt processTestResults(String owner, String repo, String accessToken, StudentTakenLab studentTakenLab, Long runId) {
        Optional<Attempt> lastAttempt = githubClient.getLastActionRunByRunId(owner, repo, accessToken, runId);
        Optional<String> downloadUrl = githubClient.getLastActionRunDownloadArtifactUrl(owner, repo, accessToken);
        Runtime runtime = GithubUrlParser.getRuntime(repo);

        TestResults testResults = processDependingOnRuntime(downloadUrl.get(), accessToken, destinationFolder, zipFileName, xmlFileName, runtime);
        testResults.getTestCaseEntities().forEach(testCaseEntity -> testCaseEntity.setTestResults(testResults));
        Attempt attempt = lastAttempt.get();
        attempt.setTestResults(testResults);
        testResults.setAttempt(attempt);
        attempt.setStudentTakenLab(studentTakenLab);
        attemptService.addAttempt(attempt);
        log.info("Successfully added new attempt {}", attempt);
        return attempt;
    }

}

