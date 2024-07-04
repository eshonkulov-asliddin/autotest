package uz.mu.autotest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.mu.autotest.client.GithubClient;
import uz.mu.autotest.dto.AttemptDto;
import uz.mu.autotest.dto.SubmitTaskRequest;
import uz.mu.autotest.dto.UserTakenLabDto;
import uz.mu.autotest.service.AuthService;
import uz.mu.autotest.service.LabService;
import uz.mu.autotest.service.UserService;
import uz.mu.autotest.service.UserTakenLabService;
import uz.mu.autotest.service.impl.GithubRepositoryService;
import uz.mu.autotest.service.impl.GithubSubmitTaskService;

import java.security.Principal;

@Controller
@RequestMapping("/github")
@RequiredArgsConstructor
@Slf4j
public class WebsocketController {

    private final LabService labService;
    private final UserService userService;
    private final UserTakenLabService userTakenLabService;
    private final GithubClient githubClient;
    private final AuthService authService;
    private final GithubSubmitTaskService githubSubmitTaskService;
    private final OAuth2AuthorizedClientService clientService;
    private final GithubRepositoryService githubRepositoryService;
    private final SimpMessagingTemplate simpMessagingTemplate;


    // TODO implement RabbitMQ interaction for submitTask
//    @MessageMapping("/submitTask")
//    public void submitTask(SubmitTaskRequest request, @AuthenticationPrincipal Principal principal, @Payload String username, @Headers Map<String, String> headers, SimpMessageHeaderAccessor headerAccessor) throws InterruptedException {
//        log.info("headers: {}", headers);
//        log.info("principalGetName: {}", principal.getName());
//        log.info("username: {}", username);
//        log.info("#############################TRIGGERED####################");
//        String sessionId = headers.get("simpSessionId");
//        log.info("sessionId: {}", sessionId);
//        // Check if the flag is set in the session attribute
//        if (headerAccessor.getSessionAttributes().get("connectionEstablished") == null) {
//            // Flag is not set, set it to true
//            headerAccessor.getSessionAttributes().put("connectionEstablished", true);
//
//            // Your existing logic here
//            Thread.sleep(1000);
//            OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) principal;
//            Thread.sleep(5000);
//            simpMessagingTemplate.convertAndSendToUser(principal.getName(), "/queue/attempts", new SubmitTaskRequest(1L));
//        }
//    }


    @MessageMapping("/submitTask")
    @SendToUser("/queue/attempts")
    public AttemptDto submitTask(SubmitTaskRequest request, @AuthenticationPrincipal Principal principal) throws InterruptedException {
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) principal;
        OAuth2User oAuth2User = oAuth2AuthenticationToken.getPrincipal();
        String login = oAuth2User.getAttribute("login").toString();
        OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId(), oAuth2AuthenticationToken.getName());
        String accessToken = "Bearer " + client.getAccessToken().getTokenValue();

        return githubSubmitTaskService.submitTask(request, login, accessToken);
    }

    @MessageMapping("/generateRepository")
    @SendToUser("/queue/repositories")
    public UserTakenLabDto generateRepository(SubmitTaskRequest request, @AuthenticationPrincipal Principal principal) {
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) principal;
        OAuth2User oAuth2User = oAuth2AuthenticationToken.getPrincipal();
        String login = oAuth2User.getAttribute("login").toString();
        OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId(), oAuth2AuthenticationToken.getName());
        String accessToken = "Bearer " + client.getAccessToken().getTokenValue();

        log.info("Principal: {}", principal);
        log.info("auth2User login: {}", login);
        log.info("access token: {}", accessToken);
        return githubRepositoryService.generateRepository(request, login, accessToken);
    }

}
