package uz.mu.autotest.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "python_test_results")
@Getter
@Setter
public class PythonTestResults extends TestResults {

    private String timestamp;
    private String hostname;

}
