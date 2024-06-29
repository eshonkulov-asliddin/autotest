package uz.mu.autotest.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class FailureEntity {
    private String message;
    private String content;
    @Override
    public String toString() {
        return "Failure{" +
                ", message='" + message + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
