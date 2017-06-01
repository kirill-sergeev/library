package ua.nure.serhieiev.library.controller;

import ua.nure.serhieiev.library.controller.util.PaginationMapper;
import ua.nure.serhieiev.library.model.Pagination;
import ua.nure.serhieiev.library.model.entities.Order;
import ua.nure.serhieiev.library.model.entities.User;
import ua.nure.serhieiev.library.service.OrderService;
import ua.nure.serhieiev.library.service.util.EmailUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static ua.nure.serhieiev.library.controller.util.Action.Constants.ORDERS_ACTION;

@WebServlet(name = "OrderListServlet", urlPatterns = ORDERS_ACTION)
public class OrderListServlet extends HttpServlet {

    private static final String UNCONFIRMED_ORDERS_PAGE = "/WEB-INF/jsp/unconfirmed-orders.jsp";
    private static final String CURRENT_ORDERS_PAGE = "/WEB-INF/jsp/current-orders.jsp";
    private static final String CLOSED_ORDERS_PAGE = "/WEB-INF/jsp/closed-orders.jsp";

    private void setExpectedDate(Order order) {
        if (order.getInternal()) {
            order.setExpectedDate(LocalDate.now());
        } else {
            Integer orderDays = Integer.valueOf(getServletContext().getInitParameter("orderDays"));
            order.setExpectedDate(LocalDate.now().plusDays(orderDays));
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("button");
        Integer orderId = Integer.valueOf(request.getParameter("order"));
        Order order = OrderService.getById(orderId);

        if (action == null) {
            action = "";
        }

        switch (action) {
            case "accept":
                User librarian = (User) request.getSession().getAttribute("user");
                order.setLibrarian(librarian);
                setExpectedDate(order);
                OrderService.acceptOrder(order);
                break;
            case "reject":
                OrderService.rejectOrder(order);
                new Thread(() -> EmailUtil.sendRejectMessage(order.getReader())).start();
                break;
            case "return":
                OrderService.returnOrder(order);
                break;
            case "email":
                new Thread(() -> EmailUtil.sendWarnMessage(order.getReader())).start();
        }
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Pagination pagination = PaginationMapper.getPagination(request);
        String type = request.getParameter("type");
        List<Order> orders;
        String path;

        if (type == null) {
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
