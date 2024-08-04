package uz.mu.autotest.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class Attempt {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private Long runNumber;
    private String detailsUrl;
    @Enumerated(EnumType.STRING)
    private Result result;
    @ManyToOne(cascade = CascadeType.MERGE)
    private StudentTakenLab studentTakenLab;

    @OneToMany(mappedBy = "attempt", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<TestResults> testResults = new ArrayList<>();

    public void addTestResults(TestResults testResults) {
        this.testResults.add(testResults);
        testResults.setAttempt(this);
    }

    @Override
    public String toString() {
        return "Attempt{" +
                "id=" + id +
                ", runNumber=" + runNumber +
                ", detailsUrl='" + detailsUrl + '\'' +
                ", result=" + result +
                '}';
    }
}
