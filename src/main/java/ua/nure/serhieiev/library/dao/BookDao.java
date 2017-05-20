package ua.nure.serhieiev.library.dao;

import ua.nure.serhieiev.library.model.Book;

import java.util.List;

public interface BookDao extends GenericDao<Book> {

    Integer count();

    List<Book> getRange(int offset, int limit, String sortBy, boolean order);

}
