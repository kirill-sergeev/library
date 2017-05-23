package ua.nure.serhieiev.library.dao.jdbc;

import ua.nure.serhieiev.library.dao.DaoException;
import ua.nure.serhieiev.library.dao.GenericDao;
import ua.nure.serhieiev.library.dao.NotFoundException;
import ua.nure.serhieiev.library.model.Book;
import ua.nure.serhieiev.library.model.Identified;
import ua.nure.serhieiev.library.service.util.Pagination;

import java.lang.reflect.ParameterizedType;
import java.sql.*;
import java.util.Arrays;
import java.util.List;

public abstract class JdbcDao<T extends Identified> implements GenericDao<T> {

    private Class<T> entityClass;
    protected static final String ID = "id";
    protected Connection con;


    protected String getSelectQuery(){
        return String.format( "SELECT * FROM %s WHERE id = ?", getTableName());
    }
    protected String getSelectAllQuery(){
        return String.format("SELECT * FROM %s", getTableName());
    }
    protected abstract String getCreateQuery();
    protected abstract String getUpdateQuery();
    protected abstract String[] getSortFields();
    protected abstract List<T> parseResultSet(ResultSet rs);
    protected abstract void prepareStatementForInsert(PreparedStatement st, T object);
    protected abstract void prepareStatementForUpdate(PreparedStatement st, T object);

    @SuppressWarnings("unchecked")
    protected JdbcDao(Connection con) {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        this.entityClass = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
        this.con = con;
    }

    @Override
    public int count() {
        int count;
        String sql = String.format("SELECT count(*) FROM %s", getTableName());
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            rs.next();
            count = rs.getInt(1);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return count;
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
        String sql = String.format("DELETE FROM %s WHERE id = ?", getTableName());
        try (PreparedStatement st = con.prepareStatement(sql)) {
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

    @Override
    public List<T> getRange(Pagination pagination) {
        int limit = pagination.getLimit();
        int offset = pagination.getOffset();
        String order = pagination.isAscending() ? "" : "DESC";
        String sortBy = pagination.getSortBy();
        if (sortBy == null || sortBy.isEmpty() || Arrays.binarySearch(getSortFields(), sortBy) == -1){
            sortBy = getSortFields()[0];
        }
        String sql = String.format("%s ORDER BY %s %s LIMIT ? OFFSET ?", getSelectAllQuery(), sortBy, order);
        return listQuery(sql, limit, offset);
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
        if (list.isEmpty()) {
            throw new NotFoundException();
        } else if (list.size() > 1) {
            throw new DaoException("Query returned several values, one expected.");
        }
        return list.get(0);
    }

    protected static boolean hasColumn(ResultSet rs, String columnName) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columns = metaData.getColumnCount();
        for (int x = 1; x <= columns; x++) {
            if (columnName.equals(metaData.getColumnName(x))) {
                return true;
            }
        }
        return false;
    }

    private String getTableName() {
        return entityClass.getSimpleName().concat("s").toLowerCase();
    }

}