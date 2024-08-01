package uz.mu.autotest.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReadmeService {

    private final RestTemplate restTemplate;
    private final Parser parser;
    private final HtmlRenderer renderer;

    @Cacheable("readme")
    public String getReadmeHtml(String url) {
        log.info("Readme url: {}", url);
        String markdown = restTemplate.getForObject(url, String.class);
        String render = renderer.render(parser.parse(markdown));
        log.info("Retrieved readme from {}: {}", url, render);
        return render;
    }
}
