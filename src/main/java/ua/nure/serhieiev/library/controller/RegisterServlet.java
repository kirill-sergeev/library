package ua.nure.serhieiev.library.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.nure.serhieiev.library.controller.util.Alert;
import ua.nure.serhieiev.library.model.entities.User;
import ua.nure.serhieiev.library.service.ApplicationException;
import ua.nure.serhieiev.library.service.UserService;
import ua.nure.serhieiev.library.service.util.EmailUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ua.nure.serhieiev.library.controller.util.Action.Constants.*;

@WebServlet(name = "RegisterServlet",urlPatterns = {REGISTER_ACTION, REGISTER_LIBRARIAN_ACTION})
public class RegisterServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(RegisterServlet.class);
    private static final String REGISTER_PAGE = "/WEB-INF/jsp/register.jsp";
    private static final String REGISTER_LIBRARIAN_PAGE = "/WEB-INF/jsp/register-librarian.jsp";
    private static final String ALERT = "alert";

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
            if (request.getServletPath().equals(REGISTER_ACTION)) {
                UserService.saveReader(user);
                new Thread(() -> EmailUtil.sendRegistrationLink(user)).start();
            } else {
                UserService.saveLibrarian(user);
                new Thread(() -> EmailUtil.sendWelcomeMessage(user)).start();
                response.sendRedirect(LIBRARIANS_ACTION);
                return;
            }
            request.setAttribute(ALERT, Alert.REGISTRATION_SUCCESSFUL);
            logger.info("Registered new user with email {}.", email);
        } catch (ApplicationException e) {
            request.setAttribute(ALERT, Alert.EMAIL_ALREADY_IN_USE);
            logger.info("Unsuccessful registration with email {}.", email);
        }
        doGet(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        register(request, response);
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
