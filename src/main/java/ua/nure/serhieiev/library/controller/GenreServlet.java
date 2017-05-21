package ua.nure.serhieiev.library.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.nure.serhieiev.library.model.Book;
import ua.nure.serhieiev.library.service.BookService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "GenreServlet", urlPatterns = {"/genre.do"})
public class GenreServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(GenreServlet.class);
    private static final String GENRE_PAGE = "/WEB-INF/jsp/genre.jsp";
    private static final String GENRE_LIST_PAGE = "/WEB-INF/jsp/genres.jsp";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (request.getParameterMap().containsKey("id")){
            String genreId = request.getParameter("id");
            List<Book> books = BookService.getRangeByGenre()
        }
    }
}
