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
public class CartListener implements ServletContextListener,
        HttpSessionListener, HttpSessionAttributeListener, ServletRequestListener {

    // Public constructor is required by servlet spec
    public CartListener() {
    }

    // -------------------------------------------------------
    // ServletContextListener implementation
    // -------------------------------------------------------
    public void contextInitialized(ServletContextEvent sce) {
        sce.getServletContext()
                .setAttribute("globalCart",
                        new ConcurrentHashMap<LocalDateTime, Integer>());
      /* This method is called when the servlet context is
         initialized(when the Web application is deployed). 
         You can initialize servlet context related data here.
      */
    }

    public void contextDestroyed(ServletContextEvent sce) {
      /* This method is invoked when the Servlet Context 
         (the Web application) is undeployed or 
         Application Server shuts down.
      */
    }

    // -------------------------------------------------------
    // HttpSessionListener implementation
    // -------------------------------------------------------
    public void sessionCreated(HttpSessionEvent se) {
        se.getSession()
                .setAttribute("localCart",
                        new ConcurrentHashMap<Integer, LocalDateTime>());
      /* Session is created. */
    }

    public void sessionDestroyed(HttpSessionEvent se) {
        Map<Integer, LocalDateTime> localCart = (Map<Integer, LocalDateTime>) se.getSession().getAttribute("localCart");
        Map<LocalDateTime, Integer> globalCart = (Map<LocalDateTime, Integer>) se.getSession().getServletContext().getAttribute("globalCart");
        globalCart.keySet().removeAll(localCart.values());
      /* Session is destroyed. */
    }

    // -------------------------------------------------------
    // HttpSessionAttributeListener implementation
    // -------------------------------------------------------

    public void attributeAdded(HttpSessionBindingEvent sbe) {
      /* This method is called when an attribute 
         is added to a session.
      */
    }

    public void attributeRemoved(HttpSessionBindingEvent sbe) {
      /* This method is called when an attribute
         is removed from a session.
      */
    }

    public void attributeReplaced(HttpSessionBindingEvent sbe) {
      /* This method is invoked when an attibute
         is replaced in a session.
      */
   /*   sbe.getSession().getServletContext()
      sbe.getSource()
      if (sbe.getName().equals("localCart")){
            sbe.getValue()
        }*/
    }

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
    }

    public void requestInitialized(ServletRequestEvent sre) {
    }


}
