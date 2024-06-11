package uz.mu.autotest.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uz.mu.autotest.controller.CloneRepoRequest;
import uz.mu.autotest.model.Attempt;
import uz.mu.autotest.model.Result;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GithubClient {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${github.api.url}")
    private final String githubApiUrl = "https://api.github.com";

    public ResponseEntity<String> triggerWorkflow(String owner, String repo, String accessToken) {

        HttpHeaders headers = getHeaders(accessToken);
        String body = "{\"ref\":\"master\"}";

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        String triggerWorkflowUrl = githubApiUrl + "/repos/" + owner + "/" + repo + "/actions/workflows/python-app.yml/dispatches";
        return restTemplate.exchange(triggerWorkflowUrl, HttpMethod.POST, entity, String.class);
    }

    public Optional<Attempt> getLastActionRun(String owner, String repo, String accessToken) {
        String actionsRunsUrl = githubApiUrl + "/repos/" + owner + "/" + repo + "/actions/runs";
        var headers = getHeaders(accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> actionsRunsResponse = restTemplate.exchange(actionsRunsUrl, HttpMethod.GET, entity, String.class);
        String bodyJSON = actionsRunsResponse.getBody();
        try {
            JsonNode lastWorkflowRun = objectMapper.readTree(bodyJSON).get("workflow_runs").get(0);
            int runNumber = lastWorkflowRun.get("run_number").intValue();
            String conclusion = lastWorkflowRun.get("conclusion").textValue();
            String detailsURL = lastWorkflowRun.get("html_url").textValue();
            Attempt attempt = new Attempt();
            attempt.setRunNumber((long) runNumber);
            attempt.setResult(conclusion.equals("failure") ? Result.FAILURE : Result.SUCCESS);
            attempt.setDetailsUrl(detailsURL);
            return Optional.of(attempt);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public ResponseEntity<String> generateRepository(String labOwner, String templateRepoOwner, String templateRepo, String description, boolean doIncludeAllBranch, boolean isPrivate, String accessToken) {
        HttpHeaders headers = getHeaders(accessToken);
        String requestBody = generateRepoRequestBody(labOwner, templateRepo, description, doIncludeAllBranch, isPrivate);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        String generateRepoUrl = githubApiUrl + "/repos/" + templateRepoOwner + "/" + templateRepo + "/generate";
        return restTemplate.exchange(generateRepoUrl, HttpMethod.POST, entity, String.class);
    }

    private String generateRepoRequestBody(String labOwner, String templateRepo, String description, boolean doIncludeAllBranch, boolean isPrivate) {
        CloneRepoRequest cloneRepoRequest = new CloneRepoRequest(labOwner, templateRepo, description, doIncludeAllBranch, isPrivate);
        String requestBody = "";
        try {
            requestBody = objectMapper.writeValueAsString(cloneRepoRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return requestBody;
    }

    private HttpHeaders getHeaders(String accessToken){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/vnd.github+json");
        headers.set("Authorization", accessToken);
        headers.set("X-GitHub-Api-Version", "2022-11-28");
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
