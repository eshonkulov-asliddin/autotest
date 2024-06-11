package uz.mu.autotest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import uz.mu.autotest.model.CustomOAuth2User;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final OAuth2AuthorizedClientService clientService;

    public String getAccessToken() {
        // Get user's GitHub token from ContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(oauthToken.getAuthorizedClientRegistrationId(), oauthToken.getName());
        return "Bearer " + client.getAccessToken().getTokenValue();
    }

    public String getCurrentUser() {
        // Logic to get current user
        CustomOAuth2User oAuth2User = (CustomOAuth2User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return oAuth2User.getLogin();
    }
}