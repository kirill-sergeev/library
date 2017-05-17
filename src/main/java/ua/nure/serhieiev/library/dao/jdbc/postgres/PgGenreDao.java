package ua.nure.serhieiev.library.dao.jdbc.postgres;

import ua.nure.serhieiev.library.dao.DaoException;
import ua.nure.serhieiev.library.dao.NotFoundException;
import ua.nure.serhieiev.library.dao.GenreDao;
import ua.nure.serhieiev.library.dao.jdbc.JdbcDao;
import ua.nure.serhieiev.library.model.Genre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PgGenreDao extends JdbcDao<Genre> implements GenreDao {

    private static final String TITLE = "title";

    private static final String SQL_CREATE_GENRE = "INSERT INTO genres (title) VALUES (?)";
    private static final String SQL_DELETE_GENRE = "DELETE FROM genres WHERE id = ?";
    private static final String SQL_UPDATE_GENRE = "UPDATE genres SET title = ? WHERE id = ?";
    private static final String SQL_SELECT_ALL_GENRES = "SELECT * FROM genres";
    private static final String SQL_SELECT_GENRE_BY_ID = "SELECT * FROM genres WHERE id = ?";

    @Override
    protected String getSelectQuery() {
        return SQL_SELECT_GENRE_BY_ID;
    }
    @Override
    protected String getSelectAllQuery() {
        return SQL_SELECT_ALL_GENRES;
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
    protected String getDeleteQuery() {
        return SQL_DELETE_GENRE;
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

    PgGenreDao(Connection con) {
        super(con);
    }

}