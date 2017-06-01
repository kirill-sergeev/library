package ua.nure.serhieiev.library.controller;

import ua.nure.serhieiev.library.controller.util.PaginationMapper;
import ua.nure.serhieiev.library.model.Pagination;
import ua.nure.serhieiev.library.model.entities.Genre;
import ua.nure.serhieiev.library.service.GenreService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static ua.nure.serhieiev.library.controller.util.Action.Constants.GENRES_ACTION;

@WebServlet(name = "GenreListServlet", urlPatterns = GENRES_ACTION)
public class GenreListServlet extends HttpServlet {

    private static final String GENRE_LIST_PAGE = "/WEB-INF/jsp/genres.jsp";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Pagination pagination = PaginationMapper.getPagination(request);
        List<Genre> genres;
        genres = GenreService.getAll(pagination);
        request.setAttribute("numberOfPages", pagination.getNumberOfPages());
        request.setAttribute("genres", genres);
        request.getRequestDispatcher(GENRE_LIST_PAGE).forward(request, response);
    }

}
