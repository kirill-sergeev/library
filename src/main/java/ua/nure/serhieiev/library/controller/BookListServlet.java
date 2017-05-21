package ua.nure.serhieiev.library.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.ArrayList;
import java.util.List;

import static ua.nure.serhieiev.library.controller.Action.Constants.*;
import static ua.nure.serhieiev.library.service.BookService.*;

@WebServlet(name = "BookListServlet", urlPatterns = BOOK_LIST_ACTION)
public class BookListServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(BookListServlet.class);
    private static final String BOOK_LIST_PAGE = "/WEB-INF/jsp/books.jsp";

    private Pagination getPagination(HttpServletRequest request) {
        Pagination pagination = new Pagination();
        try {
            String itemsParam = request.getParameter("items");
            if (itemsParam != null && !itemsParam.isEmpty()) {
                pagination.setLimit(Integer.parseInt(itemsParam));
            }
            String pageParam = request.getParameter("page");
            if (pageParam != null && !pageParam.isEmpty()) {
                pagination.setPage(Integer.parseInt(pageParam));
            }
            String orderParam = request.getParameter("order");
            if (orderParam != null && !orderParam.isEmpty()) {
                pagination.setAscending(Boolean.parseBoolean(orderParam));
            }
            String sortParam = request.getParameter("sort");
            if (sortParam != null && !sortParam.isEmpty()) {
                pagination.setSortBy(sortParam);
            }
        } catch (RuntimeException e) {
            LOG.trace("Bad parameters for items list extracting.", e);
        }

        request.setAttribute("items", pagination.getLimit());
        request.setAttribute("page", pagination.getPage());
        request.setAttribute("order", pagination.isAscending());
        request.setAttribute("sort", pagination.getSortBy());
        return pagination;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<Book> books;
        Pagination pagination = getPagination(request);
        
        if (request.getParameterMap().containsKey("author")) {
            String author = request.getParameter("author");
            books = BookService.getRangeByAuthor(pagination, new Author().setName(author));
        } else if (request.getParameterMap().containsKey("genre")) {
            String genre = request.getParameter("genre");
            books = BookService.getRangeByGenre(pagination, new Genre().setTitle(genre));
        } else if(request.getParameterMap().containsKey("publisher")){
            String publisher = request.getParameter("publisher");
            books = BookService.getRangeByPublisher(pagination, new Publisher().setTitle(publisher));
        } else {
            books = BookService.getRange(pagination);
        }

        int nOfPages = (int) Math.ceil(BookService.getBookCount()/ ((double) pagination.getLimit()));
        request.setAttribute("nOfPages", nOfPages);
        request.setAttribute("books", books);
        request.getRequestDispatcher(BOOK_LIST_PAGE).forward(request, response);
    }

}
