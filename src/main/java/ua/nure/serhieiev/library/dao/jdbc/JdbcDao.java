package ua.nure.serhieiev.library.dao.jdbc;

import ua.nure.serhieiev.library.dao.DaoException;
import ua.nure.serhieiev.library.dao.GenericDao;
import ua.nure.serhieiev.library.dao.NotFoundException;
import ua.nure.serhieiev.library.model.entities.Identified;
import ua.nure.serhieiev.library.model.Pagination;

import java.lang.reflect.ParameterizedType;
import java.sql.*;
import java.util.Arrays;
import java.util.List;

public abstract class JdbcDao<T extends Identified> implements GenericDao<T> {

    private Class<T> entityClass;
    protected static final String ID = "id";
    protected Connection con;


    protected String getSelectQuery() {
        return String.format("SELECT * FROM %s WHERE id = ?", getTableName());
    }

    protected String getSelectAllQuery() {
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
        checkId(object);
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
        checkId(id);
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
        checkId(id);
        return singleQuery(getSelectQuery(), id);
    }

    @Override
    public List<T> getAll() {
        return listQuery(getSelectAllQuery());
    }

    @Override
    public List<T> getAll(Pagination pagination) {
        return getAll(pagination, null);
    }

    protected List<T> getAll(Pagination pagination, String sql, Object... params) {
        int limit = pagination.getLimit();
        int offset = pagination.getOffset();
        String order = pagination.isAscending() ? "" : "DESC";
        String sortBy = pagination.getSortBy();
        String[] sortedFields = getSortFields();
        if (sortBy == null || sortBy.isEmpty() || Arrays.binarySearch(sortedFields, sortBy) == -1) {
            sortBy = sortedFields[0];
        }
        String sqlWithPagination = String.format("%s ORDER BY %s %s LIMIT ? OFFSET ?",
                sql == null ? getSelectAllQuery() : sql, sortBy, order);
        if (pagination.getNumberOfItems() == null) {
            pagination.setNumberOfItems(count());
        }
        Object[] returnParams = Arrays.copyOf(params, params.length + 2);
        returnParams[params.length] = limit;
        returnParams[params.length + 1] = offset;
        return listQuery(sqlWithPagination, returnParams);
    }

    @Override
    public Integer count() {
        return count(null);
    }

    protected Integer count(String sql, Object... params) {
        if (sql == null || sql.isEmpty()) {
            sql = String.format("SELECT count(*) FROM %s", getTableName());
        }
        int count;
        try (PreparedStatement st = con.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                st.setObject(i + 1, params[i]);
            }
            ResultSet rs = st.executeQuery();
            rs.next();
            count = rs.getInt(1);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return count;
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
            throw new DaoException("Query returned several values, but one expected.");
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

    protected static void checkId(Integer id) {
        if (id == null || id < 1) {
            throw new IllegalArgumentException("Entity must contains ID.");
        }
    }

    protected static void checkId(Identified object) {
        if (object == null || object.getId() == null || object.getId() < 1) {
            throw new IllegalArgumentException("Entity must contains ID.");
        }
    }

    private String getTableName() {
        return entityClass.getSimpleName().concat("s").toLowerCase();
    }

}