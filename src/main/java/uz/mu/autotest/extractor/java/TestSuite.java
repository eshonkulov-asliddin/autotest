package uz.mu.autotest.extractor.java;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;
import uz.mu.autotest.extractor.AbstractTestSuite;

import java.util.List;

@XmlRootElement(name = "testsuite")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
public class TestSuite extends AbstractTestSuite {
    @XmlAttribute
    private String name;
    @XmlAttribute
    private String time;
    @XmlAttribute
    private int tests;
    @XmlAttribute
    private int errors;
    @XmlAttribute
    private int skipped;
    @XmlAttribute
    private int failures;

    @XmlElementWrapper(name = "properties")
    @XmlElement(name = "property")
    private List<Property> properties;

    @XmlElement(name = "testcase")
    private List<TestCase> testCases;

    // Getters and setters
}

