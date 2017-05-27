package ua.nure.serhieiev.library.service;

import ua.nure.serhieiev.library.dao.*;
import ua.nure.serhieiev.library.model.entities.Genre;
import ua.nure.serhieiev.library.model.Pagination;

import java.util.*;

public final class GenreService {

    public static List<Genre> getAll(Pagination pagination) {
        List<Genre> genres;
        try (DaoFactory df = DaoFactory.getInstance()) {
            GenreDao genreDao = df.getGenreDao();
            genres = genreDao.getAll(pagination);
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
        return genres;
    }

    static void checkGenres(DaoFactory df, List<Genre> genres) {
        GenreDao genreDao = df.getGenreDao();
        for (Genre bookGenre : genres) {
            try {
                for (Genre withSameTitle : genreDao.getByTitle(bookGenre.getTitle())) {
                    if (bookGenre.getTitle().equalsIgnoreCase(withSameTitle.getTitle())) {
                        bookGenre.setId(withSameTitle.getId());
                        break;
                    }
                }
            } catch (NotFoundException e) {

            }
            if (bookGenre.getId() == null) {
                genreDao.save(bookGenre);
            }
        }
    }

    private GenreService() {
    }

}
