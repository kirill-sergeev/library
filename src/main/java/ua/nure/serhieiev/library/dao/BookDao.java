package ua.nure.serhieiev.library.dao;
import ua.nure.serhieiev.library.model.entities.Book;
import ua.nure.serhieiev.library.model.Pagination;

import java.util.List;

public interface BookDao extends GenericDao<Book> {

    List<Book> getByTitle(String title);

    List<Book> getByAuthor(Pagination pagination, Integer authorId);

    List<Book> getByGenre(Pagination pagination, Integer genreId);

    List<Book> getByPublisher(Pagination pagination, Integer publisherId);

}
