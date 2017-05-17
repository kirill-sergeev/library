package ua.nure.serhieiev.library.service;

import ua.nure.serhieiev.library.dao.DaoFactory;
import ua.nure.serhieiev.library.dao.OrderDao;
import ua.nure.serhieiev.library.dao.UserDao;
import ua.nure.serhieiev.library.model.Order;
import ua.nure.serhieiev.library.model.User;

import java.util.List;

public final class OrderService {

    public static Order save(Order order) {
        try (DaoFactory df = DaoFactory.getInstance()) {
            OrderDao orderDao = df.getOrderDao();
            UserDao userDao = df.getUserDao();
            User user = userDao.getById(order.getUser().getId());
            order.setUser(user);
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
            User user = userDao.getById(order.getUser().getId());
            order.setUser(user);
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
                User user = userDao.getById(order.getUser().getId());
                order.setUser(user);
            }
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
        return orders;
    }

    private OrderService() {
    }



}
