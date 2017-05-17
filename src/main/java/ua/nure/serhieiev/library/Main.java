package ua.nure.serhieiev.library;

import ua.nure.serhieiev.library.dao.*;
import ua.nure.serhieiev.library.model.*;
import ua.nure.serhieiev.library.service.ApplicationException;
import ua.nure.serhieiev.library.service.BookService;

import java.time.LocalDate;
import java.util.Arrays;

public class Main {

    public static void main1(String[] args) {
       /* Genre genre = new Genre().setTitle("Horror");
        Genre genre2 = new Genre().setTitle("Classical");

        Author author = new Author().setName("Pushkin");
        Author author2 = new Author().setName("Gogol");

        Publisher publisher = new Publisher().setTitle("UKA");

        Book book = new Book()
                .setIsbn(123456789L)
                .setTitle("Cobzar")
                .setQuantity(10)
                .setAvailable(10)
                .setDescription("Good book")
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
            try{
                publisherDao.save(publisher);
                genreDao.save(genre);
                genreDao.save(genre2);
                authorDao.save(author);
                authorDao.save(author2);
                bookDao.save(book);
            }catch (Exception e){
                tm.rollback();
                e.printStackTrace();
            }
        } catch (Exception e) {
            throw new ApplicationException(e);
        }*/

  /*      try (DaoFactory df = DaoFactory.getInstance()
         ) {
            BookDao bookDao = df.getBookDao();
            System.out.println(bookDao.getAll());
        } catch (Exception e) {
            throw new ApplicationException(e);
        }*/
    }

    public static void main(String[] args) {
        long start = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            System.out.println(BookService.getAll());
        }
        long finish = System.nanoTime();
        System.out.println((finish-start)/100000000);
    }

}
