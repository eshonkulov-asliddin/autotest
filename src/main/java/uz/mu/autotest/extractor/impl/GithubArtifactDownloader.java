package uz.mu.autotest.extractor.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uz.mu.autotest.extractor.ArtifactDownloader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
@Slf4j
public class GithubArtifactDownloader implements ArtifactDownloader {

    @Override
    public void downloadArtifact(String url, String token, String destinationFolder, String fileName) throws IOException {
        URL urlObj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("accept", "application/vnd.github+json");
        connection.setRequestProperty("Authorization", token);
        connection.setInstanceFollowRedirects(false); // Disable automatic redirects

        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_MOVED_TEMP || responseCode == HttpURLConnection.HTTP_MOVED_PERM) {
            // Get the redirect URL from the Location header
            String downloadUrl = connection.getHeaderField("Location");
            if (downloadUrl != null && !downloadUrl.isEmpty()) {
                // Open a connection to the download URL
                URL download = new URL(downloadUrl);
                HttpURLConnection downloadConnection = (HttpURLConnection) download.openConnection();
                downloadConnection.setRequestMethod("GET");

                // Get the input stream from the connection
                try (InputStream inputStream = downloadConnection.getInputStream()) {
                    // Create a FileOutputStream to save the downloaded file
                    File outputFile = new File(destinationFolder, fileName);
                    try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                        // Write the inputStream to the outputStream
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                    }
                    log.info("Downloaded artifact saved to: " + outputFile.getAbsolutePath());
                }
            } else {
                log.warn("Failed to find download URL in response header.");
            }
        } else {
            log.warn("Failed to download artifact. HTTP status code: " + responseCode);
        }

        connection.disconnect();
    }
}
