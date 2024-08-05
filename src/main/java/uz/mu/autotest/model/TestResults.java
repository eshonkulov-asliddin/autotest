package uz.mu.autotest.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "test_results")
@Getter
@Setter
public abstract class TestResults {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String name;
    private int errors;
    private int failures;
    private int skipped;
    private int tests;
    private double time;

    @OneToMany(mappedBy = "testResults", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<TestCaseEntity> testCaseEntities;

    @ManyToOne
    private Attempt attempt;

}
