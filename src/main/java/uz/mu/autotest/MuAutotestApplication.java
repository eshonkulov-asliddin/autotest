package uz.mu.autotest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class MuAutotestApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(MuAutotestApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(MuAutotestApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
