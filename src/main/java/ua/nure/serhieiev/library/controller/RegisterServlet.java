package ua.nure.serhieiev.library.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.nure.serhieiev.library.controller.util.Alert;
import ua.nure.serhieiev.library.model.entities.User;
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

@WebServlet(name = "RegisterServlet", asyncSupported = true,urlPatterns = {REGISTER_ACTION, REGISTER_LIBRARIAN_ACTION})
public class RegisterServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(RegisterServlet.class);
    private static final String REGISTER_PAGE = "/WEB-INF/jsp/login.jsp";
    private static final String REGISTER_LIBRARIAN_PAGE = "/WEB-INF/jsp/admin-new-librarian.jsp";

    private void register(HttpServletRequest request)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email").toLowerCase();
        String password = request.getParameter("password");
        User user = new User()
                .setName(name)
                .setEmail(email)
                .setPassword(password);
        try {
            if (request.getServletPath().equals(REGISTER_ACTION)) {
                UserService.saveReader(user);
                new Thread(() -> EmailUtil.sendRegistrationLink(user)).start();
            } else {
                UserService.saveLibrarian(user);
            }
            request.setAttribute("alert", Alert.REGISTRATION_SUCCESSFUL);
            LOG.info("Registered new user with email {}.", email);
        } catch (ApplicationException e) {
            request.setAttribute("alert", Alert.EMAIL_ALREADY_IN_USE);
            LOG.info("Unsuccessful registration with email {}.", email, e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        register(request);
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String page;
        if (request.getServletPath().equals(REGISTER_ACTION)) {
            page = REGISTER_PAGE;
        } else {
            page = REGISTER_LIBRARIAN_PAGE;
        }
        request.getRequestDispatcher(page).forward(request, response);
    }

}
