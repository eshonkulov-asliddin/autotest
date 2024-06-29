package uz.mu.autotest.processor;

import jakarta.xml.bind.JAXBException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import uz.mu.autotest.extractor.ArtifactDownloader;
import uz.mu.autotest.extractor.FileExtractor;
import uz.mu.autotest.extractor.FileService;
import uz.mu.autotest.extractor.Parser;
import uz.mu.autotest.extractor.util.TestSuites;

import java.io.File;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ArtifactProcessor {

    private final ArtifactDownloader downloader;
    private final FileExtractor extractor;
    private final Parser<TestSuites> parser;
    private final FileService fileService;

    public TestSuites processArtifact(String url, String token, String destinationFolder, String zipFileName, String xmlFileName) throws IOException, JAXBException {
            downloader.downloadArtifact(url, token, destinationFolder, zipFileName);
            extractor.extract(new File(destinationFolder, zipFileName).getAbsolutePath(), destinationFolder);
            TestSuites testSuites = parser.parse(new File(destinationFolder, xmlFileName));
            fileService.deleteFile(new File(destinationFolder, zipFileName).getAbsolutePath());
            fileService.deleteFile(new File(destinationFolder, xmlFileName).getAbsolutePath());
            return testSuites;
    }
}