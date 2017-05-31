package ua.nure.serhieiev.library.service;

import ua.nure.serhieiev.library.dao.DaoFactory;
import ua.nure.serhieiev.library.dao.NotFoundException;
import ua.nure.serhieiev.library.dao.UserDao;
import ua.nure.serhieiev.library.model.Pagination;
import ua.nure.serhieiev.library.model.entities.User;
import ua.nure.serhieiev.library.service.util.PasswordAuthentication;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public final class UserService {

    /**
     * Saves the reader in data store.
     *
     * @param reader you want to save.
     * @return this user with ID.
     */
    public static User saveReader(User reader) {
        String activationToken = UUID.randomUUID().toString();
        reader.setRole(User.Role.READER)
                .setActivationToken(activationToken)
                .setEnabled(false);
        return save(reader);
    }

    /**
     * Saves the librarian in data store.
     *
     * @param librarian you want to save.
     * @return this user with ID.
     */
    public static User saveLibrarian(User librarian) {
        librarian.setRole(User.Role.LIBRARIAN)
                .setEnabled(true);
        return save(librarian);
    }

    private static User save(User user) {
        try (DaoFactory df = DaoFactory.getInstance()) {
            UserDao userDao = df.getUserDao();
            hashPassword(user);
            userDao.save(user);
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
        return user;
    }

    /**
     * Activates the user account.
     *
     * @param user you want to activate.
     */
    public static void activate(User user) {
        try (DaoFactory df = DaoFactory.getInstance()) {
            UserDao userDao = df.getUserDao();
            user = getUniqueMatching(userDao, user)
                    .setEnabled(true)
                    .setActivationToken(null);
            userDao.update(user);
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
    }

    /**
     * Blocks the user account.
     *
     * @param user you want to block.
     */
    public static void block(User user) {
        try (DaoFactory df = DaoFactory.getInstance()) {
            UserDao userDao = df.getUserDao();
            user = getUniqueMatching(userDao, user)
                    .setEnabled(false)
                    .setActivationToken(null);
            userDao.update(user);
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
    }

    /**
     * Sets the resetPasswordToken attribute value.
     *
     * @param user, whose password needs to reset.
     */
    public static void resetPassword(User user) {
        try (DaoFactory df = DaoFactory.getInstance()) {
            UserDao userDao = df.getUserDao();
            user = getUniqueMatching(userDao, user)
                    .setResetPasswordToken(UUID.randomUUID().toString());
            userDao.update(user);
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
    }

    /**
     * Sets the new password to existing user and changes resetPasswordToken to null.
     *
     * @param user, whose password needs to change.
     */
    public static void changePassword(User user) {
        try (DaoFactory df = DaoFactory.getInstance()) {
            UserDao userDao = df.getUserDao();
            user = getUniqueMatching(userDao, user)
                    .setResetPasswordToken(null);
            hashPassword(user);
            userDao.update(user);
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
    }

    /**
     * Removes the existing user from data store.
     *
     * @param user you want to remove.
     */
    public static void remove(User user) {
        try (DaoFactory df = DaoFactory.getInstance()) {
            UserDao userDao = df.getUserDao();
            userDao.remove(user.getId());
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
    }

    /**
     * Looking for user with same authToken or email and password in data store.
     * If user exist and authToken passed in value object, sets that token.
     *
     * @return registered user.
     */
    public static User authenticate(User user) {
        User registeredUser;
        try (DaoFactory df = DaoFactory.getInstance()) {
            UserDao userDao = df.getUserDao();
            if (user.getEmail() == null && user.getAuthToken() != null) {
                registeredUser = getUniqueMatching(userDao, user);
            } else {
                registeredUser = getUniqueMatching(userDao, user);
                if (!checkPassword(registeredUser, user.getPassword())) {
                    throw new ApplicationException("Bad password!");
                }
                if (user.getAuthToken() != null) {
                    registeredUser.setAuthToken(user.getAuthToken());
                }
            }
            registeredUser.setLastVisit(LocalDate.now());
            userDao.update(registeredUser);
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
        return registeredUser;
    }

    /**
     * Get users from data store with same role.
     * Sort, order and range must be determined in pagination.
     */
    public static List<User> getByRole(Pagination pagination, User.Role role) {
        List<User> users;
        try (DaoFactory df = DaoFactory.getInstance()) {
            UserDao userDao = df.getUserDao();
            users = userDao.getAll(pagination, role);
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
        return users;
    }

    /**
     * Looking for user in data store with same unique fields (id, email,
     * authToken, resetPasswordToken, activationToken). One of this fields
     * must be presented, other fields will be ignored.
     *
     * @param user with unique field.
     * @return user with unique field and all other fields, if exist in the data store.
     */
    public static User getUniqueMatching(User user) {
        User registeredUser;
        try (DaoFactory df = DaoFactory.getInstance()) {
            UserDao userDao = df.getUserDao();
            registeredUser = getUniqueMatching(userDao, user);
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
        return registeredUser;
    }

    private static User getUniqueMatching(UserDao userDao, User user) {
        Integer id;
        String email;
        String authToken;
        String resetPasswordToken;
        String activationToken;

        User registeredUser;
        if ((id = user.getId()) != null) {
            registeredUser = userDao.getById(id);
        } else if ((email = user.getEmail()) != null) {
            registeredUser = userDao.getByEmail(email);
        } else if ((authToken = user.getAuthToken()) != null) {
            registeredUser = userDao.getByAuthToken(authToken);
        } else if ((resetPasswordToken = user.getResetPasswordToken()) != null) {
            registeredUser = userDao.getByResetPasswordToken(resetPasswordToken);
        } else if ((activationToken = user.getActivationToken()) != null) {
            registeredUser = userDao.getByActivationToken(activationToken);
        } else {
            throw new ApplicationException("User must contains at least one unique field!");
        }
        return registeredUser;
    }

    private static void hashPassword(User user) {
        PasswordAuthentication pa = new PasswordAuthentication();
        String hashedPassword = pa.hash(user.getPassword().toCharArray());
        user.setPassword(hashedPassword);
    }

    private static boolean checkPassword(User user, String password) {
        PasswordAuthentication pa = new PasswordAuthentication();
        String hashedPassword = user.getPassword();
        return pa.authenticate(password.toCharArray(), hashedPassword);
    }

    private UserService() {
    }

}
