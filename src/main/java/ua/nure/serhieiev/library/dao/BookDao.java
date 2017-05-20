package ua.nure.serhieiev.library.dao;
import ua.nure.serhieiev.library.model.Author;
import ua.nure.serhieiev.library.model.Book;
import ua.nure.serhieiev.library.model.Genre;
import ua.nure.serhieiev.library.model.Publisher;
import ua.nure.serhieiev.library.service.util.Pagination;

import java.util.List;

public interface BookDao extends GenericDao<Book> {

    List<Book> getRange(Pagination pagination);

    List<Book> getRangeByAuthor(Author author, Pagination pagination);

    List<Book> getRangeByGenre(Genre genre, Pagination pagination);

    List<Book> getRangeByPublisher(Publisher publisher, Pagination pagination);

}
