package ua.nure.serhieiev.library.dao.jdbc.postgres;

import ua.nure.serhieiev.library.dao.DaoException;
import ua.nure.serhieiev.library.dao.NotFoundException;
import ua.nure.serhieiev.library.dao.UserDao;
import ua.nure.serhieiev.library.dao.jdbc.JdbcDao;
import ua.nure.serhieiev.library.model.Author;
import ua.nure.serhieiev.library.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PgUserDao extends JdbcDao<User> implements UserDao {

    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String ACTIVATION_TOKEN = "activation_token";
    private static final String RESET_TOKEN = "reset_token";
    private static final String AUTH_TOKEN = "auth_token";
    private static final String ENABLED = "enabled";
    private static final String REGISTRATION_DATE = "registration_date";
    private static final String LAST_VISIT = "last_visit";
    private static final String ROLE = "role_id";
    private static final String[] SORT_FIELDS = {ENABLED, LAST_VISIT, NAME, REGISTRATION_DATE, ROLE};

    private static final String SQL_CREATE_USER = "INSERT INTO users (email, password, name, activation_token, role_id, enabled) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_SELECT_USER_BY_EMAIL = "SELECT * FROM users WHERE email = ?";
    private static final String SQL_SELECT_USER_BY_NAME = "SELECT * FROM users WHERE lower(name) LIKE (?)";
    private static final String SQL_SELECT_USER_BY_ROLE = "SELECT * FROM users WHERE role_id = ? ORDER BY name";
    private static final String SQL_SELECT_USER_BY_AUTH_TOKEN = "SELECT * FROM users WHERE auth_token = ?";
    private static final String SQL_SELECT_USER_BY_ACTIVATION_TOKEN = "SELECT * FROM users WHERE activation_token = ?";
    private static final String SQL_SELECT_USER_BY_RESET_PASSWORD_TOKEN = "SELECT * FROM users WHERE reset_token = ?";
    private static final String SQL_UPDATE_USER = "UPDATE users SET email = ?, password = ?, name = ?, auth_token = ?, activation_token = ?, reset_token = ?, last_visit = ?, enabled = ? WHERE id = ?";

    @Override
    protected String getCreateQuery() {
        return SQL_CREATE_USER;
    }
    @Override
    protected String getUpdateQuery() {
        return SQL_UPDATE_USER;
    }
    @Override
    protected String[] getSortFields() {
        return SORT_FIELDS.clone();
    }

    @Override
    protected List<User> parseResultSet(ResultSet rs) {
        List<User> list = new ArrayList<>();
        try {
            while (rs.next()) {
                User user = new User()
                        .setId(rs.getInt(ID))
                        .setName(rs.getString(NAME))
                        .setEmail(rs.getString(EMAIL))
                        .setPassword(rs.getString(PASSWORD))
                        .setActivationToken(rs.getString(ACTIVATION_TOKEN))
                        .setResetPasswordToken(rs.getString(RESET_TOKEN))
                        .setAuthToken(rs.getString(AUTH_TOKEN))
                        .setEnabled(rs.getBoolean(ENABLED))
                        .setRegistrationDate(rs.getDate(REGISTRATION_DATE).toLocalDate())
                        .setRole(User.Role.values()[rs.getInt(ROLE)])
                        .setLastVisit(rs.getDate(LAST_VISIT).toLocalDate());
                list.add(user);
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        if (list.isEmpty()) {
            throw new NotFoundException();
        }
        return list;
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement st, User user) {
        try {
            st.setString(1, user.getEmail());
            st.setString(2, user.getPassword());
            st.setString(3, user.getName());
            st.setString(4, user.getActivationToken());
            st.setInt(5, user.getRole().ordinal());
            st.setBoolean(6, user.getEnabled());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement st, User user) {
        try {
            st.setString(1, user.getEmail());
            st.setString(2, user.getPassword());
            st.setString(3, user.getName());
            st.setString(4, user.getAuthToken());
            st.setString(5, user.getActivationToken());
            st.setString(6, user.getResetPasswordToken());
            st.setDate(7, Date.valueOf(user.getLastVisit()));
            st.setBoolean(8, user.getEnabled());
            st.setInt(9, user.getId());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<User> getAll(User.Role role) {
        return listQuery(SQL_SELECT_USER_BY_ROLE, role.ordinal());
    }

    @Override
    public User getByAuthToken(String authToken) {
        return singleQuery(SQL_SELECT_USER_BY_AUTH_TOKEN, authToken);
    }

    @Override
    public User getByActivationToken(String activationToken) {
        return singleQuery(SQL_SELECT_USER_BY_ACTIVATION_TOKEN, activationToken);
    }

    @Override
    public User getByResetPasswordToken(String resetPasswordToken) {
        return singleQuery(SQL_SELECT_USER_BY_RESET_PASSWORD_TOKEN, resetPasswordToken);
    }

    @Override
    public User getByEmail(String email) {
        return singleQuery(SQL_SELECT_USER_BY_EMAIL, email);
    }

    @Override
    public List<User> getByName(String name) {
        return listQuery(SQL_SELECT_USER_BY_NAME, "%" + name.toLowerCase() + "%");
    }

    PgUserDao(Connection con) {
        super(con);
    }

}