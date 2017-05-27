package ua.nure.serhieiev.library.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.nure.serhieiev.library.controller.util.PaginationMapper;
import ua.nure.serhieiev.library.model.entities.Order;
import ua.nure.serhieiev.library.model.entities.User;
import ua.nure.serhieiev.library.service.OrderService;
import ua.nure.serhieiev.library.model.Pagination;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static ua.nure.serhieiev.library.model.entities.User.Role.LIBRARIAN;

@WebServlet(name = "ProfileServlet", urlPatterns = {"/profile.do"})
public class ProfileServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(ProfileServlet.class);
    private static final String PROFILE_PAGE = "/WEB-INF/jsp/user-profile.jsp";
    private static final String ORDER_LIST_PAGE = "/WEB-INF/jsp/orders.jsp";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Pagination pagination = PaginationMapper.getPagination(request);
        User user = (User) request.getSession().getAttribute("user");

        String path;
        List<Order> orders;
        if (user.getRole().ordinal() < LIBRARIAN.ordinal()){
            orders = OrderService.getByReader(pagination, user);
            path = PROFILE_PAGE;
        } else{
            orders = OrderService.getAll(pagination);
            path = ORDER_LIST_PAGE;
        }

        request.setAttribute("numberOfPages", pagination.getNumberOfPages());
        request.setAttribute("orders", orders);
        request.getRequestDispatcher(path).forward(request, response);
    }

}
