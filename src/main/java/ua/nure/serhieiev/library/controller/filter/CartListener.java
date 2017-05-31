package ua.nure.serhieiev.library.controller.filter;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSessionBindingEvent;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@WebListener()
public class CartListener implements ServletContextListener, HttpSessionListener {

    public CartListener() {
    }

    /**
     * This method is called when the servlet context is
     * initialized(when the Web application is deployed).
     */
    public void contextInitialized(ServletContextEvent sce) {
        sce.getServletContext()
                .setAttribute("globalCart",
                        new ConcurrentHashMap<LocalDateTime, Integer>());
    }

    public void contextDestroyed(ServletContextEvent sce) {
    }


    public void sessionCreated(HttpSessionEvent se) {
        se.getSession()
                .setAttribute("localCart",
                        new ConcurrentHashMap<Integer, LocalDateTime>());
    }

    public void sessionDestroyed(HttpSessionEvent se) {
        Map<Integer, LocalDateTime> localCart = (Map<Integer, LocalDateTime>)
                se.getSession().getAttribute("localCart");
        Map<LocalDateTime, Integer> globalCart = (Map<LocalDateTime, Integer>)
                se.getSession().getServletContext().getAttribute("globalCart");
        globalCart.keySet().removeAll(localCart.values());
    }

}
