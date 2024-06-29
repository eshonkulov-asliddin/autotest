package uz.mu.autotest.extractor.impl;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.springframework.stereotype.Component;
import uz.mu.autotest.extractor.Parser;
import uz.mu.autotest.extractor.util.TestSuites;

import java.io.File;

@Component
public class TestResultsXmlParser implements Parser<TestSuites> {
    @Override
    public TestSuites parse(File file) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(TestSuites.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (TestSuites) unmarshaller.unmarshal(file);
    }
}