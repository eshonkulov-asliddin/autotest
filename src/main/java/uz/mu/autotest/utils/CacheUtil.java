package uz.mu.autotest.utils;

public class CacheUtil {

    public static String getUniqueKey(String owner, String repoName) {
        return String.format("%s#%s", owner, repoName);
    }
}
