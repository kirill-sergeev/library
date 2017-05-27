package ua.nure.serhieiev.library.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.nure.serhieiev.library.controller.util.PaginationMapper;
import ua.nure.serhieiev.library.model.entities.Genre;
import ua.nure.serhieiev.library.service.GenreService;
import ua.nure.serhieiev.library.model.Pagination;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "GenreListServlet", urlPatterns = {"/genres.do"})
public class GenreListServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(GenreListServlet.class);
    private static final String GENRE_LIST_PAGE = "/WEB-INF/jsp/genres.jsp";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Pagination pagination = PaginationMapper.getPagination(request);
        List<Genre> genres;
        genres = GenreService.getAll(pagination);
        request.setAttribute("numberOfPages", pagination.getNumberOfPages());
        request.setAttribute("genres", genres);
        request.getRequestDispatcher(GENRE_LIST_PAGE).forward(request, response);
    }
}
