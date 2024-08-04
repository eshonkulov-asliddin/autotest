package uz.mu.autotest.extractor.impl;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uz.mu.autotest.extractor.FileService;
import uz.mu.autotest.extractor.Parser;
import uz.mu.autotest.extractor.java.JavaTestSuite;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JavaTestResultParser implements Parser<JavaTestSuite> {

    private final FileService fileService;

    @Override
    public List<JavaTestSuite> parse(File file) throws JAXBException {
        List<JavaTestSuite> result = new ArrayList<>();

        if (file.exists() && file.isDirectory()) {
            File[] xmlFiles = file.listFiles((dir, name) -> name.endsWith(".xml"));

            if (xmlFiles != null && xmlFiles.length > 0) {
                for (File xmlFile : xmlFiles) {
                    log.info("Parsing XML file: " + xmlFile.getAbsolutePath());
                    JAXBContext context = JAXBContext.newInstance(JavaTestSuite.class);
                    Unmarshaller unmarshaller = context.createUnmarshaller();
                    JavaTestSuite testSuite = (JavaTestSuite) unmarshaller.unmarshal(new File(xmlFile.getAbsolutePath()));
                    result.add(testSuite);
                    fileService.deleteFile(xmlFile.getAbsolutePath());
                }
            } else {
                log.error("No XML files found in the directory.");
            }
        } else {
            log.error("The specified directory does not exist or is not a directory.");
        }

        return result;
    }
}
