package ua.nure.serhieiev.library.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ua.nure.serhieiev.library.controller.Action.Constants.*;

@WebServlet(name = "UserProfileServlet", urlPatterns = {USER_PROFILE_ACTION})
public class UserProfileServlet extends HttpServlet {

    private static final String USER_PROFILE_PAGE = "/WEB-INF/jsp/users.jsp";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
