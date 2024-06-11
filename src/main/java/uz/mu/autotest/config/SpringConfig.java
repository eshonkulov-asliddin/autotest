package uz.mu.autotest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@PropertySource("classpath:application.yml")
@EnableWebSecurity
public class SpringConfig {

    @Autowired
    CustomOAuth2UserService customOAuth2UserService;
    @Autowired
    OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
        security
                .authorizeHttpRequests(auth ->
                       auth
                            .requestMatchers("/login", "/app.js").permitAll()
                            .requestMatchers("/mu-autotest").permitAll()
                            .anyRequest().authenticated()
                    )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .successHandler(oAuth2LoginSuccessHandler)
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        ));
        return security.build();
    }

}
