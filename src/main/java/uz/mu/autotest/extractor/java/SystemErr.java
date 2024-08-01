package uz.mu.autotest.extractor.java;

import jakarta.xml.bind.annotation.XmlAccessorType;

import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import lombok.Setter;

@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
public class SystemErr {
    @XmlValue
    private String message;
}