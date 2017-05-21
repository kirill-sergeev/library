package ua.nure.serhieiev.library.controller.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.nure.serhieiev.library.service.util.Pagination;

import javax.servlet.http.HttpServletRequest;

public final class PaginationMapper {

    private static final Logger LOG = LoggerFactory.getLogger(PaginationMapper.class);

    public static Pagination getPagination(HttpServletRequest request) {
        Pagination pagination = new Pagination();
        try {
            String itemsParam = request.getParameter("items");
            if (itemsParam != null && !itemsParam.isEmpty()) {
                pagination.setLimit(Integer.parseInt(itemsParam));
            }
            String pageParam = request.getParameter("page");
            if (pageParam != null && !pageParam.isEmpty()) {
                pagination.setPage(Integer.parseInt(pageParam));
            }
            String orderParam = request.getParameter("order");
            if (orderParam != null && "desc".equals(orderParam)) {
                pagination.setAscending(false);
            }
            String sortParam = request.getParameter("sort");
            if (sortParam != null && !sortParam.isEmpty()) {
                pagination.setSortBy(sortParam.toLowerCase());
            }
        } catch (RuntimeException e) {
            LOG.trace("Bad parameters for items list extracting.", e);
        }

        request.setAttribute("items", pagination.getLimit());
        request.setAttribute("page", pagination.getPage());
        request.setAttribute("order", pagination.isAscending() ? "" : "desc");
        request.setAttribute("sort", pagination.getSortBy());

        return pagination;
    }

    private PaginationMapper() {

    }

}
