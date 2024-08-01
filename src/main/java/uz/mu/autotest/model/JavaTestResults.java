package uz.mu.autotest.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "java_test_results")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JavaTestResults extends TestResults {

    private String version;

    @ElementCollection
    @CollectionTable(name = "java_test_properties", joinColumns = @JoinColumn(name = "java_test_results_id"))
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "property_name")),
            @AttributeOverride(name = "value", column = @Column(name = "property_value"))
    })
    private List<Property> properties;
}

@Embeddable
@Getter
@Setter
class Property {

    private String name;
    private String value;

}

