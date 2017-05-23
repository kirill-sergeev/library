package ua.nure.serhieiev.library.dao;

import ua.nure.serhieiev.library.model.Identified;
import ua.nure.serhieiev.library.service.util.Pagination;

import java.util.List;

public interface GenericDao<T extends Identified> {

    T getById(Integer id);

    void remove(Integer id);

    void save(T object);

    void update(T object);

    List<T> getAll();

    List<T> getRange(Pagination pagination);

    int count();

}
