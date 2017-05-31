package ua.nure.serhieiev.library;

import ua.nure.serhieiev.library.dao.*;
import ua.nure.serhieiev.library.model.entities.Author;
import ua.nure.serhieiev.library.model.entities.Book;
import ua.nure.serhieiev.library.model.entities.Genre;
import ua.nure.serhieiev.library.model.entities.Publisher;
import ua.nure.serhieiev.library.service.ApplicationException;
import ua.nure.serhieiev.library.service.util.PasswordAuthentication;


import java.time.LocalDate;
import java.util.Arrays;

public class Main {

    public static void main1(String[] args) {
        Genre genre = new Genre().setTitle("Modernon");
        Genre genre2 = new Genre().setTitle("Fantasynon");

        Author author = new Author().setName("Dostoevsky");
        Author author2 = new Author().setName("Ivanov");

        Publisher publisher = new Publisher().setTitle("MNO");

        Book book = new Book()
                .setIsbn(12365329L)
                .setTitle("Bible")
                .setQuantity(5)
                .setDescription("Bad book")
                .setPublicationDate(LocalDate.now())
                .setPublisher(publisher)
                .setAuthors(Arrays.asList(author, author2))
                .setGenres(Arrays.asList(genre, genre2));


        try (DaoFactory df = DaoFactory.getInstance();
             TransactionManager tm = df.getTransactionManager()) {
            BookDao bookDao = df.getBookDao();
            GenreDao genreDao = df.getGenreDao();
            AuthorDao authorDao = df.getAuthorDao();
            PublisherDao publisherDao = df.getPublisherDao();
            tm.start();
            try {
                publisherDao.save(publisher);
                genreDao.save(genre);
                genreDao.save(genre2);
                authorDao.save(author);
                authorDao.save(author2);
                bookDao.save(book);
            } catch (Exception e) {
                tm.rollback();
                e.printStackTrace();
            }
        } catch (Exception e) {
            throw new ApplicationException(e);
        }

        try (DaoFactory df = DaoFactory.getInstance()
        ) {
            BookDao bookDao = df.getBookDao();

        } catch (Exception e) {
            throw new ApplicationException(e);
        }
    }

    public static void main(String[] args) {
        PasswordAuthentication pa = new PasswordAuthentication();
        String hash = pa.hash("111111".toCharArray());
        System.out.println(hash);
        System.out.println(pa.authenticate("123456".toCharArray(), hash));
    }
}
