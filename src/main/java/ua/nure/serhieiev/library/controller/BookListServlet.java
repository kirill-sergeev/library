package ua.nure.serhieiev.library.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.nure.serhieiev.library.service.util.Pagination;
import ua.nure.serhieiev.library.model.Book;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static ua.nure.serhieiev.library.controller.Action.Constants.*;
import static ua.nure.serhieiev.library.service.BookService.*;

@WebServlet(name = "BookListServlet", urlPatterns = BOOK_LIST_ACTION)
public class BookListServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(BookListServlet.class);
    private static final String BOOK_LIST_PAGE = "/WEB-INF/jsp/books.jsp";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<Book> books;
        String sortBy;
        String getBy;
        int itemsOnPage = 20;
        int page;
        boolean order;
        int nOfPages;

        Pagination pagination = new Pagination();

        try {
            if (request.getParameterMap().containsKey("itemsOnPage")) {
                page = Integer.parseInt(request.getParameter("itemsOnPage"));
                pagination.setLimit(itemsOnPage);
            }
            if (request.getParameterMap().containsKey("orderBy")) {
                order = Boolean.parseBoolean(request.getParameter("orderBy"));
                pagination.setAscending(order);
            }
            if (request.getParameterMap().containsKey("page")) {
                page = Integer.parseInt(request.getParameter("page"));
                pagination.setOffset((page - 1) * itemsOnPage);
            }
            if (request.getParameterMap().containsKey("sortBy")) {
                sortBy = request.getParameter("sortBy").toUpperCase();
                pagination.setSortBy(sortBy);
            }
            if (request.getParameterMap().containsKey("getBy")) {
                getBy = request.getParameter("getBy").toUpperCase();
                //pagination.setGetBy(getBy);
            }
        } catch (RuntimeException e){
            LOG.trace("Bad parameters for book list extracting.", e);
        }

     //   nOfPages = (int) Math.ceil(count() / (double) itemsOnPage);
/*        books = getRange(pagination);
        request.setAttribute("page", page);
        request.setAttribute("order", order);
        request.setAttribute("sort", sortBy.toString().toLowerCase());
        request.setAttribute("nOfPages", nOfPages);
        request.setAttribute("books", books);
        request.getRequestDispatcher(BOOK_LIST_PAGE).forward(request, response);*/
    }




}
