package uz.mu.autotest.model;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "testcases")
@Getter
@Setter
public class TestCaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String classname;
    private String name;
    private double time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_results_id")
    private TestResults testResults;

    @Embedded
    private FailureEntity failureEntity;

}