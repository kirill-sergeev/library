package ua.nure.serhieiev.library.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.nure.serhieiev.library.controller.filter.AuthFilter;
import ua.nure.serhieiev.library.controller.util.Alert;
import ua.nure.serhieiev.library.model.entities.User;
import ua.nure.serhieiev.library.service.ApplicationException;
import ua.nure.serhieiev.library.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

import static ua.nure.serhieiev.library.controller.Action.Constants.*;


@WebServlet(name = "LoginServlet", urlPatterns = {LOGIN_ACTION})
public class LoginServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(LoginServlet.class);
    private static final String LOGIN_PAGE = "/WEB-INF/jsp/login.jsp";
    private static final String ALERT = "alert";

    private void login(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email").toLowerCase();
        String password = request.getParameter("password");
        Boolean remember = Boolean.valueOf(request.getParameter("remember"));
        User user = new User()
                .setEmail(email)
                .setPassword(password);
        try {
            if (remember) {
                String authToken = UUID.randomUUID().toString();
                user.setAuthToken(authToken);
                user = UserService.authenticate(user);
                setCookie(request, response , authToken);
            } else {
                user = UserService.authenticate(user);
            }
            request.getSession().setAttribute("user", user);
            LOG.info("Log in with email {}.", email);
        } catch (ApplicationException e) {
            request.setAttribute(ALERT, Alert.BAD_LOGIN_OR_PASSWORD);
            request.getRequestDispatcher(LOGIN_PAGE).forward(request, response);
            LOG.info("Unsuccessful log in with email {}.", email);
            return;
        }
        response.sendRedirect(MAIN_ACTION);
    }

    private void setCookie(HttpServletRequest request, HttpServletResponse response, String authToken) {
        Cookie loginCookie = new Cookie(AuthFilter.LOGIN_COOKIE, authToken);
        loginCookie.setPath(request.getContextPath());
        loginCookie.setMaxAge(24 * 60 * 60);
        response.addCookie(loginCookie);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        login(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(LOGIN_PAGE).forward(request, response);
    }

}
