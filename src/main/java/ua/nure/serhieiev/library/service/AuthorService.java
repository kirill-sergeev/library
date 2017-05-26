package ua.nure.serhieiev.library.service;

import ua.nure.serhieiev.library.dao.DaoFactory;
import ua.nure.serhieiev.library.dao.NotFoundException;
import ua.nure.serhieiev.library.dao.AuthorDao;
import ua.nure.serhieiev.library.model.entities.Author;
import ua.nure.serhieiev.library.model.Pagination;

import java.util.*;

public class AuthorService {

    public static List<Author> getAll() {
        List<Author> authors;
        try (DaoFactory df = DaoFactory.getInstance()) {
            AuthorDao authorDao = df.getAuthorDao();
            authors = authorDao.getAll();
        } catch (NotFoundException e) {
            authors = Collections.emptyList();
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
        return authors;
    }

    public static Map<Integer, List<Author>> getRange(Pagination pagination) {
        Map<Integer, List<Author>> authorsMap = new HashMap<>(1);
        List<Author> authors;
        Integer count;
        try (DaoFactory df = DaoFactory.getInstance()) {
            AuthorDao authorDao = df.getAuthorDao();
            count = authorDao.count();
            checkPagination(pagination, count);
            authors = authorDao.getAll(pagination);
            authorsMap.put(count, authors);
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
        return authorsMap;
    }

    private static void checkPagination(Pagination pagination, Integer count) {
        if ((pagination.getOffset()) >= count) {
            throw new ApplicationException("Too high offset!");
        }
    }

    private AuthorService() {
    }
    
}
