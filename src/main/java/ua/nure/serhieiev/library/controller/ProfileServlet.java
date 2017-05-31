package ua.nure.serhieiev.library.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.nure.serhieiev.library.controller.util.PaginationMapper;
import ua.nure.serhieiev.library.model.entities.Order;
import ua.nure.serhieiev.library.model.entities.User;
import ua.nure.serhieiev.library.service.OrderService;
import ua.nure.serhieiev.library.model.Pagination;
import ua.nure.serhieiev.library.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static ua.nure.serhieiev.library.controller.util.Action.Constants.*;
import static ua.nure.serhieiev.library.model.entities.User.Role.*;

@WebServlet(name = "ProfileServlet", urlPatterns = {PROFILE_ACTION, USER_ACTION})
public class ProfileServlet extends HttpServlet {

    private static final String USER_PAGE = "/WEB-INF/jsp/user.jsp";

    private void readerOrders(HttpServletRequest request, User user) {
        List<Order> orders = OrderService.getByReader(user);
        request.setAttribute("orders", orders);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = null;
        if (request.getServletPath().equals(PROFILE_ACTION)) {
            user = (User) request.getSession().getAttribute("user");
            if (user.getRole() == READER) {
                readerOrders(request, user);
            }
        } else if (request.getServletPath().equals(USER_ACTION)) {
            Integer userId = Integer.valueOf(request.getParameter("id"));
            user = UserService.getUniqueMatching(new User().setId(userId));
            if (user.getRole() == READER) {
                readerOrders(request, user);
            }
        }

        request.setAttribute("profile", user);
        request.getRequestDispatcher(USER_PAGE).forward(request, response);
    }

}
