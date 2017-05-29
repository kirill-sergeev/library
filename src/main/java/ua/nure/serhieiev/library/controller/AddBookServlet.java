package ua.nure.serhieiev.library.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.nure.serhieiev.library.model.entities.Author;
import ua.nure.serhieiev.library.model.entities.Book;
import ua.nure.serhieiev.library.model.entities.Genre;
import ua.nure.serhieiev.library.model.entities.Publisher;
import ua.nure.serhieiev.library.service.BookService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

@WebServlet(name = "AddBookServlet", urlPatterns = {"/new-book.do"})
public class AddBookServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(AddBookServlet.class);
    private static final String ADD_BOOK_PAGE = "/WEB-INF/jsp/new-book.jsp";

    private Book parseInput(HttpServletRequest request) {
        String bookTitle = request.getParameter("bookTitle");
        Long isbn = Long.valueOf(request.getParameter("isbn"));
        Integer quantity = Integer.valueOf(request.getParameter("quantity"));
        LocalDate publicationDate = LocalDate.parse(request.getParameter("publicationDate"));
        String description = request.getParameter("description");
        String publisherTitle = request.getParameter("publisherTitle");

        Publisher publisher = new Publisher().setTitle(publisherTitle);

        int i = 1;
        ArrayList<Author> authors = new ArrayList<>();
        while (request.getParameterMap().containsKey("authorName" + i)) {
            authors.add(new Author().setName(request.getParameter("authorName" + i++)));
        }

        int j = 1;
        ArrayList<Genre> genres = new ArrayList<>();
        while (request.getParameterMap().containsKey("genreTitle" + j)) {
            genres.add(new Genre().setTitle(request.getParameter("genreTitle" + j++)));
        }

        return new Book()
                .setTitle(bookTitle)
                .setIsbn(isbn)
                .setQuantity(quantity)
                .setDescription(description)
                .setPublicationDate(publicationDate).setPublisher(publisher)
                .setAuthors(authors)
                .setGenres(genres);
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Book book = parseInput(request);
        BookService.save(book);
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(ADD_BOOK_PAGE).forward(request, response);
    }

}
