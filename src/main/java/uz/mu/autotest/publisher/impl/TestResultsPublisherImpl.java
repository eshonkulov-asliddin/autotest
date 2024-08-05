package uz.mu.autotest.publisher.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import uz.mu.autotest.publisher.TestResultsPublisher;

@Service
@RequiredArgsConstructor
public class TestResultsPublisherImpl implements TestResultsPublisher<String> {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public void publish(String key, String topic, String body) {
        simpMessagingTemplate.convertAndSendToUser(key, topic, body);
    }
}
