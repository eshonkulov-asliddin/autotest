package uz.mu.autotest.utils;

import uz.mu.autotest.exception.GithubUrlParserException;
import uz.mu.autotest.extractor.Runtime;

public class GithubUrlParser {

    private static final String README_URL_PREFIX = "https://raw.githubusercontent.com";
    private static final String README_URL_POSTFIX = "/master/README.md";

    public static String extractRepoName(String url) {
        url = removeTrailingSlash(url);
        String[] parts = url.split("/");
        return parts[parts.length - 1];
    }

    public static String extractUsername(String url) {
        url = removeTrailingSlash(url);
        String[] parts = url.split("/");
        // Ensure that there's at least one part before accessing parts[-2]
        if (parts.length >= 2) {
            return parts[parts.length - 2];
        }
        throw new GithubUrlParserException("Failed to extract username from URL: " + url);
    }

    private static String removeTrailingSlash(String url) {
        return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
    }

    public static String generateReadmeUrl(String url) {
        return README_URL_PREFIX + "/" + extractUsername(url) + "/" + extractRepoName(url) + README_URL_POSTFIX;
    }

    public static Runtime getRuntime(String repoName) {
        if ("autotest-java-lab".equals(repoName)) {
            return Runtime.JAVA;
        }else{
            return Runtime.PYTHON;
        }
    }
}
