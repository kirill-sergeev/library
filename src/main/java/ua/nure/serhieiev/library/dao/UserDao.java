package ua.nure.serhieiev.library.dao;

import ua.nure.serhieiev.library.model.User;

import java.util.List;

public interface UserDao extends GenericDao<User> {

    User getByEmail(String email);

    User getByAuthToken(String authToken);

    User getByActivationToken(String activationToken);

    User getByResetPasswordToken(String resetPasswordToken);

    List<User> getAll(User.Role role);

    List<User> getByName(String name);

}