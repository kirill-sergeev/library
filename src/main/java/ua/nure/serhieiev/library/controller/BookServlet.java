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

import static ua.nure.serhieiev.library.controller.util.Action.Constants.*;

@WebServlet(name = "BookServlet", urlPatterns = {NEW_BOOK_ACTION, CHANGE_BOOK_ACTION})
public class BookServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(BookServlet.class);
    private static final String NEW_BOOK_PAGE = "/WEB-INF/jsp/new-book.jsp";
    private static final String CHANGE_BOOK_PAGE = "/WEB-INF/jsp/change-book.jsp";
    private static final String ALERT = "alert";

    private Book parseInput(HttpServletRequest request) {
        String bookTitle = request.getParameter("bookTitle");
        Integer quantity = Integer.valueOf(request.getParameter("quantity"));
        LocalDate publicationDate = LocalDate.parse(request.getParameter("publicationDate"));
        String description = request.getParameter("description");
        String publisherTitle = request.getParameter("publisherTitle");

        Integer bookId = null;
        if (request.getParameterMap().containsKey("id")) {
            bookId = Integer.valueOf(request.getParameter("id"));
        }
        Integer available = null;
        if (request.getParameterMap().containsKey("available")) {
            available = Integer.valueOf(request.getParameter("available"));
        }
        if (available != null && available > quantity) {
            request.setAttribute(ALERT, Alert.AVAILABLE_MORE_THAN_QUANTITY);
            throw new ApplicationException("Available > quantity!");
        }

        String isbnParam = request.getParameter("isbn");
        Long isbn = null;
        if (isbnParam != null && !isbnParam.isEmpty()) {
            isbn = Long.valueOf(isbnParam);
        }

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

    private void getBook(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            Integer bookId = Integer.valueOf(request.getParameter("id"));
            Book book = BookService.getById(bookId);
            request.setAttribute("book", book);
        } catch (RuntimeException e) {
            logger.info("Book not found");
            response.sendRedirect(BOOKS_ACTION);
            return;
        }
        request.getRequestDispatcher(CHANGE_BOOK_PAGE).forward(request, response);
    }

    private void addBook(HttpServletRequest request) {
        try {
            Book book = parseInput(request);
            BookService.save(book);
            request.setAttribute(ALERT, Alert.BOOK_ADDED);
            logger.info("Book {} successfully saved", book.getTitle());
        } catch (RuntimeException e) {
            request.setAttribute(ALERT, Alert.BOOK_NOT_ADDED);
            logger.warn("Book not added", e);
        }
    }

    private void changeBook(HttpServletRequest request) {
        try {
            Book book = parseInput(request);
            BookService.update(book);
            request.setAttribute(ALERT, Alert.BOOK_CHANGED);
            logger.info("Book {} successfully changed", book.getTitle());
        } catch (RuntimeException e) {
            request.setAttribute(ALERT, Alert.BOOK_NOT_CHANGED);
            logger.warn("Book not changed", e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getServletPath().equals(NEW_BOOK_ACTION)) {
            addBook(request);
        }
        if (request.getServletPath().equals(CHANGE_BOOK_ACTION)) {
            changeBook(request);
        }
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getServletPath().equals(NEW_BOOK_ACTION)) {
            request.getRequestDispatcher(NEW_BOOK_PAGE).forward(request, response);
        }
        if (request.getServletPath().equals(CHANGE_BOOK_ACTION)) {
            getBook(request, response);
        }
    }

}
