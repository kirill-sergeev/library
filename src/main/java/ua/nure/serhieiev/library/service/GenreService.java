package ua.nure.serhieiev.library.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.nure.serhieiev.library.dao.DaoFactory;
import ua.nure.serhieiev.library.dao.GenreDao;
import ua.nure.serhieiev.library.dao.NotFoundException;
import ua.nure.serhieiev.library.model.Pagination;
import ua.nure.serhieiev.library.model.entities.Genre;

import java.util.List;

public final class GenreService {

    private static final Logger logger = LoggerFactory.getLogger(GenreService.class);

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

    protected static void checkGenres(DaoFactory df, List<Genre> genres) {
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
                logger.info("Genre {} not fount.", bookGenre.getTitle());
            }
            if (bookGenre.getId() == null) {
                genreDao.save(bookGenre);
            }
        }
    }

    private GenreService() {
    }

}
