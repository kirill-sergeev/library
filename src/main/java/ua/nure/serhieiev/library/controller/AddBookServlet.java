package ua.nure.serhieiev.library.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.nure.serhieiev.library.controller.util.Alert;
import ua.nure.serhieiev.library.model.entities.Author;
import ua.nure.serhieiev.library.model.entities.Book;
import ua.nure.serhieiev.library.model.entities.Genre;
import ua.nure.serhieiev.library.model.entities.Publisher;
import ua.nure.serhieiev.library.service.ApplicationException;
import ua.nure.serhieiev.library.service.BookService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

@WebServlet(name = "AddBookServlet", urlPatterns = {"/new-book.do", "/change-book.do"})
public class AddBookServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(AddBookServlet.class);
    private static final String ADD_BOOK_PAGE = "/WEB-INF/jsp/new-book.jsp";
    private static final String CHANGE_BOOK_PAGE = "/WEB-INF/jsp/change-book.jsp";

    private Book parseInput(HttpServletRequest request) {
        Integer bookId = Integer.valueOf(request.getParameter("id"));
        String bookTitle = request.getParameter("bookTitle");
        Long isbn = Long.valueOf(request.getParameter("isbn"));
        Integer quantity = Integer.valueOf(request.getParameter("quantity"));
        Integer available = Integer.valueOf(request.getParameter("available"));
        LocalDate publicationDate = LocalDate.parse(request.getParameter("publicationDate"));
        String description = request.getParameter("description");
        String publisherTitle = request.getParameter("publisherTitle");

        Publisher publisher = new Publisher().setTitle(publisherTitle);

        ArrayList<Author> authors = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            String authorName = request.getParameter("authorName" + i);
            if (authorName != null && authorName.length() > 2) {
                authors.add(new Author().setName(authorName));
            }
        }

        ArrayList<Genre> genres = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            String genreTitle = request.getParameter("genreTitle" + i);
            if (genreTitle != null && genreTitle.length() > 2) {
                genres.add(new Genre().setTitle(genreTitle));
            }
        }

        return new Book()
                .setId(bookId)
                .setTitle(bookTitle)
                .setIsbn(isbn)
                .setQuantity(quantity)
                .setAvailable(available)
                .setDescription(description)
                .setPublicationDate(publicationDate).setPublisher(publisher)
                .setAuthors(authors)
                .setGenres(genres);
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Book book = parseInput(request);

        try {
            if (request.getServletPath().equals("/change-book.do")) {
                BookService.update(book);
                System.out.println(book);
            }else {
                BookService.save(book);
            }
            request.setAttribute("alert", Alert.BOOK_ADDED);
            LOG.info("Book {} successfully added", book.getTitle());
        }catch (ApplicationException e){
            request.setAttribute("alert", Alert.BOOK_NOT_ADDED);
            LOG.warn("Book not added", e);
        }
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getServletPath().equals("/change-book.do")){
            Integer bookId = Integer.valueOf(request.getParameter("id"));
            Book book = BookService.getById(bookId);
            request.setAttribute("book", book);
            request.getRequestDispatcher(CHANGE_BOOK_PAGE).forward(request, response);
        } else {
            request.getRequestDispatcher(ADD_BOOK_PAGE).forward(request, response);
        }
    }

}
