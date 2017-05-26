package ua.nure.serhieiev.library.controller;

import ua.nure.serhieiev.library.controller.util.Validator;
import ua.nure.serhieiev.library.dao.NotFoundException;
import ua.nure.serhieiev.library.model.Book;
import ua.nure.serhieiev.library.model.Order;
import ua.nure.serhieiev.library.model.User;
import ua.nure.serhieiev.library.service.BookService;
import ua.nure.serhieiev.library.service.OrderService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static ua.nure.serhieiev.library.controller.Action.Constants.*;

@WebServlet(name = "CartServlet", urlPatterns = CART_ACTION)
public class CartServlet extends HttpServlet {

    private static final String CART_PAGE = "/WEB-INF/jsp/cart.jsp";

    private Map<Integer, LocalDateTime> getLocalCart(HttpServletRequest request) {
        return (Map<Integer, LocalDateTime>) request.getSession().getAttribute("localCart");
    }

    private Map<LocalDateTime, Integer> getGlobalCart() {
        return (Map<LocalDateTime, Integer>) getServletContext().getAttribute("globalCart");
    }

    private Integer getBookId(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String bookParam = request.getParameter("book");
        if (!Validator.isInteger(bookParam)) {
            request.setAttribute("alert", "Bad parameter!");
            request.getRequestDispatcher(CART_PAGE).forward(request, response);
        }
        return Integer.valueOf(bookParam);
    }

    private void addToCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer bookId = getBookId(request, response);
        Map<LocalDateTime, Integer> globalCart = getGlobalCart();
        Map<Integer, LocalDateTime> localCart = getLocalCart(request);
        if (localCart.containsKey(bookId)) {
            request.setAttribute("alert", "You already add this book to cart!");
            return;
        }
        if (localCart.size() > 10) {
            request.setAttribute("alert", "Limit 10 books for reader!");
            return;
        }
        try {
            Book book = BookService.getById(bookId);
            if (book.getAvailable() > 0) {
                localCart.put(book.getId(), LocalDateTime.now());
                globalCart.put(LocalDateTime.now(), book.getId());
            } else {
                request.setAttribute("alert", "Book not available!");
            }
        } catch (NotFoundException e) {
            request.setAttribute("alert", "Book not found!");
        }
    }

    private void removeFromCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer bookId = getBookId(request, response);
        Map<LocalDateTime, Integer> globalCart = getGlobalCart();
        Map<Integer, LocalDateTime> localCart = getLocalCart(request);
        if (localCart.containsKey(bookId)) {
            globalCart.remove(localCart.remove(bookId));
        } else {
            request.setAttribute("alert", "You have not this book in cart!");
        }
    }

    private void clearCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Map<LocalDateTime, Integer> globalCart = getGlobalCart();
        Map<Integer, LocalDateTime> localCart = getLocalCart(request);
        globalCart.keySet().removeAll(localCart.values());
        localCart.clear();
    }

    private List<Book> getCartContent(HttpServletRequest request) {
        Map<LocalDateTime, Integer> globalCart = getGlobalCart();
        Map<Integer, LocalDateTime> localCart = getLocalCart(request);
        localCart.keySet().retainAll(globalCart.values());

        List<Book> books = new ArrayList<>();
        for (Integer id : localCart.keySet()) {
            Book book = BookService.getById(id);
            books.add(book);
        }
        return books;
    }

    private void makeOrder(HttpServletRequest request) {
        Boolean internal = Boolean.valueOf(request.getParameter("internal"));
        User user = (User) request.getSession().getAttribute("user");
        Order order = new Order()
                .setReader(user)
                .setInternal(internal)
                .setBooks(getCartContent(request));
        OrderService.makeOrder(order);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("button");

        switch (action) {
            case "add":
                addToCart(request, response);
                break;
            case "remove":
                removeFromCart(request, response);
                break;
            case "clear":
                clearCart(request, response);
                break;
            case "order":
                makeOrder(request);
                clearCart(request, response);
        }
        getCartContent(request);
        request.getRequestDispatcher(BOOK_LIST_ACTION).forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("books", getCartContent(request));
        request.getRequestDispatcher(CART_PAGE).forward(request, response);
    }

}
