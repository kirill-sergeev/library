package ua.nure.serhieiev.library.dao.jdbc.postgres;

import ua.nure.serhieiev.library.dao.DaoException;
import ua.nure.serhieiev.library.dao.NotFoundException;
import ua.nure.serhieiev.library.dao.PublisherDao;
import ua.nure.serhieiev.library.dao.jdbc.JdbcDao;
import ua.nure.serhieiev.library.model.Publisher;
import ua.nure.serhieiev.library.service.util.Pagination;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PgPublisherDao extends JdbcDao<Publisher> implements PublisherDao {

    private static final String TITLE = "title";

    private static final String SQL_CREATE_PUBLISHER = "INSERT INTO publishers (title) VALUES (?)";
    private static final String SQL_UPDATE_PUBLISHER = "UPDATE publishers SET title = ? WHERE id = ?";
    private static final String SQL_SELECT_PUBLISHER_BY_ID = "SELECT * FROM publishers WHERE id = ?";

    @Override
    protected String getSelectQuery() {
        return SQL_SELECT_PUBLISHER_BY_ID;
    }
    @Override
    protected String getCreateQuery() {
        return SQL_CREATE_PUBLISHER;
    }
    @Override
    protected String getUpdateQuery() {
        return SQL_UPDATE_PUBLISHER;
    }


    @Override
    protected List<Publisher> parseResultSet(ResultSet rs) {
        List<Publisher> list = new ArrayList<>();
        try {
            while (rs.next()) {
                Publisher publisher = new Publisher()
                        .setId(rs.getInt(ID))
                        .setTitle(rs.getString(TITLE));
                list.add(publisher);
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
    protected void prepareStatementForInsert(PreparedStatement st, Publisher publisher) {
        try {
            st.setString(1, publisher.getTitle());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement st, Publisher publisher) {
        try {
            st.setString(1, publisher.getTitle());
            st.setInt(2, publisher.getId());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Publisher> getRange(Pagination pagination) {
        String order = pagination.isAscending() ? " " : " DESC";
        int limit = pagination.getLimit();
        int offset = pagination.getOffset();
        String sortBy = pagination.getSortBy().isEmpty() ? "title" : pagination.getSortBy();

        String sql = "SELECT * FROM publishers" +
                " ORDER BY " + sortBy + order +
                " LIMIT ? OFFSET ?";
        return listQuery(sql, limit, offset);
    }

    PgPublisherDao(Connection con) {
        super(con);
    }

}