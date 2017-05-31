package ua.nure.serhieiev.library.controller;

import ua.nure.serhieiev.library.controller.util.Validator;
import ua.nure.serhieiev.library.dao.NotFoundException;
import ua.nure.serhieiev.library.model.Pagination;
import ua.nure.serhieiev.library.model.entities.Book;
import ua.nure.serhieiev.library.model.entities.Order;
import ua.nure.serhieiev.library.model.entities.User;
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

import static ua.nure.serhieiev.library.controller.util.Action.Constants.*;

@WebServlet(name = "CartServlet", urlPatterns = CART_ACTION)
public class CartServlet extends HttpServlet {

    private static final String CART_PAGE = "/WEB-INF/jsp/cart.jsp";
    private static final String ALERT = "alert";

    private User getCurrentUser(HttpServletRequest request) {
        return (User) request.getSession().getAttribute("user");
    }

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
            request.setAttribute(ALERT, "Bad parameter!");
            request.getRequestDispatcher(CART_PAGE).forward(request, response);
        }
        return Integer.valueOf(bookParam);
    }

    private void addToCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer bookId = getBookId(request, response);
        Map<LocalDateTime, Integer> globalCart = getGlobalCart();
        Map<Integer, LocalDateTime> localCart = getLocalCart(request);
        User user = getCurrentUser(request);
        if (!user.getEnabled()) {
            request.setAttribute(ALERT, "You cannot add books to cart, because your account is blocked!");
            return;
        }
        if (localCart.containsKey(bookId)) {
            request.setAttribute(ALERT, "You already add this book to cart!");
            return;
        }
        if (localCart.size() > 10) {
            request.setAttribute(ALERT, "Limit 10 books for reader!");
            return;
        }
        List<Book> currentBooks = OrderService.getCurrentBooksByReader(user);
        if (currentBooks.contains(new Book().setId(bookId))) {
            request.setAttribute(ALERT, "You have not returned this books yet!");
            return;
        }
        try {
            Book book = BookService.getById(bookId);
            int inCart = Collections.frequency(globalCart.values(), book.getId());
            if (book.getAvailable() - inCart > 0) {
                localCart.put(book.getId(), LocalDateTime.now());
                globalCart.put(LocalDateTime.now(), book.getId());
            } else {
                request.setAttribute(ALERT, "Book not available!");
            }
        } catch (NotFoundException e) {
            request.setAttribute(ALERT, "Book not found!");
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
            request.setAttribute(ALERT, "You have not this book in cart!");
        }
    }

    private void clearCart(HttpServletRequest request)
            throws ServletException, IOException {
        Map<LocalDateTime, Integer> globalCart = getGlobalCart();
        Map<Integer, LocalDateTime> localCart = getLocalCart(request);
        globalCart.keySet().removeAll(localCart.values());
        localCart.clear();
    }

    private List<Book> getCartContent(HttpServletRequest request) {
        Map<Integer, LocalDateTime> localCart = getLocalCart(request);
        List<Book> books = new ArrayList<>();
        for (Integer id : localCart.keySet()) {
            Book book = BookService.getById(id);
            books.add(book);
        }
        return books;
    }

    private void makeOrder(HttpServletRequest request)
            throws ServletException, IOException {
        User user = getCurrentUser(request);
        if (!user.getEnabled()) {
            request.setAttribute(ALERT, "You cannot make order, because your account is blocked!");
            return;
        }
        List<Book> books = getCartContent(request);
        if (books.isEmpty()) {
            request.setAttribute(ALERT, "Order is empty!");
            return;
        }

        Boolean internal = Boolean.valueOf(request.getParameter("internal"));
        Order order = new Order()
                .setReader(user)
                .setInternal(internal)
                .setBooks(books);
        OrderService.makeOrder(order);
        request.setAttribute(ALERT, "Order successfully created, wait for approve!");
        clearCart(request);
    }

    private String setPath(HttpServletRequest request) {
        String header = request.getHeader("referer");
        String path;

        if (header == null) {
            path = BOOKS_ACTION;
        } else {
            path = header.substring(header.lastIndexOf('/'));
        }
        return path;
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
                clearCart(request);
                break;
            case "order":
                makeOrder(request);
        }
        getCartContent(request);
        response.sendRedirect(setPath(request));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("books", getCartContent(request));
        request.getRequestDispatcher(CART_PAGE).forward(request, response);
    }

}
