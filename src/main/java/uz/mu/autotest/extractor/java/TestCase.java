package uz.mu.autotest.extractor.java;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Getter;
import lombok.Setter;

@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
public class TestCase {
    @XmlAttribute
    private String name;
    @XmlAttribute
    private String classname;
    @XmlAttribute
    private String time;

    @XmlElement(name = "error")
    private Error error;

    @XmlElement(name = "failure")
    private Failure failure;

    @XmlElement(name = "system-err")
    private SystemErr systemErr;

    // Getters and setters
}
