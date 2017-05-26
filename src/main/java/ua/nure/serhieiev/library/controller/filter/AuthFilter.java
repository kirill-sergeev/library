package ua.nure.serhieiev.library.controller.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.nure.serhieiev.library.controller.Action;
import ua.nure.serhieiev.library.model.entities.User;
import ua.nure.serhieiev.library.service.ApplicationException;
import ua.nure.serhieiev.library.service.UserService;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static ua.nure.serhieiev.library.controller.Action.Constants.*;
import static ua.nure.serhieiev.library.model.entities.User.Role.*;

@WebFilter(filterName = "AuthFilter", urlPatterns = {"*.do"})
public class AuthFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(AuthFilter.class);
    public static final String LOGIN_COOKIE = "LOGIN_COOKIE";

    private User setUserRole(HttpServletRequest req) {
        User user = new User().setRole(GUEST);
        if (req.getCookies() != null) {
            for (Cookie cookie : req.getCookies()) {
                if (cookie.getName().equals(LOGIN_COOKIE)) {
                    try {
                        user.setAuthToken(cookie.getValue());
                        user = UserService.authenticate(user);
                    } catch (ApplicationException e) {
                        LOG.info("Unsuccessful auth by token.");
                    }
                    break;
                }
            }
        }
        req.getSession().setAttribute("user", user);
        return user;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(true);
        User user = (User) session.getAttribute("user");

        if (user == null || user.getRole() == null) {
            user = setUserRole(req);
        }

        for (Action action : Action.values()) {
            if (req.getServletPath().contains(action.getPath())) {
                for (User.Role role : action.getAllowedUsers()) {
                    if (role == user.getRole()) {
                        chain.doFilter(req, resp);
                        return;
                    }
                }
            }
        }
        String action = user.getRole() == GUEST ? LOGIN_ACTION : MAIN_ACTION;
        resp.sendRedirect(action);
    }

    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

}




