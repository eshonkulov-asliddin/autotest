package uz.mu.autotest.processor;

import jakarta.xml.bind.JAXBException;

import java.io.IOException;
import java.util.List;

public interface ArtifactProcessor<T> {
    List<T> processArtifact(String url, String token, String destinationFolder, String zipFileName, String xmlFileName) throws IOException, JAXBException;
}
