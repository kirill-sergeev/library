package ua.nure.serhieiev.library.controller;

import ua.nure.serhieiev.library.controller.util.PaginationMapper;
import ua.nure.serhieiev.library.controller.util.Validator;
import ua.nure.serhieiev.library.model.Book;
import ua.nure.serhieiev.library.model.Order;
import ua.nure.serhieiev.library.service.BookService;
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

@WebServlet(name = "OrderServlet", urlPatterns = {"/unconfirmed-orders.do", "/orders.do"})
public class OrderServlet extends HttpServlet {

    private static final String ORDER_PAGE = "/WEB-INF/jsp/orders.jsp";

    private Integer getOrderId(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String orderParam = request.getParameter("order");
        if (!Validator.isInteger(orderParam)) {
            request.setAttribute("alert", "Unknown order!");
            request.getRequestDispatcher(ORDER_PAGE).forward(request, response);
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

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("button");
        Integer orderId = getOrderId(request, response);
        Order order = new Order().setId(orderId);

        switch (action) {
            case "accept":
                OrderService.acceptOrder(order);
                break;
            case "decline":
                OrderService.declineOrder(order);
                break;
        }
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();

        switch (path) {
            case "/unconfirmed-orders.do":
                List<Order> orders = OrderService.getUnconfirmed();
                request.setAttribute("orders", orders);
                break;
            case "/orders.do":
                getAllOrders(request, response);
        }
        request.getRequestDispatcher(ORDER_PAGE).forward(request, response);
    }

}
