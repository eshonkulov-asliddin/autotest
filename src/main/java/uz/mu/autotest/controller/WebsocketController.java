package uz.mu.autotest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.mu.autotest.dto.GenerateRepositoryRequest;
import uz.mu.autotest.dto.StudentTakenLabDto;
import uz.mu.autotest.dto.SubmitTaskRequest;
import uz.mu.autotest.service.impl.GithubRepositoryService;
import uz.mu.autotest.service.impl.GithubSubmitTaskService;

import java.security.Principal;

@Controller
@RequestMapping("/github")
@RequiredArgsConstructor
@Slf4j
public class WebsocketController {

    private final GithubSubmitTaskService githubSubmitTaskService;
    private final OAuth2AuthorizedClientService clientService;
    private final GithubRepositoryService githubRepositoryService;


    //TODO implement RabbitMQ interaction for submitTask
    @MessageMapping("/submitTask")
    public void submitTask(SubmitTaskRequest request, @AuthenticationPrincipal Principal principal) {
        String login = principal.getName();
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) principal;
        OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId(), oAuth2AuthenticationToken.getName());
        String accessToken = "Bearer " + client.getAccessToken().getTokenValue();

        githubSubmitTaskService.submitTask(request, login, accessToken);
    }

// TODO previous solution
//    @MessageMapping("/submitTask")
//    @SendToUser("/queue/attempts")
//    public AttemptDto submitTask(SubmitTaskRequest request, @AuthenticationPrincipal Principal principal) throws InterruptedException {
//        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) principal;
//        OAuth2User oAuth2User = oAuth2AuthenticationToken.getPrincipal();
//        String login = oAuth2User.getAttribute("login").toString();
//        OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId(), oAuth2AuthenticationToken.getName());
//        String accessToken = "Bearer " + client.getAccessToken().getTokenValue();
//
//        return githubSubmitTaskService.submitTask(request, login, accessToken);
//    }

    @MessageMapping("/generateRepository")
    @SendToUser("/queue/repositories")
    public StudentTakenLabDto generateRepository(GenerateRepositoryRequest request, @AuthenticationPrincipal Principal principal) {
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) principal;
        OAuth2User oAuth2User = oAuth2AuthenticationToken.getPrincipal();
        String login = oAuth2User.getAttribute("login").toString();
        OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId(), oAuth2AuthenticationToken.getName());
        String accessToken = "Bearer " + client.getAccessToken().getTokenValue();

        return githubRepositoryService.generateRepository(request, login, accessToken);
    }

}
