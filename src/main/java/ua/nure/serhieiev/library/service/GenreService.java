package ua.nure.serhieiev.library.service;

import ua.nure.serhieiev.library.dao.*;
import ua.nure.serhieiev.library.model.entities.Genre;
import ua.nure.serhieiev.library.model.Pagination;

import java.util.*;

public class GenreService {

    public static List<Genre> getAll() {
        List<Genre> genres;
        try (DaoFactory df = DaoFactory.getInstance()) {
            GenreDao genreDao = df.getGenreDao();
            genres = genreDao.getAll();
        } catch (NotFoundException e) {
            genres = Collections.emptyList();
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
        return genres;
    }

    public static Map<Integer, List<Genre>> getRange(Pagination pagination) {
        Map<Integer, List<Genre>> genresMap = new HashMap<>(1);
        List<Genre> genres;
        Integer count;
        try (DaoFactory df = DaoFactory.getInstance()) {
            GenreDao genreDao = df.getGenreDao();
            count = genreDao.count();
            checkPagination(pagination, count);
            genres = genreDao.getAll(pagination);
            genresMap.put(count, genres);
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
        return genresMap;
    }

    private static void checkPagination(Pagination pagination, Integer count) {
        if ((pagination.getOffset()) >= count) {
            throw new ApplicationException("Too high offset!");
        }
    }

    private GenreService() {
    }

}
