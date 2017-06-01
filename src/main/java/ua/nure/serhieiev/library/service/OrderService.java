package ua.nure.serhieiev.library.service;

import ua.nure.serhieiev.library.dao.*;
import ua.nure.serhieiev.library.model.Pagination;
import ua.nure.serhieiev.library.model.entities.Book;
import ua.nure.serhieiev.library.model.entities.Identified;
import ua.nure.serhieiev.library.model.entities.Order;
import ua.nure.serhieiev.library.model.entities.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ua.nure.serhieiev.library.model.entities.User.Role.LIBRARIAN;
import static ua.nure.serhieiev.library.model.entities.User.Role.READER;

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
            LocalDate expectedDate = order.getExpectedDate();
            order = orderDao.getById(order.getId());
            order.setOrderDate(LocalDate.now());
            order.setExpectedDate(expectedDate);
            order.setLibrarian(librarian);
            orderDao.update(order);
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
        return order;
    }

    public static void rejectOrder(Order order) {
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
            order = orderDao.getById(orderId);
            fillNestedFields(df, Collections.singletonList(order));
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
        return order;
    }

    public static List<Order> getUnconfirmed(Pagination pagination) {
        List<Order> orders;
        try (DaoFactory df = DaoFactory.getInstance()) {
            OrderDao orderDao = df.getOrderDao();
            orders = orderDao.getUnconfirmed(pagination);
            fillNestedFields(df, orders);
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
        return orders;
    }

    public static List<Order> getCurrent(Pagination pagination) {
        List<Order> orders;
        try (DaoFactory df = DaoFactory.getInstance()) {
            OrderDao orderDao = df.getOrderDao();
            orders = orderDao.getCurrent(pagination);
            fillNestedFields(df, orders);
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
        return orders;
    }

    public static List<Order> getClosed(Pagination pagination) {
        List<Order> orders;
        try (DaoFactory df = DaoFactory.getInstance()) {
            OrderDao orderDao = df.getOrderDao();
            orders = orderDao.getClosed(pagination);
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

    public static List<Order> getAll(Pagination pagination) {
        return getAll(pagination, null);
    }

    private static List<Order> getAll(Pagination pagination, Identified object) {
        List<Order> orders;
        try (DaoFactory df = DaoFactory.getInstance()) {
            OrderDao orderDao = df.getOrderDao();
            if (object == null) {
                orders = orderDao.getAll(pagination);
            } else if (object instanceof User) {
                orders = orderDao.getByReader(object.getId());
            } else {
                throw new IllegalArgumentException("Object must be an Author, Genre, Publisher or nothing.");
            }
            fillNestedFields(df, orders);
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
        return orders;
    }

    public static List<Order> getByReader(User reader) {
        return getAll(null, reader);
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
