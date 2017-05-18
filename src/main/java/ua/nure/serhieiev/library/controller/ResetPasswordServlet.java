package ua.nure.serhieiev.library.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.nure.serhieiev.library.controller.util.Alert;
import ua.nure.serhieiev.library.model.User;
import ua.nure.serhieiev.library.service.ApplicationException;
import ua.nure.serhieiev.library.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ua.nure.serhieiev.library.controller.Action.Constants.*;

@WebServlet(name = "ResetPasswordServlet", urlPatterns = {RESET_ACTION})
public class ResetPasswordServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(ResetPasswordServlet.class);
    private static final String RESET_PAGE = "/reset.jsp";
    private static final String CHANGE_PASSWORD_PAGE = "/change-password.jsp";

    private void sendResetToken(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        try {
            UserService.resetPassword(new User().setEmail(email));
            request.setAttribute("alert", Alert.PASSWORD_RESET_SUCCESSFUL);
            LOG.info("Reset password request with email {}.", email);
        } catch (ApplicationException e) {
            request.setAttribute("alert", Alert.WRONG_EMAIL);
            LOG.info("Wrong reset password request with email {}.", email);
        }
        request.getRequestDispatcher(RESET_PAGE).forward(request, response);
    }

    private void confirmResetToken(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String resetToken = request.getParameter("token");
        try {
            User user = UserService.getUniqueMatching(new User().setResetPasswordToken(resetToken));
            LOG.info("Used reset password token on account {}.", user.getEmail());
        } catch (ApplicationException e) {
            request.setAttribute("alert", Alert.WRONG_TOKEN);
            request.getRequestDispatcher(RESET_PAGE).forward(request, response);
            LOG.info("Used wrong reset password token {}.", resetToken);
            return;
        }
        request.getRequestDispatcher(CHANGE_PASSWORD_PAGE).forward(request, response);
    }

    private void changePasswordByToken(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String resetToken = request.getParameter("token");
        String password = request.getParameter("password");
        User user = new User().setResetPasswordToken(resetToken).setPassword(password);
        try {
            user = UserService.changePassword(user);
            request.setAttribute("alert", Alert.PASSWORD_CHANGED_SUCCESSFUL);
            LOG.info("Password changed by reset token on account {}.", user.getEmail());
        } catch (ApplicationException e) {
            request.setAttribute("alert", Alert.PASSWORD_NOT_CHANGED);
            request.getRequestDispatcher(RESET_PAGE).forward(request, response);
            LOG.info("Password not changed by reset token {}.", resetToken);
            return;
        }
        response.sendRedirect(LOGIN_ACTION);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getParameterMap().containsKey("email")) {
            sendResetToken(request, response);
        } else if (request.getParameterMap().containsKey("token")) {
            changePasswordByToken(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getParameterMap().containsKey("token")) {
            confirmResetToken(request, response);
        } else {
            request.getRequestDispatcher(RESET_PAGE).forward(request, response);
        }
    }

}
