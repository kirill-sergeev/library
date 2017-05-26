package ua.nure.serhieiev.library.dao.jdbc.postgres;

import ua.nure.serhieiev.library.dao.DaoException;
import ua.nure.serhieiev.library.dao.NotFoundException;
import ua.nure.serhieiev.library.dao.PublisherDao;
import ua.nure.serhieiev.library.dao.jdbc.JdbcDao;
import ua.nure.serhieiev.library.model.Publisher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PgPublisherDao extends JdbcDao<Publisher> implements PublisherDao {

    private static final String TITLE = "title";
    private static final String[] SORT_FIELDS = {TITLE};

    private static final String SQL_CREATE_PUBLISHER = "INSERT INTO publishers (title) VALUES (?)";
    private static final String SQL_UPDATE_PUBLISHER = "UPDATE publishers SET title = ? WHERE id = ?";
    private static final String SQL_SELECT_PUBLISHER_BY_TITLE = "SELECT * FROM publishers WHERE lower(title) LIKE (?)";

    @Override
    protected String getCreateQuery() {
        return SQL_CREATE_PUBLISHER;
    }
    @Override
    protected String getUpdateQuery() {
        return SQL_UPDATE_PUBLISHER;
    }
    @Override
    protected String[] getSortFields() {
        return SORT_FIELDS.clone();
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
    public List<Publisher> getByTitle(String title) {
        return listQuery(SQL_SELECT_PUBLISHER_BY_TITLE, "%" + title.toLowerCase() + "%");
    }

    PgPublisherDao(Connection con) {
        super(con);
    }

}