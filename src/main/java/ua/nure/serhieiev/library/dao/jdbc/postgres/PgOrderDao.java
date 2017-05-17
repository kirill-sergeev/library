package ua.nure.serhieiev.library.dao.jdbc.postgres;

import ua.nure.serhieiev.library.dao.DaoException;
import ua.nure.serhieiev.library.dao.NotFoundException;
import ua.nure.serhieiev.library.dao.OrderDao;
import ua.nure.serhieiev.library.dao.jdbc.JdbcDao;
import ua.nure.serhieiev.library.model.Author;
import ua.nure.serhieiev.library.model.Book;
import ua.nure.serhieiev.library.model.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PgOrderDao extends JdbcDao<Order> implements OrderDao {

    private static final String ORDER_DATE = "order_date";
    private static final String EXPECTED_DATE = "expected_date";
    private static final String RETURNED_DATE = "return_date";
    private static final String BOOKS = "books";

    private static final String SQL_CREATE_ORDER = "INSERT INTO orders (user_id, order_date, expected_date) VALUES (?, ?, ?)";
    private static final String SQL_DELETE_ORDER = "DELETE FROM orders WHERE id = ?";
    private static final String SQL_UPDATE_ORDER = "UPDATE order SET returned_date = ? WHERE id = ?";
    private static final String SQL_INSERT_BOOK_INTO_ORDER = "INSERT INTO books_orders(book_id, order_id) VALUES (?, ?)";
    private static final String SQL_DELETE_ALL_BOOK_FROM_ORDER = "DELETE FROM books_orders VALUES WHERE order_id = ?";
    private static final String SQL_SELECT_ALL_ORDERS =
            "SELECT o.*, array_agg (DISTINCT bo.book_id) AS books" +
                    " FROM orders o, books_orders bo" +
                    " WHERE o.id = bo.order_id" +
                    " GROUP BY o.id";
    private static final String SQL_SELECT_ORDER_BY_ID =
            "SELECT o.*, array_agg (DISTINCT bo.book_id) AS books" +
                    " FROM orders o, books_orders bo" +
                    " WHERE o.id = bo.order_id AND o.id = ?" +
                    " GROUP BY o.id";

    @Override
    protected String getSelectQuery() {
        return SQL_SELECT_ORDER_BY_ID;
    }
    @Override
    protected String getSelectAllQuery() {
        return SQL_SELECT_ALL_ORDERS;
    }
    @Override
    protected String getCreateQuery() {
        return SQL_CREATE_ORDER;
    }
    @Override
    protected String getUpdateQuery() {
        return SQL_UPDATE_ORDER;
    }
    @Override
    protected String getDeleteQuery() {
        return SQL_DELETE_ORDER;
    }

    @Override
    protected List<Order> parseResultSet(ResultSet rs) {
        List<Order> list = new ArrayList<>();
        try {
            while (rs.next()) {
                Order order = new Order()
                        .setId(rs.getInt(ID))
                        .setOrderDate((rs.getDate(ORDER_DATE).toLocalDate()))
                        .setExpectedReturnDate((rs.getDate(EXPECTED_DATE).toLocalDate()))
                        .setReturnDate((rs.getDate(RETURNED_DATE).toLocalDate()));

                List<Book> books = new ArrayList<>();
                for (Integer bookId : (Integer[]) rs.getArray(BOOKS).getArray()) {
                    books.add(new Book().setId(bookId));
                }
                order.setBooks(books);

                list.add(order);
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
    protected void prepareStatementForInsert(PreparedStatement st, Order order) {
        try {
            st.setInt(1, order.getUser().getId());
            st.setDate(2, Date.valueOf(order.getOrderDate()));
            st.setDate(3, Date.valueOf(order.getExpectedReturnDate()));
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement st, Order order) {
        try {
            st.setDate(1, Date.valueOf(order.getReturnDate()));
            st.setInt(2, order.getId());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private void addBooksToOrder(Order order) {
        try(PreparedStatement st = con.prepareStatement(SQL_INSERT_BOOK_INTO_ORDER)){
            for (Book book : order.getBooks()) {
                st.setInt(1, order.getId());
                st.setInt(2, book.getId());
                st.addBatch();
            }
            st.executeBatch();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private void removeBooksFromOrder(Order order) {
        try (PreparedStatement st = con.prepareStatement(SQL_DELETE_ALL_BOOK_FROM_ORDER)) {
            st.setInt(1, order.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void save(Order order) {
        super.save(order);
        addBooksToOrder(order);
    }

    @Override
    public void update(Order order) {
        super.save(order);
        removeBooksFromOrder(order);
        addBooksToOrder(order);
    }

    PgOrderDao(Connection con) {
        super(con);
    }

}