package uz.mu.autotest.extractor.util;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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

    @XmlElement(name = "failure")
    private Failure failure;

}