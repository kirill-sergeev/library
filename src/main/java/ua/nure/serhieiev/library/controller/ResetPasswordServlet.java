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

import static ua.nure.serhieiev.library.controller.util.Action.Constants.LOGIN_ACTION;
import static ua.nure.serhieiev.library.controller.util.Action.Constants.RESET_ACTION;

@WebServlet(name = "ResetPasswordServlet", urlPatterns = RESET_ACTION)
public class ResetPasswordServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(ResetPasswordServlet.class);
    private static final String RESET_PAGE = "/WEB-INF/jsp/reset.jsp";
    private static final String CHANGE_PASSWORD_PAGE = "/WEB-INF/jsp/change-password.jsp";
    private static final String ALERT = "alert";
    private static final String TOKEN_PARAM = "token";
    private static final String EMAIL_PARAM = "email";
    private static final String PASSWORD_PARAM = "password";

    private void sendResetToken(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter(EMAIL_PARAM);
        try {
            User user = new User().setEmail(email);
            UserService.resetPassword(user);
            new Thread(() -> EmailUtil.sendResetLink(user)).start();
            request.setAttribute(ALERT, Alert.PASSWORD_RESET_SUCCESSFUL);
            logger.info("Reset password request with email {}.", email);
        } catch (ApplicationException e) {
            request.setAttribute(ALERT, Alert.WRONG_EMAIL);
            logger.info("Wrong reset password request with email {}.", email);
        }
        request.getRequestDispatcher(RESET_PAGE).forward(request, response);
    }

    private void confirmResetToken(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String resetToken = request.getParameter(TOKEN_PARAM);
        try {
            User user = UserService.getUniqueMatching(new User().setResetPasswordToken(resetToken));
            request.setAttribute(TOKEN_PARAM, resetToken);
            logger.info("Used reset password token on account {}.", user.getEmail());
        } catch (ApplicationException e) {
            request.setAttribute(ALERT, Alert.WRONG_TOKEN);
            request.getRequestDispatcher(RESET_PAGE).forward(request, response);
            logger.info("Used wrong reset password token {}.", resetToken);
            return;
        }
        request.getRequestDispatcher(CHANGE_PASSWORD_PAGE).forward(request, response);
    }

    private void changePasswordByToken(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String resetToken = request.getParameter(TOKEN_PARAM);
        String password = request.getParameter(PASSWORD_PARAM);
        User user = new User().setResetPasswordToken(resetToken).setPassword(password);
        try {
            UserService.changePassword(user);
            request.setAttribute(ALERT, Alert.PASSWORD_CHANGED_SUCCESSFUL);
            logger.info("Password changed by reset token on account {}.", user.getEmail());
        } catch (ApplicationException e) {
            request.setAttribute(ALERT, Alert.PASSWORD_NOT_CHANGED);
            request.getRequestDispatcher(RESET_PAGE).forward(request, response);
            logger.info("Password not changed by reset token {}.", resetToken);
            return;
        }
        response.sendRedirect(LOGIN_ACTION);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getParameterMap().containsKey(EMAIL_PARAM)) {
            sendResetToken(request, response);
        } else if (request.getParameterMap().containsKey(TOKEN_PARAM)) {
            changePasswordByToken(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getParameterMap().containsKey(TOKEN_PARAM)) {
            confirmResetToken(request, response);
        } else {
            request.getRequestDispatcher(RESET_PAGE).forward(request, response);
        }
    }

}
