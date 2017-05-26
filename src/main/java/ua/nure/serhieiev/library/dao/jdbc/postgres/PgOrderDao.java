package ua.nure.serhieiev.library.dao.jdbc.postgres;

import ua.nure.serhieiev.library.dao.DaoException;
import ua.nure.serhieiev.library.dao.NotFoundException;
import ua.nure.serhieiev.library.dao.OrderDao;
import ua.nure.serhieiev.library.dao.jdbc.JdbcDao;
import ua.nure.serhieiev.library.model.Book;
import ua.nure.serhieiev.library.model.Order;
import ua.nure.serhieiev.library.model.Publisher;
import ua.nure.serhieiev.library.model.User;
import ua.nure.serhieiev.library.service.util.Pagination;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PgOrderDao extends JdbcDao<Order> implements OrderDao {

    private static final String ORDER_DATE = "order_date";
    private static final String RETURNED_DATE = "return_date";
    private static final String INTERNAL = "internal";
    private static final String READER = "reader_id";
    private static final String LIBRARIAN = "librarian_id";
    private static final String BOOKS = "books";
    private static final String[] SORT_FIELDS = {INTERNAL, LIBRARIAN, ORDER_DATE, READER, RETURNED_DATE};

    private static final String SQL_COUNT_ORDERS_BY_READER = "SELECT count(*) FROM orders WHERE reader_id = ?";
    private static final String SQL_CREATE_ORDER = "INSERT INTO orders (reader_id, internal) VALUES (?, ?)";
    private static final String SQL_UPDATE_ORDER = "UPDATE orders SET librarian_id = ?, order_date = ?, return_date = ? WHERE id = ?";
    private static final String SQL_INSERT_BOOK_INTO_ORDER = "INSERT INTO books_orders(book_id, order_id) VALUES (?, ?)";
    private static final String SQL_DELETE_ALL_BOOK_FROM_ORDER = "DELETE FROM books_orders VALUES WHERE order_id = ?";
    private static final String SQL_SELECT_ALL_ORDERS =
            "SELECT o.*, array_agg (DISTINCT bo.book_id) AS books" +
                    " FROM orders o, books_orders bo" +
                    " WHERE o.id = bo.order_id" +
                    " GROUP BY o.id";
    private static final String SQL_SELECT_UNCONFIRMED_ORDERS =
            "SELECT o.*, array_agg (DISTINCT bo.book_id) AS books" +
                    " FROM orders o, books_orders bo" +
                    " WHERE o.id = bo.order_id AND order_date IS NULL" +
                    " GROUP BY o.id";
    private static final String SQL_SELECT_CURRENT_ORDERS =
            "SELECT o.*, array_agg (DISTINCT bo.book_id) AS books" +
                    " FROM orders o, books_orders bo" +
                    " WHERE o.id = bo.order_id AND order_date IS NOT NULL AND return_date IS NULL" +
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
    protected String[] getSortFields() {
        return SORT_FIELDS.clone();
    }

    @Override
    protected List<Order> parseResultSet(ResultSet rs) {
        List<Order> list = new ArrayList<>();
        try {
            while (rs.next()) {
                Order order = new Order()
                        .setId(rs.getInt(ID))
                        .setReader(new User().setId(rs.getInt(READER)))
                        .setInternal(rs.getBoolean(INTERNAL));
                if (rs.getObject(LIBRARIAN) != null) {
                    order.setLibrarian(new User().setId(rs.getInt(LIBRARIAN)));
                }
                if (rs.getDate(ORDER_DATE) != null) {
                    order.setOrderDate((rs.getDate(ORDER_DATE).toLocalDate()));
                }
                if (rs.getDate(RETURNED_DATE) != null) {
                    order.setReturnDate((rs.getDate(RETURNED_DATE).toLocalDate()));
                }

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
        return list;
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement st, Order order) {
        try {
            st.setInt(1, order.getReader().getId());
            st.setBoolean(2, order.getInternal());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement st, Order order) {
        try {
            st.setInt(1, order.getLibrarian().getId());
            st.setDate(2, Date.valueOf(order.getOrderDate()));
            st.setDate(3, order.getReturnDate() == null ? null : Date.valueOf(order.getReturnDate()));
            st.setInt(4, order.getId());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private void addBooksToOrder(Order order) {
        try (PreparedStatement st = con.prepareStatement(SQL_INSERT_BOOK_INTO_ORDER)) {
            for (Book book : order.getBooks()) {
                st.setInt(1, book.getId());
                st.setInt(2, order.getId());
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
        super.update(order);
        removeBooksFromOrder(order);
        addBooksToOrder(order);
    }

    @Override
    public List<Order> getRangeByReader(User reader, Pagination pagination) {
        checkId(reader);
        String order = pagination.isAscending() ? " " : " DESC";
        int limit = pagination.getLimit();
        int offset = pagination.getOffset();
        String sortBy = pagination.getSortBy().isEmpty() ? ORDER_DATE : pagination.getSortBy();
        String sql = "SELECT o.*, array_agg (DISTINCT bo.book_id) AS books" +
                " FROM orders o, books_orders bo" +
                " WHERE o.id = bo.order_id AND reader_id = ?" +
                " GROUP BY o.id" +
                " ORDER BY " + sortBy + order + " LIMIT ? OFFSET ?";
        return listQuery(sql, reader.getId(), limit, offset);
    }

    @Override
    public List<Order> getRangeCurrent(Pagination pagination) {
        String order = pagination.isAscending() ? " " : " DESC";
        int limit = pagination.getLimit();
        int offset = pagination.getOffset();
        String sortBy = pagination.getSortBy().isEmpty() ? ORDER_DATE : pagination.getSortBy();
        String sql = String.format("%s ORDER BY %s %s LIMIT ? OFFSET ?", SQL_SELECT_CURRENT_ORDERS, sortBy, order);
        return listQuery(sql, limit, offset);
    }

    @Override
    public int count(User reader) {
        checkId(reader);
        return count(SQL_COUNT_ORDERS_BY_READER, reader.getId());
    }

    @Override
    public List<Order> getUnconfirmed() {
        return listQuery(SQL_SELECT_UNCONFIRMED_ORDERS);
    }

    PgOrderDao(Connection con) {
        super(con);
    }

}