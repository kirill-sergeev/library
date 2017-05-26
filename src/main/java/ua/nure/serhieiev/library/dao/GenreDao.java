package ua.nure.serhieiev.library.dao;

import ua.nure.serhieiev.library.model.entities.Genre;

import java.util.List;

public interface GenreDao extends GenericDao<Genre> {

    List<Genre> getByTitle(String title);

}
