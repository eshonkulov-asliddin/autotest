package uz.mu.autotest.extractor.util;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@ToString
@Getter
@Setter
public class TestSuite {

    @XmlAttribute
    private String name;

    @XmlAttribute
    private int errors;

    @XmlAttribute
    private int failures;

    @XmlAttribute
    private int skipped;

    @XmlAttribute
    private int tests;

    @XmlAttribute
    private double time;

    @XmlAttribute
    private String timestamp;

    @XmlAttribute
    private String hostname;

    @XmlElement(name = "testcase")
    private List<TestCase> testCases;

}