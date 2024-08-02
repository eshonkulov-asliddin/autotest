package uz.mu.autotest.extractor.python;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@XmlRootElement(name = "testsuites")
@XmlAccessorType(XmlAccessType.FIELD)
@ToString
@Getter
@Setter
public class TestSuites {

    @XmlElement(name = "testsuite")
    private List<PythonTestSuite> testsuite;

}

