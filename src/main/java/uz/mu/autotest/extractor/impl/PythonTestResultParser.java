package uz.mu.autotest.extractor.impl;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.springframework.stereotype.Component;
import uz.mu.autotest.extractor.Parser;
import uz.mu.autotest.extractor.python.TestSuite;
import uz.mu.autotest.extractor.python.TestSuites;

import java.io.File;

@Component
public class PythonTestResultParser implements Parser<TestSuite> {
    @Override
    public TestSuite parse(File file) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(TestSuites.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        TestSuites testSuites = (TestSuites) unmarshaller.unmarshal(file);
        return testSuites.getTestsuite().get(0);
    }
}
