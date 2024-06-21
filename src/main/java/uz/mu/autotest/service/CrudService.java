package uz.mu.autotest.service;

import java.util.List;
import java.util.Optional;

public interface CrudService<T, E, ID> {

    List<T> getAll();

    Optional<T> getById(ID id);

    void save(E entity);

    boolean deleteById(ID id);

}
