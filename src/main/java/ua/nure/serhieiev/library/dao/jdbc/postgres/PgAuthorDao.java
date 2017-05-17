package ua.nure.serhieiev.library.dao.jdbc.postgres;

import ua.nure.serhieiev.library.dao.DaoException;
import ua.nure.serhieiev.library.dao.NotFoundException;
import ua.nure.serhieiev.library.dao.AuthorDao;
import ua.nure.serhieiev.library.dao.jdbc.JdbcDao;
import ua.nure.serhieiev.library.model.Author;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PgAuthorDao extends JdbcDao<Author> implements AuthorDao {

    private static final String NAME = "name";

    private static final String SQL_CREATE_AUTHOR = "INSERT INTO authors (name) VALUES (?)";
    private static final String SQL_DELETE_AUTHOR = "DELETE FROM authors WHERE id = ?";
    private static final String SQL_UPDATE_AUTHOR = "UPDATE authors SET name = ? WHERE id = ?";
    private static final String SQL_SELECT_ALL_AUTHORS = "SELECT * FROM authors";
    private static final String SQL_SELECT_AUTHOR_BY_ID = "SELECT * FROM authors WHERE id = ?";

    @Override
    protected String getSelectQuery() {
        return SQL_SELECT_AUTHOR_BY_ID;
    }
    @Override
    protected String getSelectAllQuery() {
        return SQL_SELECT_ALL_AUTHORS;
    }
    @Override
    protected String getCreateQuery() {
        return SQL_CREATE_AUTHOR;
    }
    @Override
    protected String getUpdateQuery() {
        return SQL_UPDATE_AUTHOR;
    }
    @Override
    protected String getDeleteQuery() {
        return SQL_DELETE_AUTHOR;
    }


    @Override
    protected List<Author> parseResultSet(ResultSet rs) {
        List<Author> list = new ArrayList<>();
        try {
            while (rs.next()) {
                Author author = new Author()
                        .setId(rs.getInt(ID))
                        .setName(rs.getString(NAME));
                list.add(author);
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
    protected void prepareStatementForInsert(PreparedStatement st, Author author) {
        try {
            st.setString(1, author.getName());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement st, Author author) {
        try {
            st.setString(1, author.getName());
            st.setInt(2, author.getId());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    PgAuthorDao(Connection con) {
        super(con);
    }

}