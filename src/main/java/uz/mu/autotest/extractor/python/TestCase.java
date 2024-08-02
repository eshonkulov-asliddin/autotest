package uz.mu.autotest.extractor.python;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import uz.mu.autotest.model.TestResults;

@XmlAccessorType(XmlAccessType.FIELD)
@ToString
@Getter
@Setter
public class TestCase {

    @XmlAttribute
    private String classname;

    @XmlAttribute
    private String name;

    @XmlAttribute
    private double time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_results_id")
    private TestResults testResults;

    @XmlElement(name = "failure")
    private Failure failure;

}