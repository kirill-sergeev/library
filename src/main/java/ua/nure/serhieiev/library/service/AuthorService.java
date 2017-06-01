package ua.nure.serhieiev.library.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.nure.serhieiev.library.dao.AuthorDao;
import ua.nure.serhieiev.library.dao.DaoFactory;
import ua.nure.serhieiev.library.dao.NotFoundException;
import ua.nure.serhieiev.library.model.Pagination;
import ua.nure.serhieiev.library.model.entities.Author;

import java.util.List;

public final class AuthorService {

    private static final Logger logger = LoggerFactory.getLogger(AuthorService.class);

    public static List<Author> getAll(Pagination pagination) {
        List<Author> authors;
        try (DaoFactory df = DaoFactory.getInstance()) {
            AuthorDao authorDao = df.getAuthorDao();
            authors = authorDao.getAll(pagination);
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
        return authors;
    }

    public static List<Author> getByName(String name) {
        if (name == null || name.trim().length() < 3){
            throw new ApplicationException("Name must be longer than 2 characters!");
        }
        List<Author> authors;
        try (DaoFactory df = DaoFactory.getInstance()) {
            AuthorDao authorDao = df.getAuthorDao();
            authors = authorDao.getByName(name);
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
        return authors;
    }

    protected static void checkAuthors(DaoFactory df, List<Author> authors){
        AuthorDao authorDao = df.getAuthorDao();
        for (Author bookAuthor : authors) {
            try {
                for (Author withSameName : authorDao.getByName(bookAuthor.getName())) {
                    if (bookAuthor.getName().equalsIgnoreCase(withSameName.getName())) {
                        bookAuthor.setId(withSameName.getId());
                        break;
                    }
                }
            } catch (NotFoundException e) {
                logger.info("Author {} not fount.", bookAuthor.getName());
            }
            if (bookAuthor.getId() == null) {
                authorDao.save(bookAuthor);
            }
        }
    }

    private AuthorService() {
    }
    
}
