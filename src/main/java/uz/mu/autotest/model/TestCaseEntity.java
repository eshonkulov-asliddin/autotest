package uz.mu.autotest.model;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.TableGenerator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Setter
@Getter
public class TestCaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "test_case_gen")
    @TableGenerator(name = "test_case_gen", table = "sequence_table", pkColumnName = "seq_name", valueColumnName = "seq_value", pkColumnValue = "test_case_seq", allocationSize = 1)
    private Long id;
    private String name;
    private String classname;
    private double time;
    @Embedded
    private FailureEntity failure;

    @ManyToOne
    private TestSuiteEntity testSuite;
}
