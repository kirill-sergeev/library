package ua.nure.serhieiev.library.dao;

import ua.nure.serhieiev.library.model.entities.Author;

import java.util.List;

public interface AuthorDao extends GenericDao<Author>{

    List<Author> getByName(String name);

}
