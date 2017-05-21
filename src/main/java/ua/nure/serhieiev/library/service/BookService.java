package ua.nure.serhieiev.library.service;

import ua.nure.serhieiev.library.dao.*;
import ua.nure.serhieiev.library.model.*;
import ua.nure.serhieiev.library.service.util.Pagination;

import java.util.*;

public final class BookService {

    public static Book save(Book book) {
        try (DaoFactory df = DaoFactory.getInstance();
             TransactionManager tm = df.getTransactionManager()) {
            BookDao bookDao = df.getBookDao();
            tm.start();
            try {
                bookDao.save(book);
            } catch (RuntimeException e) {
                tm.rollback();
                e.printStackTrace();
            }
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
        return book;
    }

    public static Book update(Book book) {
        try (DaoFactory df = DaoFactory.getInstance();
             TransactionManager tm = df.getTransactionManager()) {
            BookDao bookDao = df.getBookDao();
            tm.start();
            try {
                bookDao.update(book);
            } catch (RuntimeException e) {
                tm.rollback();
                e.printStackTrace();
            }
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
        return book;
    }

    public static void remove(Book book) {
        try (DaoFactory df = DaoFactory.getInstance();
             TransactionManager tm = df.getTransactionManager()) {
            BookDao bookDao = df.getBookDao();
            tm.start();
            try {
                bookDao.remove(book.getId());
            } catch (RuntimeException e) {
                tm.rollback();
                e.printStackTrace();
            }
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
    }

    public static Book getById(int bookId) {
        Book book;
        try (DaoFactory df = DaoFactory.getInstance()) {
            BookDao bookDao = df.getBookDao();
            book = bookDao.getById(bookId);
            fillNestedFields(df, Collections.singletonList(book));
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
        return book;
    }

    public static List<Book> getAll() {
        return getAll(false);
    }

    public static List<Book> getAll(boolean fetchType) {
        List<Book> books;
        try (DaoFactory df = DaoFactory.getInstance()) {
            BookDao bookDao = df.getBookDao();
            books = bookDao.getAll();

            if (fetchType) {
                fillNestedFields(df, books);
            }

        } catch (Exception e) {
            throw new ApplicationException(e);
        }
        return books;
    }

    public static Map<Integer, List<Book>> getRange(Pagination pagination) {
        return getRange(pagination, null);
    }

    public static Map<Integer, List<Book>> getRangeByAuthor(Pagination pagination, Author author) {
        return getRange(pagination, author);
    }

    public static Map<Integer, List<Book>> getRangeByGenre(Pagination pagination, Genre genre) {
        return getRange(pagination, genre);
    }

    public static Map<Integer, List<Book>> getRangeByPublisher(Pagination pagination, Publisher publisher) {
        return getRange(pagination, publisher);
    }

    private static Map<Integer, List<Book>> getRange(Pagination pagination, Identified object) {
        Map<Integer, List<Book>> bookMap = new HashMap<>(1);
        List<Book> books;
        Integer count;
        try (DaoFactory df = DaoFactory.getInstance()) {
            BookDao bookDao = df.getBookDao();
            if (object == null) {
                count = bookDao.count();
                checkPagination(pagination, count);
                books = bookDao.getRange(pagination);
            } else if (object instanceof Author) {
                count = bookDao.count((Author) object);
                checkPagination(pagination, count);
                books = bookDao.getRangeByAuthor((Author) object, pagination);
            } else if (object instanceof Genre) {
                count = bookDao.count((Genre) object);
                checkPagination(pagination, count);
                books = bookDao.getRangeByGenre((Genre) object, pagination);
            } else if (object instanceof Publisher) {
                count = bookDao.count((Publisher) object);
                checkPagination(pagination, count);
                books = bookDao.getRangeByPublisher((Publisher) object, pagination);
            } else {
                throw new IllegalArgumentException("Object must be an Author, Genre, Publisher or nothing.");
            }
            fillNestedFields(df, books);
            bookMap.put(count, books);
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
        return bookMap;
    }

    private static void checkPagination(Pagination pagination, Integer count) {
        List<String> fields = Arrays.asList("quantity", "available", "title", "isbn", "publication_date", "");
        if ((pagination.getOffset()) >= count) {
            throw new ApplicationException("Too high offset!");
        }
        if (!fields.contains(pagination.getSortBy())) {
            throw new ApplicationException("Bad sort field!");
        }
    }

    private static void fillNestedFields(DaoFactory df, List<Book> books) {
        AuthorDao authorDao = df.getAuthorDao();
        GenreDao genreDao = df.getGenreDao();
        PublisherDao publisherDao = df.getPublisherDao();

        for (Book book : books) {
            List<Author> authors = new ArrayList<>();
            for (Author author : book.getAuthors()) {
                authors.add(authorDao.getById(author.getId()));
            }
            book.setAuthors(authors);

            List<Genre> genres = new ArrayList<>();
            for (Genre genre : book.getGenres()) {
                genres.add(genreDao.getById(genre.getId()));
            }
            book.setGenres(genres);

            Publisher publisher = publisherDao.getById(book.getPublisher().getId());
            book.setPublisher(publisher);
        }
    }

    private BookService() {
    }

}
