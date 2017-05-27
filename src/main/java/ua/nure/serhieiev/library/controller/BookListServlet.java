package ua.nure.serhieiev.library.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.nure.serhieiev.library.controller.util.PaginationMapper;
import ua.nure.serhieiev.library.model.entities.Author;
import ua.nure.serhieiev.library.model.entities.Genre;
import ua.nure.serhieiev.library.model.entities.Publisher;
import ua.nure.serhieiev.library.service.BookService;
import ua.nure.serhieiev.library.model.Pagination;
import ua.nure.serhieiev.library.model.entities.Book;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static ua.nure.serhieiev.library.controller.Action.Constants.*;

@WebServlet(name = "BookListServlet", urlPatterns = BOOKS_ACTION)
public class BookListServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(BookListServlet.class);
    private static final String BOOKS_PAGE = "/WEB-INF/jsp/books.jsp";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Pagination pagination = PaginationMapper.getPagination(request);
        List<Book> books;

        if (request.getParameterMap().containsKey("author")) {
            Integer authorId = Integer.valueOf(request.getParameter("author"));
            books = BookService.getByAuthor(pagination, new Author().setId(authorId));
            request.setAttribute("author", authorId);
        } else if (request.getParameterMap().containsKey("genre")) {
            Integer genreId = Integer.valueOf(request.getParameter("genre"));
            books = BookService.getByGenre(pagination, new Genre().setId(genreId));
            request.setAttribute("genre", genreId);
        } else if(request.getParameterMap().containsKey("publisher")){
            Integer publisherId = Integer.valueOf(request.getParameter("publisher"));
            books = BookService.getByPublisher(pagination, new Publisher().setId(publisherId));
            request.setAttribute("publisher", publisherId);
        } else {
            books = BookService.getAll(pagination);
        }

        Map<LocalDateTime, Integer> globalCart = (Map<LocalDateTime, Integer>) getServletContext().getAttribute("globalCart");
        for (Book book: books){
            int inCart = Collections.frequency(globalCart.values(), book.getId());
            book.setAvailable(book.getAvailable() - inCart);
        }

        request.setAttribute("numberOfPages", pagination.getNumberOfPages());
        request.setAttribute("books", books);
        request.getRequestDispatcher(BOOKS_PAGE).forward(request, response);
    }

}
