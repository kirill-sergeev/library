package ua.nure.serhieiev.library;

import ua.nure.serhieiev.library.dao.*;
import ua.nure.serhieiev.library.model.entities.Author;
import ua.nure.serhieiev.library.model.entities.Book;
import ua.nure.serhieiev.library.model.entities.Genre;
import ua.nure.serhieiev.library.model.entities.Publisher;
import ua.nure.serhieiev.library.service.ApplicationException;
import ua.nure.serhieiev.library.service.BookService;

import java.time.LocalDate;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
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
        }

/*        try (DaoFactory df = DaoFactory.getInstance()
         ) {
            BookDao bookDao = df.getBookDao();

        } catch (Exception e) {
            throw new ApplicationException(e);
        }*/




     /*   Pagination pagination = new Pagination().setAscending(true).setLimit(10).setOffset(0).setSortBy("id");
        System.out.println(BookService.getByAuthor(new Author().setId(10),pagination));*/

       // System.out.println(BookService.getAll(1, BookService.Field.TITLE, true, 2));
    }

    public static void main2(String[] args) {
        long start = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            System.out.println(BookService.getAll(true).size());
        }
        long finish = System.nanoTime();
        System.out.println((finish-start)/100000000);

       /* List<String> fields = new ArrayList<>();
        for (Field field : Book.class.getDeclaredFields()){
                fields.add(field.getName().replaceAll("(.)(\\p{Upper})", "$1_$2").toLowerCase());
        }
        System.out.println(fields);*/
    }

}
