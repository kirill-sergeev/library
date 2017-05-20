package ua.nure.serhieiev.library.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        Field sortBy = Field.TITLE;
        int itemsOnPage = 20;
        int page = 1;
        boolean order = true;
        int nOfPages = (int) Math.ceil(count() / (double) itemsOnPage);

        try {
            if (request.getParameterMap().containsKey("page")) {
                order = Boolean.parseBoolean(request.getParameter("order"));
            }
            if (request.getParameterMap().containsKey("page")) {
                page = Integer.parseInt(request.getParameter("page"));
            }
            if (request.getParameterMap().containsKey("sort")) {
                sortBy = Field.valueOf(request.getParameter("sort").toUpperCase());
            }
            if (request.getParameterMap().containsKey("page")) {
                page = Integer.parseInt(request.getParameter("page"));
            }
        } catch (RuntimeException e){
            LOG.trace("Bad parameters for book list extracting.", e);
        }
        books = getList(page, sortBy, order, itemsOnPage);
        request.setAttribute("page", page);
        request.setAttribute("order", order);
        request.setAttribute("sort", sortBy.toString().toLowerCase());
        request.setAttribute("nOfPages", nOfPages);
        request.setAttribute("books", books);
        request.getRequestDispatcher(BOOK_LIST_PAGE).forward(request, response);
    }

}
