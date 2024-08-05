package uz.mu.autotest.service.impl;

import org.springframework.stereotype.Service;
import uz.mu.autotest.service.CacheService;
import uz.mu.autotest.service.UserSessionData;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class InMemoryCacheService implements CacheService<String, UserSessionData> {

    private final ConcurrentHashMap<String, UserSessionData> cache = new ConcurrentHashMap<>();

    @Override
    public void store(String key, UserSessionData value) {
        cache.put(key, value);
    }

    @Override
    public UserSessionData retrieve(String key) {
        return cache.get(key);
    }

    @Override
    public void remove(String key) {
        cache.remove(key);
    }
}
