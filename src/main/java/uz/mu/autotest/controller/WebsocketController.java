package uz.mu.autotest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.mu.autotest.dto.lab.SubmitTaskRequest;
import uz.mu.autotest.dto.repo.GenerateRepositoryRequest;
import uz.mu.autotest.dto.student.StudentTakenLabDto;
import uz.mu.autotest.service.impl.GithubRepositoryService;
import uz.mu.autotest.service.impl.GithubSubmitTaskService;

import java.security.Principal;

@Controller
@RequestMapping("/github")
@RequiredArgsConstructor
@Slf4j
public class WebsocketController {

    @Value("${queue.repositories}")
    private String studentSubscribedQueue;

    private final GithubSubmitTaskService githubSubmitTaskService;
    private final OAuth2AuthorizedClientService clientService;
    private final GithubRepositoryService githubRepositoryService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    //TODO implement RabbitMQ interaction for submitTask
    @MessageMapping("/submitTask")
    public void submitTask(SubmitTaskRequest request, @AuthenticationPrincipal Principal principal) {
        String login = principal.getName();
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) principal;
        OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId(), oAuth2AuthenticationToken.getName());
        String accessToken = "Bearer " + client.getAccessToken().getTokenValue();

        githubSubmitTaskService.submitTask(request, login, accessToken);
    }

    @MessageMapping("/generateRepository")
    public void generateRepository(GenerateRepositoryRequest request, @AuthenticationPrincipal Principal principal) {
        String login = principal.getName();
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) principal;
        OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId(), oAuth2AuthenticationToken.getName());
        String accessToken = "Bearer " + client.getAccessToken().getTokenValue();

        try {
            StudentTakenLabDto studentTakenLabDto = githubRepositoryService.generateRepository(request, login, accessToken);
            simpMessagingTemplate.convertAndSendToUser(login, studentSubscribedQueue, studentTakenLabDto);
        }catch (Exception e) {
            String errorMessage = String.format("{\"status\": \"error\", \"message\": \"%s\"}", e.getMessage());
            simpMessagingTemplate.convertAndSendToUser(login, studentSubscribedQueue, errorMessage);
        }
    }

}
