package ua.nure.serhieiev.library.dao;

import ua.nure.serhieiev.library.model.Pagination;
import ua.nure.serhieiev.library.model.entities.User;

import java.util.List;

public interface UserDao extends GenericDao<User> {

    User getByEmail(String email);

    User getByAuthToken(String authToken);

    User getByActivationToken(String activationToken);

    User getByResetPasswordToken(String resetPasswordToken);

    List<User> getByName(String name);

    List<User> getAll(Pagination pagination, User.Role role);

}