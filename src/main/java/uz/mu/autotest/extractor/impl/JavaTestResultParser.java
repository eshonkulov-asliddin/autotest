package uz.mu.autotest.extractor.impl;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.springframework.stereotype.Component;
import uz.mu.autotest.extractor.Parser;
import uz.mu.autotest.extractor.java.JavaTestSuite;

import java.io.File;

@Component
public class JavaTestResultParser implements Parser<JavaTestSuite> {
    @Override
    public JavaTestSuite parse(File file) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(JavaTestSuite.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (JavaTestSuite) unmarshaller.unmarshal(file);
    }
}
