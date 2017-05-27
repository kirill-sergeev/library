/*
package ua.nure.serhieiev.library.controller;

import ua.nure.serhieiev.library.model.entities.Identified;
import ua.nure.serhieiev.library.service.AuthorService;
import ua.nure.serhieiev.library.service.BookService;
import ua.nure.serhieiev.library.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "SearchServlet", urlPatterns = {"/search.do"})
public class SearchServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String type = request.getParameter("type");
        String value = request.getParameter("value");
        List<Identified> result;

        switch (type) {
            case "author":
                result = AuthorService.find(value);
                break;
            case "book":
                result = BookService.find(value);
                break;
            case "reader":
                result = UserService.find(value);
                break;
        request.setAttribute("result", result);
        request.getRequestDispatcher("search.jsp").forward(request, response);
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
*/
