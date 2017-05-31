package ua.nure.serhieiev.library.dao.jdbc.postgres;

import ua.nure.serhieiev.library.dao.DaoException;
import ua.nure.serhieiev.library.dao.BookDao;
import ua.nure.serhieiev.library.dao.jdbc.JdbcDao;
import ua.nure.serhieiev.library.model.entities.Author;
import ua.nure.serhieiev.library.model.entities.Book;
import ua.nure.serhieiev.library.model.entities.Genre;
import ua.nure.serhieiev.library.model.entities.Publisher;
import ua.nure.serhieiev.library.model.Pagination;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PgBookDao extends JdbcDao<Book> implements BookDao {

    private static final String TITLE = "title";
    private static final String QUANTITY = "quantity";
    private static final String AVAILABLE = "available";
    private static final String PUBLISHER = "publisher_id";
    private static final String PUBLICATION_DATE = "publication_date";
    private static final String DESCRIPTION = "description";
    private static final String ISBN = "isbn";
    private static final String AUTHORS = "authors";
    private static final String GENRES = "genres";
    private static final String[] SORT_FIELDS = {AVAILABLE, PUBLICATION_DATE, PUBLISHER, QUANTITY, TITLE};

    private static final String SQL_COUNT_BOOKS_BY_AUTHOR = "SELECT count(*) FROM books_authors WHERE author_id = ?";
    private static final String SQL_COUNT_BOOKS_BY_GENRE = "SELECT count(*) FROM books_genres WHERE genre_id = ?";
    private static final String SQL_COUNT_BOOKS_BY_PUBLISHER = "SELECT count(*) FROM books WHERE publisher_id = ?";
    private static final String SQL_CREATE_BOOK = "INSERT INTO books (title, quantity, publisher_id, publication_date, description, isbn) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE_BOOK = "UPDATE books SET title = ?, quantity = ?, available = ?, publisher_id = ?, publication_date = ?, description = ?, isbn = ? WHERE id = ?";
    private static final String SQL_INSERT_AUTHOR_INTO_BOOK = "INSERT INTO books_authors(book_id, author_id) VALUES (?, ?)";
    private static final String SQL_INSERT_GENRE_INTO_BOOK = "INSERT INTO books_genres(book_id, genre_id) VALUES (?, ?)";
    private static final String SQL_DELETE_ALL_AUTHORS_FROM_BOOK = "DELETE FROM books_authors VALUES WHERE book_id = ?";
    private static final String SQL_DELETE_ALL_GENRES_FROM_BOOK = "DELETE FROM books_genres VALUES WHERE book_id = ?";
    private static final String SQL_SELECT_ALL_BOOKS =
            "SELECT b.*, array_agg (DISTINCT ba.author_id) AS authors, array_agg(DISTINCT bg.genre_id) AS genres" +
                    " FROM books b, books_authors ba, books_genres bg" +
                    " WHERE b.id = ba.book_id AND b.id = bg.book_id" +
                    " GROUP BY b.id";
    private static final String SQL_SELECT_BOOK_BY_ID =
            "SELECT b.*, array_agg(DISTINCT ba.author_id) AS authors, array_agg(DISTINCT bg.genre_id) AS genres" +
                    " FROM books b, books_authors ba, books_genres bg" +
                    " WHERE b.id = ba.book_id AND b.id = bg.book_id AND b.id = ?" +
                    " GROUP BY b.id";
    private static final String SQL_SELECT_BOOK_BY_AUTHOR = "SELECT * FROM books WHERE id IN (SELECT book_id FROM books_authors WHERE author_id = ?)";
    private static final String SQL_SELECT_BOOK_BY_GENRE = "SELECT * FROM books WHERE id IN (SELECT book_id FROM books_genres WHERE genre_id = ?)";
    private static final String SQL_SELECT_BOOK_BY_PUBLISHER =
            "SELECT b.*, array_agg (DISTINCT ba.author_id) AS authors, array_agg(DISTINCT bg.genre_id) AS genres" +
                    " FROM books b, books_authors ba, books_genres bg, authors a, genres g" +
                    " WHERE b.id = ba.book_id AND b.id = bg.book_id AND ba.author_id = a.id AND bg.genre_id = g.id AND b.publisher_id = ?" +
                    " GROUP BY b.id";
    private static final String SQL_SELECT_BOOK_BY_TITLE =
            "SELECT b.*, array_agg (DISTINCT ba.author_id) AS authors, array_agg(DISTINCT bg.genre_id) AS genres" +
                    " FROM books b, books_authors ba, books_genres bg" +
                    " WHERE b.id = ba.book_id AND b.id = bg.book_id AND lower(b.title) LIKE (?)" +
                    " GROUP BY b.id";

    @Override
    protected String getSelectQuery() {
        return SQL_SELECT_BOOK_BY_ID;
    }

    @Override
    protected String getSelectAllQuery() {
        return SQL_SELECT_ALL_BOOKS;
    }

    @Override
    protected String getCreateQuery() {
        return SQL_CREATE_BOOK;
    }

    @Override
    protected String getUpdateQuery() {
        return SQL_UPDATE_BOOK;
    }

    @Override
    protected String[] getSortFields() {
        return SORT_FIELDS.clone();
    }

    @Override
    protected List<Book> parseResultSet(ResultSet rs) {
        List<Book> list = new ArrayList<>();
        try {
            while (rs.next()) {
                Book book = new Book()
                        .setId(rs.getInt(ID))
                        .setTitle(rs.getString(TITLE))
                        .setPublisher(new Publisher().setId(rs.getInt(PUBLISHER)))
                        .setPublicationDate(rs.getDate(PUBLICATION_DATE).toLocalDate())
                        .setDescription(rs.getString(DESCRIPTION))
                        .setQuantity(rs.getInt(QUANTITY))
                        .setAvailable(rs.getInt(AVAILABLE))
                        .setIsbn(rs.getLong(ISBN));

                List<Author> authors = new ArrayList<>();
                if (hasColumn(rs, AUTHORS)) {
                    for (Integer authorId : (Integer[]) rs.getArray(AUTHORS).getArray()) {
                        authors.add(new Author().setId(authorId));
                    }
                }
                book.setAuthors(authors);

                List<Genre> genres = new ArrayList<>();
                if (hasColumn(rs, GENRES)) {
                    for (Integer genreId : (Integer[]) rs.getArray(GENRES).getArray()) {
                        genres.add(new Genre().setId(genreId));
                    }
                }
                book.setGenres(genres);

                list.add(book);
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return list;
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement st, Book book) {
        try {
            st.setString(1, book.getTitle());
            st.setInt(2, book.getQuantity());
            st.setInt(3, book.getPublisher().getId());
            st.setDate(4, Date.valueOf(book.getPublicationDate()));
            st.setString(5, book.getDescription());
            st.setLong(6, book.getIsbn());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement st, Book book) {
        try {
            st.setString(1, book.getTitle());
            st.setInt(2, book.getQuantity());
            st.setInt(3, book.getAvailable());
            st.setInt(4, book.getPublisher().getId());
            st.setDate(5, Date.valueOf(book.getPublicationDate()));
            st.setString(6, book.getDescription());
            st.setLong(7, book.getIsbn());
            st.setInt(8, book.getId());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private void addAuthorsToBook(Book book) {
        try (PreparedStatement st = con.prepareStatement(SQL_INSERT_AUTHOR_INTO_BOOK)) {
            for (Author author : book.getAuthors()) {
                st.setInt(1, book.getId());
                st.setInt(2, author.getId());
                st.addBatch();
            }
            st.executeBatch();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private void addGenresToBook(Book book) {
        try (PreparedStatement st = con.prepareStatement(SQL_INSERT_GENRE_INTO_BOOK)) {
            for (Genre genre : book.getGenres()) {
                st.setInt(1, book.getId());
                st.setInt(2, genre.getId());
                st.addBatch();
            }
            st.executeBatch();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private void removeAuthorsFromBook(Book book) {
        try (PreparedStatement st = con.prepareStatement(SQL_DELETE_ALL_AUTHORS_FROM_BOOK)) {
            st.setInt(1, book.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private void removeGenresFromBook(Book book) {
        try (PreparedStatement st = con.prepareStatement(SQL_DELETE_ALL_GENRES_FROM_BOOK)) {
            st.setInt(1, book.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void save(Book book) {
        super.save(book);
        addGenresToBook(book);
        addAuthorsToBook(book);
    }

    @Override
    public void update(Book book) {
        removeAuthorsFromBook(book);
        removeGenresFromBook(book);
        addAuthorsToBook(book);
        addGenresToBook(book);
        super.update(book);
    }

    @Override
    public List<Book> getByTitle(String title) {
        if (title == null || title.length() < 3){
            throw new DaoException("Title must be longer than 2 characters!");
        }
        return listQuery(SQL_SELECT_BOOK_BY_TITLE, "%" + title.toLowerCase() + "%");
    }

    @Override
    public List<Book> getByAuthor(Pagination pagination, Integer authorId) {
        checkId(authorId);
        pagination.setNumberOfItems(countByAuthor(authorId));
        return getAll(pagination, SQL_SELECT_BOOK_BY_AUTHOR, authorId);
    }

    @Override
    public List<Book> getByGenre(Pagination pagination, Integer genreId) {
        checkId(genreId);
        pagination.setNumberOfItems(countByGenre(genreId));
        return getAll(pagination, SQL_SELECT_BOOK_BY_GENRE, genreId);
    }

    @Override
    public List<Book> getByPublisher(Pagination pagination, Integer publisherId) {
        checkId(publisherId);
        pagination.setNumberOfItems(countByPublisher(publisherId));
        return getAll(pagination, SQL_SELECT_BOOK_BY_PUBLISHER, publisherId);
    }

    private int countByAuthor(Integer authorId) {
        return count(SQL_COUNT_BOOKS_BY_AUTHOR, authorId);
    }

    private int countByGenre(Integer genreId) {
        return count(SQL_COUNT_BOOKS_BY_GENRE, genreId);
    }

    private int countByPublisher(Integer publisherId) {
        return count(SQL_COUNT_BOOKS_BY_PUBLISHER, publisherId);
    }

    PgBookDao(Connection con) {
        super(con);
    }

}