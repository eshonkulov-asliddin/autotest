package uz.mu.autotest.extractor;

import java.io.IOException;

public interface FileExtractor {
    void extract(String zipFilePath, String outputDir) throws IOException;
}
