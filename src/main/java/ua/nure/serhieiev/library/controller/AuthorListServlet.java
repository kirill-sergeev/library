package ua.nure.serhieiev.library.controller;

import ua.nure.serhieiev.library.controller.util.PaginationMapper;
import ua.nure.serhieiev.library.controller.util.Validator;
import ua.nure.serhieiev.library.model.Pagination;
import ua.nure.serhieiev.library.model.entities.Author;
import ua.nure.serhieiev.library.service.AuthorService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static ua.nure.serhieiev.library.controller.util.Action.Constants.AUTHORS_ACTION;

@WebServlet(name = "AuthorListServlet", urlPatterns = AUTHORS_ACTION)
public class AuthorListServlet extends HttpServlet {

    private static final String AUTHOR_LIST_PAGE = "/WEB-INF/jsp/authors.jsp";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Pagination pagination = PaginationMapper.getPagination(request);
        List<Author> authors;

        String name = request.getParameter("search");
        if (Validator.isString(name)) {
            authors = AuthorService.getByName(name);
        } else {
            authors = AuthorService.getAll(pagination);
        }
        request.setAttribute("numberOfPages", pagination.getNumberOfPages());
        request.setAttribute("authors", authors);
        request.getRequestDispatcher(AUTHOR_LIST_PAGE).forward(request, response);
    }
}
