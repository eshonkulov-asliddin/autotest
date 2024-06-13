package uz.mu.autotest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.mu.autotest.client.GithubClient;
import uz.mu.autotest.model.Attempt;
import uz.mu.autotest.model.UserTakenLab;
import uz.mu.autotest.service.AuthService;
import uz.mu.autotest.service.GenerateRepositoryService;
import uz.mu.autotest.service.SubmitTaskService;
import uz.mu.autotest.service.LabService;
import uz.mu.autotest.service.UserService;
import uz.mu.autotest.service.UserTakenLabService;
import uz.mu.autotest.dto.SubmitTaskRequest;

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
    private final SubmitTaskService submitTaskService;
    private final OAuth2AuthorizedClientService clientService;
    private final GenerateRepositoryService generateRepositoryService;


    @MessageMapping("/submitTask")
    @SendToUser("/topic/attempts")
    public Attempt submitTask(SimpMessageHeaderAccessor accessor, SubmitTaskRequest request, @AuthenticationPrincipal Principal principal) {
        String simpSessionId = accessor.getSessionId();
        log.info("simpSessionId: {}", simpSessionId);
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) principal;
        OAuth2User oAuth2User = oAuth2AuthenticationToken.getPrincipal();
        String login = oAuth2User.getAttribute("login").toString();
        OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId(), oAuth2AuthenticationToken.getName());
        String accessToken = "Bearer " + client.getAccessToken().getTokenValue();

        log.info("Principal: {}", principal);
        log.info("auth2User login: {}", login);
        log.info("access token: {}", accessToken);

        return submitTaskService.submitTask(request, login, accessToken, simpSessionId);
    }

    @MessageMapping("/generateRepository")
    @SendToUser("/topic/repositories")
    public UserTakenLab generateRepository(SubmitTaskRequest request, @AuthenticationPrincipal Principal principal) {
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) principal;
        OAuth2User oAuth2User = oAuth2AuthenticationToken.getPrincipal();
        String login = oAuth2User.getAttribute("login").toString();
        OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId(), oAuth2AuthenticationToken.getName());
        String accessToken = "Bearer " + client.getAccessToken().getTokenValue();

        log.info("Principal: {}", principal);
        log.info("auth2User login: {}", login);
        log.info("access token: {}", accessToken);
        return generateRepositoryService.generateRepository(request, login, accessToken);
    }

}
