package ua.nure.serhieiev.library.dao;

import ua.nure.serhieiev.library.model.entities.Identified;
import ua.nure.serhieiev.library.model.Pagination;

import java.util.List;

public interface GenericDao<T extends Identified> {

    Integer count();

    T getById(Integer id);

    void remove(Integer id);

    void save(T object);

    void update(T object);

    List<T> getAll();

    List<T> getAll(Pagination pagination);

}
