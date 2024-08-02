package uz.mu.autotest.service;

public interface CacheService<K,V> {
    void store(K key, V value);
    V retrieve(K key);
    void remove(K key);
}

