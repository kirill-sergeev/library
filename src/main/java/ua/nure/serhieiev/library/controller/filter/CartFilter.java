package ua.nure.serhieiev.library.controller.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.nure.serhieiev.library.controller.LogoutServlet;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

import static ua.nure.serhieiev.library.controller.util.Action.Constants.*;

@WebFilter(filterName = "CartFilter", urlPatterns = {CART_ACTION, BOOKS_ACTION})
public class CartFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(LogoutServlet.class);

    @SuppressWarnings("unchecked")
    private Map<LocalDateTime, Integer> getGlobalCart(HttpServletRequest request) {
        return  (Map<LocalDateTime, Integer>) request.getServletContext()
                .getAttribute("globalCart");
    }

    @SuppressWarnings("unchecked")
    private Map<Integer, LocalDateTime> getLocalCart(HttpServletRequest request) {
        return (Map<Integer, LocalDateTime>) request.getSession()
                .getAttribute("localCart");
    }

    private void updateGlobalContent(HttpServletRequest request){
        Map<LocalDateTime, Integer> globalCart = getGlobalCart(request);
        for (LocalDateTime time : globalCart.keySet()) {
            if (time.plusSeconds(30).isBefore(LocalDateTime.now())) {
                Integer bookId = globalCart.remove(time);
                logger.info("Book with ID {} removed from global cart", bookId);
            }
        }
    }

    private void updateLocalCartContent(HttpServletRequest request){
        Map<Integer, LocalDateTime> localCart = getLocalCart(request);
        localCart.keySet().retainAll(getGlobalCart(request).values());
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        updateGlobalContent(req);
        updateLocalCartContent(req);
        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

}
