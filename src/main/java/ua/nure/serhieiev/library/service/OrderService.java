package ua.nure.serhieiev.library.service;

import ua.nure.serhieiev.library.dao.*;
import ua.nure.serhieiev.library.model.*;
import ua.nure.serhieiev.library.service.util.Pagination;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ua.nure.serhieiev.library.model.User.Role.LIBRARIAN;
import static ua.nure.serhieiev.library.model.User.Role.READER;

public final class OrderService {

    public static Order save(Order order) {
        try (DaoFactory df = DaoFactory.getInstance()) {
            OrderDao orderDao = df.getOrderDao();
            UserDao userDao = df.getUserDao();
            User reader = userDao.getById(order.getReader().getId());
            User librarian = userDao.getById(order.getLibrarian().getId());
            if (reader.getRole() != READER || librarian.getRole() != LIBRARIAN) {
                throw new ApplicationException("Order must have reader and librarian!");
            }
            order.setReader(reader)
                    .setLibrarian(librarian);
            orderDao.save(order);
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
        return order;
    }

    public static Order update(Order order) {
        try (DaoFactory df = DaoFactory.getInstance()) {
            OrderDao orderDao = df.getOrderDao();
            orderDao.update(order);
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
        return order;
    }

    public static void remove(Integer orderId) {
        try (DaoFactory df = DaoFactory.getInstance()) {
            OrderDao orderDao = df.getOrderDao();
            orderDao.remove(orderId);
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
    }

    public static Order getById(int orderId) {
        Order order;
        try (DaoFactory df = DaoFactory.getInstance()) {
            OrderDao orderDao = df.getOrderDao();
            UserDao userDao = df.getUserDao();
            order = orderDao.getById(orderId);
            User reader = userDao.getById(order.getReader().getId());
            User librarian = userDao.getById(order.getLibrarian().getId());
            order.setReader(reader)
                    .setLibrarian(librarian);
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
        return order;
    }

    public static List<Order> getAll() {
        List<Order> orders;
        try (DaoFactory df = DaoFactory.getInstance()) {
            OrderDao orderDao = df.getOrderDao();
            orders = orderDao.getAll();
            fillNestedFields(df, orders);
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
        return orders;
    }

    public static Map<Integer, List<Order>> getRange(Pagination pagination) {
        return getRange(pagination, null);
    }

    public static Map<Integer, List<Order>> getRangeByReader(Pagination pagination, User reader) {
        return getRange(pagination, reader);
    }

    private static Map<Integer, List<Order>> getRange(Pagination pagination, Identified object) {
        Map<Integer, List<Order>> orderMap = new HashMap<>(1);
        List<Order> orders;
        Integer count;
        try (DaoFactory df = DaoFactory.getInstance()) {
            OrderDao orderDao = df.getOrderDao();
            if (object == null) {
                count = orderDao.count();
                checkPagination(pagination, count);
                orders = orderDao.getRange(pagination);
            } else if (object instanceof User) {
                count = orderDao.count((User) object);
                checkPagination(pagination, count);
                orders = orderDao.getRangeByReader((User) object, pagination);
            } else {
                throw new IllegalArgumentException("Object must be an Author, Genre, Publisher or nothing.");
            }
            fillNestedFields(df, orders);
            orderMap.put(count, orders);
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
        return orderMap;
    }

    private static void checkPagination(Pagination pagination, Integer count) {
        if ((pagination.getOffset()) >= count) {
            throw new ApplicationException("Too high offset!");
        }
    }

    private static void fillNestedFields(DaoFactory df, List<Order> orders) {
        UserDao userDao = df.getUserDao();
        BookDao bookDao = df.getBookDao();

        for (Order order : orders) {
            User reader = userDao.getById(order.getReader().getId());
            User librarian = userDao.getById(order.getLibrarian().getId());
            List<Book> books = new ArrayList<>();
            for (Book book : order.getBooks()) {
                books.add(bookDao.getById(book.getId()));
            }
            order.setBooks(books);
            order.setReader(reader);
            order.setLibrarian(librarian);
        }
    }

    private OrderService() {
    }

}
