package ua.nure.serhieiev.library.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.nure.serhieiev.library.controller.util.Alert;
import ua.nure.serhieiev.library.controller.util.PaginationMapper;
import ua.nure.serhieiev.library.model.Pagination;
import ua.nure.serhieiev.library.model.entities.User;
import ua.nure.serhieiev.library.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static ua.nure.serhieiev.library.controller.util.Action.Constants.LIBRARIANS_ACTION;
import static ua.nure.serhieiev.library.controller.util.Action.Constants.READERS_ACTION;
import static ua.nure.serhieiev.library.model.entities.User.Role.LIBRARIAN;
import static ua.nure.serhieiev.library.model.entities.User.Role.READER;

@WebServlet(name = "UserListServlet", urlPatterns = {READERS_ACTION, LIBRARIANS_ACTION})
public class UserListServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(UserListServlet.class);
    private static final String USER_LIST_PAGE = "/WEB-INF/jsp/users.jsp";
    private static final String ALERT = "alert";

    private String setPath(HttpServletRequest request){
        String header = request.getHeader("referer");
        String path;

        if (header == null){
            path = USER_LIST_PAGE;
        } else{
            path = header.substring(header.lastIndexOf('/'));
        }
        return path;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("button");
        Integer userId = Integer.valueOf(request.getParameter("user"));
        User user = new User().setId(userId);

        if (action == null) {
            action = "";
        }

        switch (action) {
            case "activate":
                UserService.activate(user);
                request.setAttribute(ALERT, Alert.USER_UNBLOCKED);
                LOG.info("Unblocked user with id {}.", userId);
                break;
            case "block":
                UserService.block(user);
                request.setAttribute(ALERT, Alert.USER_BLOCKED);
                LOG.info("Blocked user with id {}.", userId);
                break;
            case "remove":
                UserService.remove(user);
                request.setAttribute(ALERT, Alert.USER_REMOVED);
                LOG.info("Removed user with id {}.", userId);
                break;
        }
        response.sendRedirect(setPath(request));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Pagination pagination = PaginationMapper.getPagination(request);
        List<User> users = null;

        if (request.getServletPath().equals(READERS_ACTION)){
            users = UserService.getByRole(pagination, READER);
        } else if (request.getServletPath().equals(LIBRARIANS_ACTION)){
            users = UserService.getByRole(pagination, LIBRARIAN);
        }
        request.setAttribute("numberOfPages", pagination.getNumberOfPages());
        request.setAttribute("users", users);
        request.getRequestDispatcher(USER_LIST_PAGE).forward(request, response);
    }

}
