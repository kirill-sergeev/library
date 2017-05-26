package ua.nure.serhieiev.library.controller;

import ua.nure.serhieiev.library.controller.util.PaginationMapper;
import ua.nure.serhieiev.library.controller.util.Validator;
import ua.nure.serhieiev.library.model.Order;
import ua.nure.serhieiev.library.model.User;
import ua.nure.serhieiev.library.service.OrderService;
import ua.nure.serhieiev.library.service.util.Pagination;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static ua.nure.serhieiev.library.controller.Action.Constants.*;

@WebServlet(name = "OrderServlet", urlPatterns = {ORDER_LIST_ACTION, UNCONFIRMED_ORDER_LIST_ACTION, CURRENT_ORDER_LIST_ACTION})
public class OrderServlet extends HttpServlet {

    private static final String UNCONFIRMED_ORDER_LIST_PAGE = "/WEB-INF/jsp/unconfirmed-orders.jsp";
    private static final String CURRENT_ORDER_LIST_PAGE = "/WEB-INF/jsp/current-orders.jsp";
    private static final String ORDER_LIST_PAGE = "/WEB-INF/jsp/orders.jsp";

    private Integer getOrderId(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String orderParam = request.getParameter("order");
        if (!Validator.isInteger(orderParam)) {
            request.setAttribute("alert", "Unknown order!");
           // request.getRequestDispatcher(ORDER_LIST_PAGE).forward(request, response);
        }
        return Integer.valueOf(orderParam);
    }

    private void getAllOrders(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Pagination pagination = PaginationMapper.getPagination(request);
        Map<Integer, List<Order>> ordersMap = OrderService.getRange(pagination);
        List<Order> orders = ordersMap.entrySet().iterator().next().getValue();
        int ordersCount = ordersMap.entrySet().iterator().next().getKey();
        int nOfPages = (int) Math.ceil(ordersCount/ ((double) pagination.getLimit()));
        request.setAttribute("nOfPages", nOfPages);
        request.setAttribute("orders", orders);
    }

    private void getCurrentOrders(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Pagination pagination = PaginationMapper.getPagination(request);
        Map<Integer, List<Order>> ordersMap = OrderService.getRangeCurrent(pagination);
        List<Order> orders = ordersMap.entrySet().iterator().next().getValue();
        int ordersCount = ordersMap.entrySet().iterator().next().getKey();
        int nOfPages = (int) Math.ceil(ordersCount/ ((double) pagination.getLimit()));
        request.setAttribute("nOfPages", nOfPages);
        request.setAttribute("orders", orders);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("button");
        Integer orderId = getOrderId(request, response);
        Order order = new Order().setId(orderId);

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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        switch (path) {
            case UNCONFIRMED_ORDER_LIST_ACTION:
                request.setAttribute("orders", OrderService.getUnconfirmed());
                request.getRequestDispatcher(UNCONFIRMED_ORDER_LIST_PAGE).forward(request, response);
                return;
            case CURRENT_ORDER_LIST_ACTION:
                getCurrentOrders(request, response);
                request.getRequestDispatcher(CURRENT_ORDER_LIST_PAGE).forward(request, response);
                return;
            case ORDER_LIST_ACTION:
                getAllOrders(request, response);
                request.getRequestDispatcher(UNCONFIRMED_ORDER_LIST_PAGE).forward(request, response);
        }
    }

}
