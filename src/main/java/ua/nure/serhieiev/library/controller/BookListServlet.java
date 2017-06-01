package ua.nure.serhieiev.library.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.nure.serhieiev.library.controller.util.PaginationMapper;
import ua.nure.serhieiev.library.controller.util.Validator;
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
import javax.sql.RowSetInternal;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static ua.nure.serhieiev.library.controller.util.Action.Constants.*;

@WebServlet(name = "BookListServlet", urlPatterns = BOOKS_ACTION)
public class BookListServlet extends HttpServlet {

    private static final String BOOKS_PAGE = "/WEB-INF/jsp/books.jsp";

    private static final String AUTHOR_PARAM = "author";
    private static final String GENRE_PARAM = "genre";
    private static final String PUBLISHER_PARAM = "publisher";

    private void setAvailableBooks(List<Book> books) {
        Map<LocalDateTime, Integer> globalCart =
                (Map<LocalDateTime, Integer>) getServletContext().getAttribute("globalCart");
        for (Book book : books) {
            int inCart = Collections.frequency(globalCart.values(), book.getId());
            book.setAvailable(book.getAvailable() - inCart);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Pagination pagination = PaginationMapper.getPagination(request);
        List<Book> books;

        String title = request.getParameter("search");
        if (Validator.isString(title)) {
            books = BookService.getByTitle(title);
        } else {
            if (request.getParameterMap().containsKey(AUTHOR_PARAM)) {
                Integer authorId = Integer.valueOf(request.getParameter(AUTHOR_PARAM));
                books = BookService.getByAuthor(pagination, new Author().setId(authorId));
                request.setAttribute(AUTHOR_PARAM, authorId);
            } else if (request.getParameterMap().containsKey(GENRE_PARAM)) {
                Integer genreId = Integer.valueOf(request.getParameter(GENRE_PARAM));
                books = BookService.getByGenre(pagination, new Genre().setId(genreId));
                request.setAttribute(GENRE_PARAM, genreId);
            } else if (request.getParameterMap().containsKey(PUBLISHER_PARAM)) {
                Integer publisherId = Integer.valueOf(request.getParameter(PUBLISHER_PARAM));
                books = BookService.getByPublisher(pagination, new Publisher().setId(publisherId));
                request.setAttribute(PUBLISHER_PARAM, publisherId);
            } else {
                books = BookService.getAll(pagination);
            }
        }

        setAvailableBooks(books);
        request.setAttribute("numberOfPages", pagination.getNumberOfPages());
        request.setAttribute("books", books);
        request.getRequestDispatcher(BOOKS_PAGE).forward(request, response);
        request.getSession().removeAttribute("alert");
    }

}
