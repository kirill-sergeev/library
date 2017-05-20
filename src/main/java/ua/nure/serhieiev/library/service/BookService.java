package ua.nure.serhieiev.library.service;

import ua.nure.serhieiev.library.dao.*;
import ua.nure.serhieiev.library.model.*;
import ua.nure.serhieiev.library.service.util.Pagination;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class BookService {

    private static int bookCount;

    public static int getBookCount() {
        if (bookCount == 0) {
            try (DaoFactory df = DaoFactory.getInstance()) {
                BookDao bookDao = df.getBookDao();
                bookCount = bookDao.count();
            } catch (Exception e) {
                throw new ApplicationException(e);
            }
        }
        return bookCount;
    }

    public static Book save(Book book) {
        try (DaoFactory df = DaoFactory.getInstance();
             TransactionManager tm = df.getTransactionManager()) {
            BookDao bookDao = df.getBookDao();
            tm.start();
            try {
                bookDao.save(book);
                bookCount++;
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
                bookCount--;
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

    public static List<Book> getRange(Pagination pagination) {
        return getRange(pagination, null);
    }

    public static List<Book> getRangeByAuthor(Pagination pagination, Author author) {
        return getRange(pagination, author);
    }

    public static List<Book> getRangeByGenre(Pagination pagination, Genre genre) {
        return getRange(pagination, genre);
    }

    public static List<Book> getRangeByPublisher(Pagination pagination, Publisher publisher) {
        return getRange(pagination, publisher);
    }

    private static List<Book> getRange(Pagination pagination, Identified object) {
        List<Book> books;
        try (DaoFactory df = DaoFactory.getInstance()) {
            BookDao bookDao = df.getBookDao();
            if ((pagination.getOffset()) >= getBookCount()) {
                throw new ApplicationException("Too high offset!");
            }
            if (object == null) {
                books = bookDao.getRange(pagination);
            } else if (object instanceof Author) {
                books = bookDao.getRangeByAuthor((Author) object, pagination);
            } else if (object instanceof Genre) {
                books = bookDao.getRangeByGenre((Genre) object, pagination);
            } else if (object instanceof Publisher) {
                books = bookDao.getRangeByPublisher((Publisher) object, pagination);
            } else {
                throw new IllegalArgumentException("Object must be an Author, Genre, Publisher or nothing.");
            }
            fillNestedFields(df, books);
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
        return books;
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
