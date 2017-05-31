package ua.nure.serhieiev.library.controller;

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

import static ua.nure.serhieiev.library.controller.util.Action.Constants.PUBLISHERS_ACTION;

@WebServlet(name = "PublisherListServlet", urlPatterns = PUBLISHERS_ACTION)
public class PublisherListServlet extends HttpServlet {

    private static final String PUBLISHER_LIST_PAGE = "/WEB-INF/jsp/publishers.jsp";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Pagination pagination = PaginationMapper.getPagination(request);
        List<Publisher> publishers;

        String title = request.getParameter("search");
        if (title != null && !title.trim().isEmpty()) {
            publishers = PublisherService.getByTitle(title);
        } else {
            publishers = PublisherService.getAll(pagination);
        }
        request.setAttribute("numberOfPages", pagination.getNumberOfPages());
        request.setAttribute("publishers", publishers);
        request.getRequestDispatcher(PUBLISHER_LIST_PAGE).forward(request, response);
    }

}

