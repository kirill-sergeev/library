package ua.nure.serhieiev.library.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.nure.serhieiev.library.controller.filter.AuthFilter;
import ua.nure.serhieiev.library.model.entities.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ua.nure.serhieiev.library.controller.Action.Constants.*;

@WebServlet(name = "LogoutServlet", urlPatterns = {LOGOUT_ACTION})
public class LogoutServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(LogoutServlet.class);

    private void logout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        removeCookie(request, response);
        request.getSession().invalidate();
        response.sendRedirect(MAIN_ACTION);
        LOG.info("User {} logged out.", user.getEmail());
    }

    private void removeCookie(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Cookie loginCookie = new Cookie(AuthFilter.LOGIN_COOKIE, "");
        loginCookie.setPath(request.getContextPath());
        loginCookie.setMaxAge(0);
        response.addCookie(loginCookie);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logout(request, response);
    }

}
