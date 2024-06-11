package uz.mu.autotest.config;

import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public Parser parser() {
        return Parser.builder().build();
    }

    @Bean
    public HtmlRenderer renderer() {
        return HtmlRenderer.builder().build();
    }
}
