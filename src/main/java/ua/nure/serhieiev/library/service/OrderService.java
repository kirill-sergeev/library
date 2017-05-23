package ua.nure.serhieiev.library.service;

import ua.nure.serhieiev.library.dao.DaoFactory;
import ua.nure.serhieiev.library.dao.OrderDao;
import ua.nure.serhieiev.library.dao.UserDao;
import ua.nure.serhieiev.library.model.Order;
import ua.nure.serhieiev.library.model.User;

import java.util.List;

import static ua.nure.serhieiev.library.model.User.Role.LIBRARIAN;
import static ua.nure.serhieiev.library.model.User.Role.READER;

public final class OrderService {

    public static Order save(Order order) {
        try (DaoFactory df = DaoFactory.getInstance()) {
            OrderDao orderDao = df.getOrderDao();
            UserDao userDao = df.getUserDao();
            User reader = userDao.getById(order.getReader().getId());
            User librarian = userDao.getById(order.getLibrarian().getId());
            if (reader.getRole() != READER || librarian.getRole() != LIBRARIAN){
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
            UserDao userDao = df.getUserDao();
            orders = orderDao.getAll();
            for (Order order : orders) {
                User reader = userDao.getById(order.getReader().getId());
                User librarian = userDao.getById(order.getLibrarian().getId());
                order.setReader(reader)
                        .setLibrarian(librarian);
            }
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
        return orders;
    }

    private OrderService() {
    }



}
