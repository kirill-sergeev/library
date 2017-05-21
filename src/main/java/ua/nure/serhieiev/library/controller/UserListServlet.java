package ua.nure.serhieiev.library.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.nure.serhieiev.library.controller.util.Alert;
import ua.nure.serhieiev.library.model.User;
import ua.nure.serhieiev.library.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static ua.nure.serhieiev.library.controller.Action.Constants.*;
import static ua.nure.serhieiev.library.model.User.Role.LIBRARIAN;
import static ua.nure.serhieiev.library.model.User.Role.READER;

@WebServlet(name = "UserListServlet", urlPatterns = {READER_LIST_ACTION, LIBRARIAN_LIST_ACTION})
public class UserListServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(UserListServlet.class);
    private static final String USER_LIST_PAGE = "/WEB-INF/jsp/admin-users.jsp";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("button");
        Integer userId = Integer.valueOf(request.getParameter("user"));
        User user = new User().setId(userId);

        switch (action) {
            case "activate":
                UserService.activate(user);
                request.setAttribute("alert", Alert.USER_UNBLOCKED);
                LOG.info("Unblocked user with id {}.", userId);
                break;
            case "block":
                UserService.block(user);
                request.setAttribute("alert", Alert.USER_BLOCKED);
                LOG.info("Blocked user with id {}.", userId);
                break;
            case "remove":
                UserService.remove(user);
                request.setAttribute("alert", Alert.USER_REMOVED);
                LOG.info("Removed user with id {}.", userId);
                break;
        }
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<User> users;
        String pageType;
        if (request.getServletPath().equals(READER_LIST_ACTION)){
            users = UserService.getAll(READER);
            pageType = READER.value();
        } else {
            users = UserService.getAll(LIBRARIAN);
            pageType = LIBRARIAN.value();
        }
        request.setAttribute("pageType", pageType);
        request.setAttribute("users", users);
        request.getRequestDispatcher(USER_LIST_PAGE).forward(request, response);
    }

}
