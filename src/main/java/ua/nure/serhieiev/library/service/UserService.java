package ua.nure.serhieiev.library.service;

import ua.nure.serhieiev.library.dao.DaoFactory;
import ua.nure.serhieiev.library.dao.UserDao;
import ua.nure.serhieiev.library.model.User;

import java.util.List;
import java.util.UUID;

public final class UserService {

    public static User save(User user) {
        try (DaoFactory df = DaoFactory.getInstance()) {
            UserDao userDao = df.getUserDao();
            user.setRole(User.Role.READER)
                    .setActivationToken(UUID.randomUUID().toString());
            userDao.save(user);
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
        return user;
    }

    public static void remove(User user) {
        try (DaoFactory df = DaoFactory.getInstance()) {
            UserDao userDao = df.getUserDao();
            userDao.remove(user.getId());
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
    }

    public static User getById(int id) {
        User user;
        try (DaoFactory df = DaoFactory.getInstance()) {
            UserDao userDao = df.getUserDao();
            user = userDao.getById(id);
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
        return user;
    }

    public static List<User> getAll() {
        List<User> users;
        try (DaoFactory df = DaoFactory.getInstance()) {
            UserDao userDao = df.getUserDao();
            users = userDao.getAll();
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
        return users;
    }

    private UserService() {
    }

}
