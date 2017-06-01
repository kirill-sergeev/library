package ua.nure.serhieiev.library.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ErrorServlet", urlPatterns = "/error.do")
public class ErrorServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(ErrorServlet.class);
    private static final String ERROR_PAGE = "/WEB-INF/jsp/error.jsp";

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
        String servletName = (String) request.getAttribute("javax.servlet.error.servlet_name");
        String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");

        if (servletName == null){
            servletName = "Unknown";
        }
        if (requestUri == null){
            requestUri = "Unknown";
        }

        logger.debug("Status code {}.  Exception {}. Servlet {}. URI {}.",
                statusCode, throwable, servletName, requestUri);

        request.getRequestDispatcher(ERROR_PAGE).forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

}
