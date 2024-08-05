package uz.mu.autotest.publisher;

public interface TestResultsPublisher<T> {
    void publish(String key, String topic, T testResults);
}
