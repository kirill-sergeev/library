package ua.nure.serhieiev.library.dao;

import ua.nure.serhieiev.library.model.Genre;
import ua.nure.serhieiev.library.service.util.Pagination;

import java.util.List;

public interface GenreDao extends GenericDao<Genre> {
    List<Genre> getRange(Pagination pagination);
}
