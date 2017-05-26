package ua.nure.serhieiev.library.service;

import ua.nure.serhieiev.library.dao.*;
import ua.nure.serhieiev.library.model.*;
import ua.nure.serhieiev.library.service.util.Pagination;

import java.time.LocalDate;
import java.util.*;

import static ua.nure.serhieiev.library.model.User.Role.LIBRARIAN;
import static ua.nure.serhieiev.library.model.User.Role.READER;

public final class OrderService {

    public static Order makeOrder(Order order) {
        User reader = order.getReader();
        if (reader == null
                || reader.getId() == null
                || reader.getRole() != READER
                || !reader.getEnabled()) {
            throw new ApplicationException("Bad reader!");
        }
        if (order.getBooks() == null || order.getBooks().isEmpty()) {
            throw new ApplicationException("Empty order!");
        }
        if (order.getInternal() == null) {
            order.setInternal(false);
        }

        try (DaoFactory df = DaoFactory.getInstance();
             TransactionManager tm = df.getTransactionManager()) {
            OrderDao orderDao = df.getOrderDao();
            BookDao bookDao = df.getBookDao();

            tm.start();
            orderDao.save(order);

            for (Book book : order.getBooks()) {
                book = bookDao.getById(book.getId());
                book.setAvailable(book.getAvailable() - 1);
                bookDao.update(book);
            }
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
        return order;
    }

    public static Order acceptOrder(Order order) {
        if (order == null || order.getId() == null) {
            throw new ApplicationException("Bad order!");
        }
        User librarian = order.getLibrarian();
        if (librarian == null
                || librarian.getId() == null
                || librarian.getRole() != LIBRARIAN
                || !librarian.getEnabled()) {
            throw new ApplicationException("Bad librarian!");
        }
        try (DaoFactory df = DaoFactory.getInstance()) {
            OrderDao orderDao = df.getOrderDao();

            order = orderDao.getById(order.getId());
            order.setOrderDate(LocalDate.now());
            order.setLibrarian(librarian);
            orderDao.update(order);
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
        return order;
    }

    public static void declineOrder(Order order) {
        if (order == null || order.getId() == null) {
            throw new ApplicationException("Bad order!");
        }
        try (DaoFactory df = DaoFactory.getInstance();
             TransactionManager tm = df.getTransactionManager()) {
            OrderDao orderDao = df.getOrderDao();
            BookDao bookDao = df.getBookDao();

            tm.start();
            order = orderDao.getById(order.getId());
            orderDao.remove(order.getId());

            for (Book book : order.getBooks()) {
                book = bookDao.getById(book.getId());
                book.setAvailable(book.getAvailable() + 1);
                bookDao.update(book);
            }
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
    }

    public static void returnOrder(Order order) {
        if (order == null || order.getId() == null) {
            throw new ApplicationException("Bad order!");
        }
        try (DaoFactory df = DaoFactory.getInstance();
             TransactionManager tm = df.getTransactionManager()) {
            OrderDao orderDao = df.getOrderDao();
            BookDao bookDao = df.getBookDao();

            tm.start();
            order = orderDao.getById(order.getId());
            order.setReturnDate(LocalDate.now());
            orderDao.update(order);

            for (Book book : order.getBooks()) {
                book = bookDao.getById(book.getId());
                book.setAvailable(book.getAvailable() + 1);
                bookDao.update(book);
            }
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

    public static List<Order> getUnconfirmed() {
        List<Order> orders;
        try (DaoFactory df = DaoFactory.getInstance()) {
            OrderDao orderDao = df.getOrderDao();
            orders = orderDao.getUnconfirmed();
            fillNestedFields(df, orders);
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
        return orders;
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

    public static Map<Integer, List<Order>> getRangeCurrent(Pagination pagination) {
        Map<Integer, List<Order>> orderMap = new HashMap<>(1);
        List<Order> orders;
        Integer count;
        try (DaoFactory df = DaoFactory.getInstance()) {
            OrderDao orderDao = df.getOrderDao();
            count = orderDao.count();
            checkPagination(pagination, count);
            orders = orderDao.getRangeCurrent(pagination);
            fillNestedFields(df, orders);
            orderMap.put(count, orders);
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
        return orderMap;
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

            User librarian = null;
            if (order.getLibrarian() != null) {
                librarian = userDao.getById(order.getLibrarian().getId());
            }

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
