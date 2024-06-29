package uz.mu.autotest.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.TableGenerator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class TestSuiteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "test_suite_gen")
    @TableGenerator(name = "test_suite_gen", table = "sequence_table", pkColumnName = "seq_name", valueColumnName = "seq_value", pkColumnValue = "test_suite_seq", allocationSize = 1)
    private Long id;
    private String name;
    private int errors;
    private int failures;
    private int skipped;
    private int tests;
    private double time;
    private String timestamp;
    private String hostname;

    @OneToMany(mappedBy = "testSuite", cascade = CascadeType.ALL)
    private List<TestCaseEntity> testCases;

    @OneToOne
    private Attempt attempt;
}

