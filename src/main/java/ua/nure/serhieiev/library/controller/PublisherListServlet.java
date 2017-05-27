package ua.nure.serhieiev.library.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.nure.serhieiev.library.controller.util.PaginationMapper;
import ua.nure.serhieiev.library.model.entities.Publisher;
import ua.nure.serhieiev.library.service.PublisherService;
import ua.nure.serhieiev.library.model.Pagination;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "PublisherListServlet", urlPatterns = {"/publishers.do"})
public class PublisherListServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(PublisherListServlet.class);
    private static final String PUBLISHER_LIST_PAGE = "/WEB-INF/jsp/publishers.jsp";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Pagination pagination = PaginationMapper.getPagination(request);
        List<Publisher> publishers;
        publishers = PublisherService.getAll(pagination);
        request.setAttribute("numberOfPages", pagination.getNumberOfPages());
        request.setAttribute("publishers", publishers);
        request.getRequestDispatcher(PUBLISHER_LIST_PAGE).forward(request, response);
    }
}

