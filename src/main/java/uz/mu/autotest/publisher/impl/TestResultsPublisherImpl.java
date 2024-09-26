package uz.mu.autotest.publisher.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import uz.mu.autotest.publisher.TestResultsPublisher;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestResultsPublisherImpl implements TestResultsPublisher<String> {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public void publish(String key, String topic, String body) {
        log.info("Publishing results to topic {} with key {}: {}",topic, key, body);
        simpMessagingTemplate.convertAndSendToUser(key, topic, body);
    }
}
