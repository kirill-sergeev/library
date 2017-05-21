package ua.nure.serhieiev.library.dao.jdbc.postgres;

import ua.nure.serhieiev.library.dao.DaoException;
import ua.nure.serhieiev.library.dao.NotFoundException;
import ua.nure.serhieiev.library.dao.GenreDao;
import ua.nure.serhieiev.library.dao.jdbc.JdbcDao;
import ua.nure.serhieiev.library.model.Genre;
import ua.nure.serhieiev.library.service.util.Pagination;

import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PgGenreDao extends JdbcDao<Genre> implements GenreDao {

    private static final String TITLE = "title";

    private static final String SQL_CREATE_GENRE = "INSERT INTO genres (title) VALUES (?)";
    private static final String SQL_UPDATE_GENRE = "UPDATE genres SET title = ? WHERE id = ?";
    private static final String SQL_SELECT_GENRE_BY_ID = "SELECT * FROM genres WHERE id = ?";

    @Override
    protected String getSelectQuery() {
        return SQL_SELECT_GENRE_BY_ID;
    }
    @Override
    protected String getCreateQuery() {
        return SQL_CREATE_GENRE;
    }
    @Override
    protected String getUpdateQuery() {
        return SQL_UPDATE_GENRE;
    }

    @Override
    protected List<Genre> parseResultSet(ResultSet rs) {
        List<Genre> list = new ArrayList<>();
        try {
            while (rs.next()) {
                Genre genre = new Genre()
                        .setId(rs.getInt(ID))
                        .setTitle(rs.getString(TITLE));
                list.add(genre);
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
    protected void prepareStatementForInsert(PreparedStatement st, Genre genre) {
        try {
            st.setString(1, genre.getTitle());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement st, Genre genre) {
        try {
            st.setString(1, genre.getTitle());
            st.setInt(2, genre.getId());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Genre> getRange(Pagination pagination) {
        String order = pagination.isAscending() ? " " : " DESC";
        int limit = pagination.getLimit();
        int offset = pagination.getOffset();
        String sortBy = pagination.getSortBy().isEmpty() ? "title" : pagination.getSortBy();

        String sql = "SELECT * FROM genres" +
                " ORDER BY " + sortBy + order +
                " LIMIT ? OFFSET ?";
        return listQuery(sql, limit, offset);
    }

    PgGenreDao(Connection con) {
        super(con);
    }

}