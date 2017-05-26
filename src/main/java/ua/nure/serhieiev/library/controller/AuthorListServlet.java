package ua.nure.serhieiev.library.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.nure.serhieiev.library.controller.util.PaginationMapper;
import ua.nure.serhieiev.library.model.entities.Author;
import ua.nure.serhieiev.library.service.AuthorService;
import ua.nure.serhieiev.library.model.Pagination;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "AuthorListServlet", urlPatterns = {"/authors.do"})
public class AuthorListServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(AuthorListServlet.class);
    private static final String AUTHOR_LIST_PAGE = "/WEB-INF/jsp/authors.jsp";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Pagination pagination = PaginationMapper.getPagination(request);
        Map<Integer, List<Author>> authorsMap;
        List<Author> authors;
        int authorsCount;
        authorsMap = AuthorService.getRange(pagination);
        authors= authorsMap.entrySet().iterator().next().getValue();
        authorsCount = authorsMap.entrySet().iterator().next().getKey();

        int nOfPages = (int) Math.ceil(authorsCount/ ((double) pagination.getLimit()));
        request.setAttribute("nOfPages", nOfPages);
        request.setAttribute("authors", authors);
        request.getRequestDispatcher(AUTHOR_LIST_PAGE).forward(request, response);
    }
}