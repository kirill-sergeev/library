package ua.nure.serhieiev.library.dao.jdbc;

import ua.nure.serhieiev.library.dao.DaoException;
import ua.nure.serhieiev.library.dao.GenericDao;
import ua.nure.serhieiev.library.dao.NotFoundException;
import ua.nure.serhieiev.library.model.Identified;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public abstract class JdbcDao<T extends Identified> implements GenericDao<T> {

    protected static final String ID = "id";

    protected Connection con;
    protected abstract String getCreateQuery();
    protected abstract String getDeleteQuery();
    protected abstract String getSelectAllQuery();
    protected abstract String getSelectQuery();
    protected abstract String getUpdateQuery();
    protected abstract List<T> parseResultSet(ResultSet rs);
    protected abstract void prepareStatementForInsert(PreparedStatement st, T object);
    protected abstract void prepareStatementForUpdate(PreparedStatement st, T object);

    protected JdbcDao(Connection con) {
        this.con = con;
    }

    @Override
    public void save(T object) {
        if (object == null || object.getId() != null) {
            throw new IllegalArgumentException("Bad input.");
        }
        try (PreparedStatement st = con.prepareStatement(getCreateQuery(),
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            prepareStatementForInsert(st, object);
            if (st.executeUpdate() == 0) {
                throw new DaoException("Creating entity failed, no rows affected.");
            }
            ResultSet rs = st.getGeneratedKeys();
            if (rs.next()) {
                object.setId(rs.getInt(1));
            } else {
                throw new DaoException("Creating entity failed, no generated key obtained.");
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void update(T object) {
        if (object == null || object.getId() == null) {
            throw new IllegalArgumentException("Entity is not created yet, ID is null.");
        }
        try (PreparedStatement st = con.prepareStatement(getUpdateQuery())) {
            prepareStatementForUpdate(st, object);
            if (st.executeUpdate() == 0) {
                throw new DaoException("Updating entity failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void remove(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Entity is not created yet, ID is null.");
        }
        try (PreparedStatement st = con.prepareStatement(getDeleteQuery())) {
            st.setInt(1, id);
            if (st.executeUpdate() == 0) {
                throw new DaoException("Deleting entity failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public T getById(Integer id) {
        return singleQuery(getSelectQuery(), id);
    }

    @Override
    public List<T> getAll() {
        return listQuery(getSelectAllQuery());
    }

    protected List<T> listQuery(String sql, Object... params) {
        List<T> list;
        try (PreparedStatement st = con.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                st.setObject(i + 1, params[i]);
            }
            ResultSet rs = st.executeQuery();
            list = parseResultSet(rs);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return list;
    }

    protected T singleQuery(String sql, Object... params) {
        List<T> list = listQuery(sql, params);
        if (list.isEmpty()){
            throw new NotFoundException();
        }else if (list.size() > 1){
            throw new DaoException("Query returned several values, one expected.");
        }
        return list.get(0);
    }

}