package ua.nure.serhieiev.library.service;

import ua.nure.serhieiev.library.dao.*;
import ua.nure.serhieiev.library.model.entities.*;
import ua.nure.serhieiev.library.model.Pagination;

import java.util.*;

public final class BookService {

    public static Book save(Book book) {
        try (DaoFactory df = DaoFactory.getInstance();
             TransactionManager tm = df.getTransactionManager()) {
            BookDao bookDao = df.getBookDao();

            tm.start();
            AuthorService.checkAuthors(df, book.getAuthors());
            GenreService.checkGenres(df, book.getGenres());
            PublisherService.checkPublisher(df, book.getPublisher());

            try {
                bookDao.save(book);
            } catch (RuntimeException e) {
                tm.rollback();
                throw new ApplicationException(e);
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
            AuthorService.checkAuthors(df, book.getAuthors());
            GenreService.checkGenres(df, book.getGenres());
            PublisherService.checkPublisher(df, book.getPublisher());

            try {
                bookDao.update(book);
            } catch (RuntimeException e) {
                tm.rollback();
                throw new ApplicationException(e);
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
                throw new ApplicationException(e);
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

    public static List<Book> getByTitle(String title) {
        if (title == null || title.trim().length() < 3){
            throw new ApplicationException("Title must be longer than 2 characters!");
        }
        List<Book> books;
        try (DaoFactory df = DaoFactory.getInstance()) {
            BookDao bookDao = df.getBookDao();
            books = bookDao.getByTitle(title);
            fillNestedFields(df, books);
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
        return books;
    }

    public static List<Book> getAll(Pagination pagination) {
        return getAll(pagination, null);
    }

    public static List<Book> getByAuthor(Pagination pagination, Author author) {
        return getAll(pagination, author);
    }

    public static List<Book> getByGenre(Pagination pagination, Genre genre) {
        return getAll(pagination, genre);
    }

    public static List<Book> getByPublisher(Pagination pagination, Publisher publisher) {
        return getAll(pagination, publisher);
    }

    private static List<Book> getAll(Pagination pagination, Identified object) {
        List<Book> books;

        try (DaoFactory df = DaoFactory.getInstance()) {
            BookDao bookDao = df.getBookDao();
            if (object == null) {
                books = bookDao.getAll(pagination);
            } else if (object instanceof Author) {
                books = bookDao.getByAuthor(pagination, object.getId());
            } else if (object instanceof Genre) {
                books = bookDao.getByGenre(pagination, object.getId());
            } else if (object instanceof Publisher) {
                books = bookDao.getByPublisher(pagination, object.getId());
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
