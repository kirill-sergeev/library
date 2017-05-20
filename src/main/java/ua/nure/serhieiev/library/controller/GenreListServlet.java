package ua.nure.serhieiev.library.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.nure.serhieiev.library.model.Genre;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static ua.nure.serhieiev.library.controller.Action.Constants.GENRE_LIST_ACTION;

@WebServlet(name = "GenreListServlet", urlPatterns = GENRE_LIST_ACTION)
public class GenreListServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(GenreListServlet.class);
    private static final String GENRE_LIST_PAGE = "/WEB-INF/jsp/genres.jsp";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<Genre> genres;

/*        genres = GenreSer;
        request.setAttribute("page", page);
        request.setAttribute("order", order);
        request.setAttribute("sort", sortBy.toString().toLowerCase());
        request.setAttribute("nOfPages", nOfPages);
        request.setAttribute("genres", genres);
        request.getRequestDispatcher(GENRE_LIST_PAGE).forward(request, response);*/
    }

}