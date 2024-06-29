package uz.mu.autotest.extractor;

import java.io.IOException;

public interface ArtifactDownloader {
    void downloadArtifact(String url, String token, String destinationFolder, String fileName) throws IOException;
}
