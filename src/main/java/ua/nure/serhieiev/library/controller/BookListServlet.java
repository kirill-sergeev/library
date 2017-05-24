package ua.nure.serhieiev.library.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.nure.serhieiev.library.controller.util.PaginationMapper;
import ua.nure.serhieiev.library.model.Author;
import ua.nure.serhieiev.library.model.Genre;
import ua.nure.serhieiev.library.model.Publisher;
import ua.nure.serhieiev.library.service.BookService;
import ua.nure.serhieiev.library.service.util.Pagination;
import ua.nure.serhieiev.library.model.Book;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static ua.nure.serhieiev.library.controller.Action.Constants.*;
import static ua.nure.serhieiev.library.service.BookService.*;

@WebServlet(name = "BookListServlet", urlPatterns = BOOK_LIST_ACTION)
public class BookListServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(BookListServlet.class);
    private static final String BOOK_LIST_PAGE = "/WEB-INF/jsp/books.jsp";


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Map<Integer, List<Book>> bookMap;
        List<Book> books;
        int booksCount;
        Pagination pagination = PaginationMapper.getPagination(request);


        if (request.getParameterMap().containsKey("author")) {
            Integer author = Integer.valueOf(request.getParameter("author"));
            bookMap = BookService.getRangeByAuthor(pagination, new Author().setId(author));
        } else if (request.getParameterMap().containsKey("genre")) {
            Integer genre = Integer.valueOf(request.getParameter("genre"));
            bookMap = BookService.getRangeByGenre(pagination, new Genre().setId(genre));
        } else if(request.getParameterMap().containsKey("publisher")){
            Integer publisher = Integer.valueOf(request.getParameter("publisher"));
            bookMap = BookService.getRangeByPublisher(pagination, new Publisher().setId(publisher));
        } else {
            bookMap = BookService.getRange(pagination);
        }


        Map<LocalDateTime, Integer> globalCart = (Map<LocalDateTime, Integer>) getServletContext().getAttribute("globalCart");
        books = bookMap.entrySet().iterator().next().getValue();
        for (Book book: books){
            int inCart = Collections.frequency(globalCart.values(), book.getId());
            System.out.println(book.getAvailable());
            System.out.println(inCart);
            book.setAvailable(book.getAvailable() - inCart);
        }



        booksCount = bookMap.entrySet().iterator().next().getKey();
        int nOfPages = (int) Math.ceil(booksCount/ ((double) pagination.getLimit()));
        request.setAttribute("nOfPages", nOfPages);
        request.setAttribute("books", books);
        request.getRequestDispatcher(BOOK_LIST_PAGE).forward(request, response);
    }

}
