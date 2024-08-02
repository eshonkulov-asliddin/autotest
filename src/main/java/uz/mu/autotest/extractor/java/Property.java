package uz.mu.autotest.extractor.java;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;


@XmlAccessorType(XmlAccessType.FIELD)
public class Property {
    @XmlAttribute
    private String name;
    @XmlAttribute
    private String value;

    // Getters and setters
}

