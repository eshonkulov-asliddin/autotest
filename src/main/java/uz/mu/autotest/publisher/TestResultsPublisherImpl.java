package uz.mu.autotest.publisher;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import uz.mu.autotest.dto.AttemptDto;

@Service
@RequiredArgsConstructor
public class TestResultsPublisherImpl implements TestResultsPublisher<AttemptDto> {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public void publish(String key, String topic, AttemptDto testResults) {
        simpMessagingTemplate.convertAndSendToUser(key, topic, testResults);
    }
}
