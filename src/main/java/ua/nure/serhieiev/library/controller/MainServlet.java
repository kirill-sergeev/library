package ua.nure.serhieiev.library.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "MainServlet", urlPatterns = {""})
public class MainServlet extends HttpServlet {

    static final String ERROR_PAGE = "/WEB-INF/jsp/404.jsp";
    private static final String INDEX_PAGE = "/WEB-INF/jsp/index.jsp";
    public static final String INDEX_ACTION = "/";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        request.getRequestDispatcher(INDEX_PAGE).forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        request.getRequestDispatcher(INDEX_PAGE).forward(request, response);
    }

}