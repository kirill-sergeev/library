package ua.nure.serhieiev.library.service;

import ua.nure.serhieiev.library.dao.DaoFactory;
import ua.nure.serhieiev.library.dao.NotFoundException;
import ua.nure.serhieiev.library.dao.UserDao;
import ua.nure.serhieiev.library.model.Pagination;
import ua.nure.serhieiev.library.model.entities.User;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public final class UserService {

    public static User saveReader(User user) {
        String activationToken = UUID.randomUUID().toString();
        try (DaoFactory df = DaoFactory.getInstance()) {
            UserDao userDao = df.getUserDao();
                user.setRole(User.Role.READER)
                        .setActivationToken(activationToken)
                        .setEnabled(false);
            userDao.save(user);
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
        return user;
    }

    public static User saveLibrarian(User user) {
        try (DaoFactory df = DaoFactory.getInstance()) {
            UserDao userDao = df.getUserDao();
            user.setRole(User.Role.LIBRARIAN)
                    .setEnabled(true);
            userDao.save(user);
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
        return user;
    }

    public static User updateProfile(User user) {
        try (DaoFactory df = DaoFactory.getInstance()) {
            UserDao userDao = df.getUserDao();
            userDao.update(user);
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

    public static User authenticate(User user) {
        User registeredUser;
        try (DaoFactory df = DaoFactory.getInstance()) {
            UserDao userDao = df.getUserDao();
            if (user.getEmail() == null && user.getAuthToken() != null) {
                registeredUser = getUniqueMatching(userDao, user);
            } else {
                registeredUser = getUniqueMatching(userDao, user);
                if (!registeredUser.getPassword().equals(user.getPassword())) {
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

    public static User activate(User user) {
        User registeredUser;
        try (DaoFactory df = DaoFactory.getInstance()) {
            UserDao userDao = df.getUserDao();
            registeredUser = getUniqueMatching(userDao, user)
                    .setEnabled(true)
                    .setActivationToken(null);
            userDao.update(registeredUser);
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
        return registeredUser;
    }

    public static User block(User user) {
        User registeredUser;
        try (DaoFactory df = DaoFactory.getInstance()) {
            UserDao userDao = df.getUserDao();
            registeredUser = getUniqueMatching(userDao, user)
                    .setEnabled(false)
                    .setActivationToken(null);
            userDao.update(registeredUser);
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
        return registeredUser;
    }

    public static User resetPassword(User user) {
        User registeredUser;
        try (DaoFactory df = DaoFactory.getInstance()) {
            UserDao userDao = df.getUserDao();
            registeredUser = getUniqueMatching(userDao, user)
                    .setResetPasswordToken(UUID.randomUUID().toString());
            userDao.update(registeredUser);
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
        return registeredUser;
    }

    public static User changePassword(User user) {
        User registeredUser;
        try (DaoFactory df = DaoFactory.getInstance()) {
            UserDao userDao = df.getUserDao();
            registeredUser = getUniqueMatching(userDao, user)
                    .setResetPasswordToken(null)
                    .setPassword(user.getPassword());
            userDao.update(registeredUser);
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
        return registeredUser;
    }

    public static List<User> getByRole(Pagination pagination, User.Role role) {
        List<User> users;
        try (DaoFactory df = DaoFactory.getInstance()) {
            UserDao userDao = df.getUserDao();
            users = userDao.getAll(pagination, role);
        }catch (Exception e) {
            throw new ApplicationException(e);
        }
        return users;
    }

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

    private UserService() {
    }

}
