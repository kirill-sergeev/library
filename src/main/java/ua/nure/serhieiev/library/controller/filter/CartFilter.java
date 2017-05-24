package ua.nure.serhieiev.library.controller.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@WebFilter("/*")
public class CartFilter implements Filter {
    public void destroy() {
    }


    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(true);
        Map<Integer, LocalDateTime> cart = (Map<Integer, LocalDateTime>) session.getAttribute("localCart");


        Map<LocalDateTime, Integer> globalCart = (Map<LocalDateTime, Integer>) req.getServletContext().getAttribute("globalCart");
        for (LocalDateTime time : globalCart.keySet()) {
            if (time.plusSeconds(30).isBefore(LocalDateTime.now())) {
                System.out.println("removed " + globalCart.remove(time));

            }
        }
        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
