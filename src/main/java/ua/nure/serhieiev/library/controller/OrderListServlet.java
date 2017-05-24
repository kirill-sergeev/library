/*
package ua.nure.serhieiev.library.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.nure.serhieiev.library.controller.util.PaginationMapper;
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

import static ua.nure.serhieiev.library.controller.Action.Constants.BOOK_LIST_ACTION;
import static ua.nure.serhieiev.library.model.User.Role.LIBRARIAN;

@WebServlet(name = "OrderListServlet", urlPatterns = "/orders.do")
public class OrderListServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(BookListServlet.class);
    private static final String ORDER_LIST_PAGE = "/WEB-INF/jsp/orders.jsp";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Pagination pagination = PaginationMapper.getPagination(request);

        Map<Integer, List<Order>> ordersMap;

        if (request.getParameterMap().containsKey("reader")) {
            Integer readerId = Integer.valueOf(request.getParameter("reader"));
            ordersMap = OrderService.getRangeByReader(pagination, new User().setId(readerId));
        } else{
            ordersMap = OrderService.getRange(pagination);
        }

        int ordersCount;
        List<Order> orders = ordersMap.entrySet().iterator().next().getValue();
        ordersCount = ordersMap.entrySet().iterator().next().getKey();

        int nOfPages = (int) Math.ceil(ordersCount / ((double) pagination.getLimit()));
        request.setAttribute("nOfPages", nOfPages);
        request.setAttribute("orders", orders);
        request.getRequestDispatcher(ORDER_LIST_PAGE).forward(request, response);
    }
}
*/
