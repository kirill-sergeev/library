package ua.nure.serhieiev.library.dao;

import ua.nure.serhieiev.library.model.Author;
import ua.nure.serhieiev.library.service.util.Pagination;

import java.util.List;

public interface AuthorDao extends GenericDao<Author>{
    List<Author> getRange(Pagination pagination);
}
