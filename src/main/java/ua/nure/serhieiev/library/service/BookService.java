package ua.nure.serhieiev.library.service;

import ua.nure.serhieiev.library.dao.*;
import ua.nure.serhieiev.library.model.Author;
import ua.nure.serhieiev.library.model.Book;
import ua.nure.serhieiev.library.model.Genre;
import ua.nure.serhieiev.library.model.Publisher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class BookService {

    public static Book save(Book book) {
        try (DaoFactory df = DaoFactory.getInstance();
             TransactionManager tm = df.getTransactionManager()) {
            BookDao bookDao = df.getBookDao();
            tm.start();
            try {
                bookDao.save(book);
            } catch (Exception e) {
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
            } catch (Exception e) {
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
            } catch (Exception e) {
                tm.rollback();
                e.printStackTrace();
            }
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
    }

    public static Book getById(int bookId) {
        return getById(bookId, false);
    }

    public static Book getById(int bookId, boolean fetchType) {
        Book book;
        try (DaoFactory df = DaoFactory.getInstance()) {
            BookDao bookDao = df.getBookDao();
            book = bookDao.getById(bookId);

            if (fetchType) {
                fillNestedFields(df, Collections.singletonList(book));
            }
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
