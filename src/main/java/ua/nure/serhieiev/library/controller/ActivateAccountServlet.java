package ua.nure.serhieiev.library.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.nure.serhieiev.library.controller.util.Alert;
import ua.nure.serhieiev.library.model.entities.User;
import ua.nure.serhieiev.library.service.ApplicationException;
import ua.nure.serhieiev.library.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ua.nure.serhieiev.library.controller.util.Action.Constants.ACTIVATE_ACTION;
import static ua.nure.serhieiev.library.controller.util.Action.Constants.LOGIN_ACTION;

@WebServlet(name = "ActivateAccountServlet", urlPatterns = ACTIVATE_ACTION)
public class ActivateAccountServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(ActivateAccountServlet.class);
    private static final String ALERT = "alert";

    private void activate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String activationToken = request.getParameter("token");

        try {
            User user = new User().setActivationToken(activationToken);
            UserService.activate(user);
            request.setAttribute(ALERT, Alert.ACTIVATION_SUCCESSFUL);
            logger.info("Account {} activated.", user.getEmail());
        } catch (ApplicationException e) {
            request.setAttribute(ALERT, Alert.WRONG_TOKEN);
            logger.info("Wrong activation token {}.", activationToken);
        }
        request.getRequestDispatcher(LOGIN_ACTION).forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        activate(request, response);
    }

}
