package ua.nure.serhieiev.library.controller;

import ua.nure.serhieiev.library.model.User;
import ua.nure.serhieiev.library.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static ua.nure.serhieiev.library.controller.Action.Constants.*;

@WebServlet(name = "UserListServlet", urlPatterns = {USER_LIST_ACTION})
public class UserListServlet extends HttpServlet {

    private static final String USER_LIST_PAGE = "/WEB-INF/jsp/admin-users.jsp";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("button");
        Integer userId = Integer.valueOf(request.getParameter("user"));
        User user = new User().setId(userId);

        switch (action) {
            case "info":
                response.sendRedirect(USER_PROFILE_ACTION + "/?id=" + userId);
                break;
            case "block":
                request.getRequestDispatcher(USER_LIST_ACTION).forward(request, response);
                break;
            case "delete":
                request.getRequestDispatcher(USER_LIST_ACTION).forward(request, response);
                break;
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<User> users = UserService.getAll();
        request.setAttribute("users", users);
        request.getRequestDispatcher(USER_LIST_PAGE).forward(request, response);
    }
}
