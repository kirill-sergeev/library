package ua.nure.serhieiev.library.controller;

import ua.nure.serhieiev.library.controller.util.PaginationMapper;
import ua.nure.serhieiev.library.controller.util.Validator;
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

import static ua.nure.serhieiev.library.controller.Action.Constants.*;

@WebServlet(name = "OrderServlet", urlPatterns = {ORDERS_ACTION})
public class OrderServlet extends HttpServlet {

    private static final String UNCONFIRMED_ORDERS_PAGE = "/WEB-INF/jsp/unconfirmed-orders.jsp";
    private static final String CURRENT_ORDERS_PAGE = "/WEB-INF/jsp/current-orders.jsp";
    private static final String CLOSED_ORDERS_PAGE = "/WEB-INF/jsp/closed-orders.jsp";

    private Integer getOrderId(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String orderParam = request.getParameter("order");
        if (!Validator.isInteger(orderParam)) {
            request.setAttribute("alert", "Unknown order!");
            // request.getRequestDispatcher(ORDER_LIST_PAGE).forward(request, response);
        }
        return Integer.valueOf(orderParam);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("button");
        Integer orderId = getOrderId(request, response);
        Order order = new Order().setId(orderId);

        if (action == null){
            action = "";
        }

        switch (action) {
            case "accept":
                User librarian = (User) request.getSession().getAttribute("user");
                order.setLibrarian(librarian);
                OrderService.acceptOrder(order);
                break;
            case "decline":
                OrderService.declineOrder(order);
                break;
            case "return":
                OrderService.returnOrder(order);
                break;
        }
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Pagination pagination = PaginationMapper.getPagination(request);
        String type = request.getParameter("type");
        List<Order> orders;
        String path;

        if (type == null){
            type = "unconfirmed";
        }

        switch (type) {
            default:
            case "unconfirmed":
                orders = OrderService.getUnconfirmed(pagination);
                path = UNCONFIRMED_ORDERS_PAGE;
                break;
            case "current":
                orders = OrderService.getCurrent(pagination);
                path = CURRENT_ORDERS_PAGE;
                break;
            case "closed":
                orders = OrderService.getClosed(pagination);
                path = CLOSED_ORDERS_PAGE;
        }

        request.setAttribute("orders", orders);
        request.setAttribute("numberOfPages", pagination.getNumberOfPages());
        request.getRequestDispatcher(path).forward(request, response);
    }

}
