package ua.nure.serhieiev.library.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.nure.serhieiev.library.controller.util.Alert;
import ua.nure.serhieiev.library.model.entities.Order;
import ua.nure.serhieiev.library.model.entities.User;
import ua.nure.serhieiev.library.service.OrderService;
import ua.nure.serhieiev.library.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static ua.nure.serhieiev.library.controller.util.Action.Constants.PROFILE_ACTION;
import static ua.nure.serhieiev.library.controller.util.Action.Constants.USER_ACTION;
import static ua.nure.serhieiev.library.model.entities.User.Role.READER;

@WebServlet(name = "ProfileServlet", urlPatterns = {PROFILE_ACTION, USER_ACTION})
public class ProfileServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(ProfileServlet.class);
    private static final String USER_PAGE = "/WEB-INF/jsp/user.jsp";
    private static final String ALERT = "alert";

    private void readerOrders(HttpServletRequest request, User user) {
        List<Order> orders = OrderService.getByReader(user);
        Map<Order, Integer> ordersWithFines = setFines(orders);
        request.setAttribute("orders", ordersWithFines);
    }

    protected Map<Order, Integer> setFines(List<Order> orders){
        Map<Order, Integer> fines = new ConcurrentHashMap<>();
        Integer oneDayFine = Integer.valueOf(getServletContext().getInitParameter("oneDayFine"));
        Integer fine;
        Integer days;
        for(Order order : orders){
            fine = 0;
            if (order.getExpectedDate() != null
                    && order.getReturnDate() == null
                    && order.getExpectedDate().isBefore(LocalDate.now())){
                days = Math.toIntExact(ChronoUnit.DAYS.between(order.getExpectedDate(), LocalDate.now()));
                fine = order.getBooks().size() * oneDayFine * days;
            }
            fines.put(order, fine);
        }
        return fines;
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (request.getParameterMap().containsKey("password")){
            try {
                user.setPassword(request.getParameter("password"));
                UserService.changePassword(user);
                request.setAttribute(ALERT, Alert.PASSWORD_CHANGED_SUCCESSFUL);
                logger.info("User with ID {} change password.", user.getId());
            } catch (RuntimeException e) {
                request.setAttribute(ALERT, Alert.PASSWORD_NOT_CHANGED);
                logger.info("User with ID {} unsuccessful trying to change password.", user.getId());
            }
        }
        if (request.getParameterMap().containsKey("email")){
            try {
                user.setEmail(request.getParameter("email"));
                UserService.changeEmail(user);
                request.setAttribute(ALERT, Alert.EMAIL_CHANGED);
                logger.info("User with ID {} change email to {}.", user.getId(), user.getEmail());
            } catch (RuntimeException e) {
                request.setAttribute(ALERT, Alert.EMAIL_NOT_CHANGED);
                logger.info("User with ID {} unsuccessful trying to change email to {}.", user.getId(), user.getEmail());
            }
        }
        request.setAttribute("profile", user);
        request.getRequestDispatcher(USER_PAGE).forward(request, response);
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
