package ua.nure.serhieiev.library.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.nure.serhieiev.library.controller.util.Alert;
import ua.nure.serhieiev.library.model.User;
import ua.nure.serhieiev.library.service.ApplicationException;
import ua.nure.serhieiev.library.service.UserService;
import ua.nure.serhieiev.library.service.util.EmailUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ua.nure.serhieiev.library.controller.Action.Constants.*;

@WebServlet(name = "RegisterServlet", asyncSupported = true, urlPatterns = {REGISTER_ACTION})
public class RegisterServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(RegisterServlet.class);
    private static final String REGISTER_PAGE = "/WEB-INF/jsp/login.jsp";

    private void register(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email").toLowerCase();
        String password = request.getParameter("password");
        User user = new User()
                .setName(name)
                .setEmail(email)
                .setPassword(password);
        try {
            user = UserService.save(user);
            EmailUtil.sendRegistrationLink(user);
            request.setAttribute("alert", Alert.REGISTRATION_SUCCESSFUL);
            LOG.info("Registered new user with email {}.", email);
        } catch (ApplicationException e) {
            request.setAttribute("alert", Alert.EMAIL_ALREADY_IN_USE);
            LOG.info("Unsuccessful registration with email {}.", email, e);
        }
        request.getRequestDispatcher(REGISTER_PAGE).forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        register(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(REGISTER_PAGE).forward(request, response);
    }

}



/*

@WebServlet(name = "RegisterServlet", urlPatterns = {REGISTER_ACTION})
public class RegisterServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(RegisterServlet.class);
    private static final String REGISTER_PAGE = "/WEB-INF/jsp/login.jsp";

    private void register(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email").toLowerCase();
        String password = request.getParameter("password");
        User user = new User()
                .setName(name)
                .setEmail(email)
                .setPassword(password);
        try {
            user = UserService.save(user);
            EmailUtil.sendRegistrationLink(user);
            request.setAttribute("alert", Alert.REGISTRATION_SUCCESSFUL);
            LOG.info("Registered new user with email {}.", email);
        } catch (ApplicationException e) {
            request.setAttribute("alert", Alert.EMAIL_ALREADY_IN_USE);
            LOG.info("Unsuccessful registration with email {}.", email, e);
        }
        request.getRequestDispatcher(REGISTER_PAGE).forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        register(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(REGISTER_PAGE).forward(request, response);
    }

}
*/
