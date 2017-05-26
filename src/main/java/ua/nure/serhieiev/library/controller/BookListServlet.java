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

@WebServlet(name = "BookListServlet", urlPatterns = BOOK_LIST_ACTION)
public class BookListServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(BookListServlet.class);
    private static final String BOOK_LIST_PAGE = "/WEB-INF/jsp/books.jsp";


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<Book> books;
        Pagination pagination = PaginationMapper.getPagination(request);

        if (request.getParameterMap().containsKey("author")) {
            Integer author = Integer.valueOf(request.getParameter("author"));
            books = BookService.getRangeByAuthor(pagination, new Author().setId(author));
        } else if (request.getParameterMap().containsKey("genre")) {
            Integer genre = Integer.valueOf(request.getParameter("genre"));
            books = BookService.getRangeByGenre(pagination, new Genre().setId(genre));
        } else if(request.getParameterMap().containsKey("publisher")){
            Integer publisher = Integer.valueOf(request.getParameter("publisher"));
            books = BookService.getRangeByPublisher(pagination, new Publisher().setId(publisher));
        } else {
            books = BookService.getRange(pagination);
        }

        Map<LocalDateTime, Integer> globalCart = (Map<LocalDateTime, Integer>) getServletContext().getAttribute("globalCart");
        for (Book book: books){
            int inCart = Collections.frequency(globalCart.values(), book.getId());
            book.setAvailable(book.getAvailable() - inCart);
        }

        request.setAttribute("numberOfPages", pagination.getNumberOfPages());
        request.setAttribute("books", books);
        request.getRequestDispatcher(BOOK_LIST_PAGE).forward(request, response);
    }

}
