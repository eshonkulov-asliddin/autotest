package uz.mu.autotest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.mu.autotest.client.GithubClient;
import uz.mu.autotest.model.Attempt;
import uz.mu.autotest.model.Lab;
import uz.mu.autotest.model.LabStatus;
import uz.mu.autotest.model.UserTakenLab;
import uz.mu.autotest.service.AuthService;
import uz.mu.autotest.service.GithubTaskSubmissionService;
import uz.mu.autotest.service.LabService;
import uz.mu.autotest.service.UserService;
import uz.mu.autotest.service.UserTakenLabService;
import uz.mu.autotest.utils.GithubUrlParser;
import uz.mu.autotest.dto.SubmitTaskRequest;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/github")
@RequiredArgsConstructor
@Slf4j
public class GitHubController {

    private final LabService labService;
    private final UserService userService;
    private final UserTakenLabService userTakenLabService;
    private final GithubClient githubClient;
    private final AuthService authService;
    private final GithubTaskSubmissionService githubTaskSubmissionService;
    private final OAuth2AuthorizedClientService clientService;


    @MessageMapping("/submitTask")
    @SendToUser("/topic/attempts")
    public Attempt submitTask(SubmitTaskRequest request, @AuthenticationPrincipal Principal principal) {

        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) principal;
        OAuth2User oAuth2User = oAuth2AuthenticationToken.getPrincipal();
        String login = oAuth2User.getAttribute("login").toString();
        OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId(), oAuth2AuthenticationToken.getName());
        String accessToken = "Bearer " + client.getAccessToken().getTokenValue();

        log.info("Principal: {}", principal);
        log.info("auth2User login: {}", login);
        log.info("access token: {}", accessToken);

        return githubTaskSubmissionService.submitTask(request, login, accessToken);
    }


    @GetMapping("/generateRepository/{labId}")
    public String generateRepository(Model model, @PathVariable(name="labId") Long labId) throws JsonProcessingException {
        log.info("generate repository called");

        Optional<Lab> lab = labService.getById(labId);

        if (lab.isEmpty()) {
            throw new IllegalArgumentException("Lab doesn't exist");
        }

        String accessToken = authService.getAccessToken();
        String labOwner = authService.getCurrentUser();
        String repoUrl = lab.get().getGithubUrl();
        String templateRepoOwner = GithubUrlParser.extractUsername(repoUrl);
        String templateRepo = GithubUrlParser.extractRepoName(repoUrl);
        String description = "Lab " + lab.get().getId();
        boolean doIncludeAllBranch = false;
        boolean isPrivate = false;

        ResponseEntity<String> genereateRepositoryResponse = githubClient.generateRepository(labOwner, templateRepoOwner, templateRepo, description, doIncludeAllBranch, isPrivate, accessToken);
        log.info("Generate repository response: {}", genereateRepositoryResponse);

        if (genereateRepositoryResponse.getStatusCode() == HttpStatus.CREATED) {
            String labGithubUrl = "https://github.com" + "/" + labOwner + "/" + templateRepo;
            UserTakenLab userTakenLab = new UserTakenLab();
            userTakenLab.setGithubUrl(labGithubUrl);
            userTakenLab.setStatus(LabStatus.STARTED);
            userTakenLab.setCourse(lab.get().getCourse());
            userTakenLab.setLab(lab.get());
            userTakenLab.setUser(userService.getByUsername(labOwner).get());
            userTakenLabService.save(userTakenLab);

            log.info("Insert new Lab: {}", userTakenLab);

            // Redirect user to the lab HTML page with a success message
            model.addAttribute("startedLab", userTakenLab);

            return "continue-lab.html";
        } else {
            // Redirect user to the lab HTML page with an error message
            model.addAttribute("error", "Failed to create repository. Please try again.");
            return "start-lab.html";
        }
    }

}
