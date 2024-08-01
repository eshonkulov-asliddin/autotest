package uz.mu.autotest.extractor.python;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlValue;
import lombok.ToString;
import lombok.Getter;
import lombok.Setter;

@XmlAccessorType(XmlAccessType.FIELD)
@ToString
@Getter
@Setter
public class Failure {

    @XmlAttribute
    private String message;

    @XmlValue
    private String content;
}