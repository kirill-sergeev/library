package ua.nure.serhieiev.library.dao.jdbc.postgres;

import ua.nure.serhieiev.library.dao.DaoException;
import ua.nure.serhieiev.library.dao.NotFoundException;
import ua.nure.serhieiev.library.dao.FineDao;
import ua.nure.serhieiev.library.dao.jdbc.JdbcDao;
import ua.nure.serhieiev.library.model.Fine;
import ua.nure.serhieiev.library.model.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PgFineDao extends JdbcDao<Fine> implements FineDao {

    private static final String ORDER = "order_id";
    private static final String COST = "cost";
    private static final String PAID = "paid";

    private static final String SQL_CREATE_FINE = "INSERT INTO fines (order_id, cost) VALUES (?, ?)";
    private static final String SQL_DELETE_FINE = "DELETE FROM fines WHERE id = ?";
    private static final String SQL_UPDATE_FINE = "UPDATE fines SET cost = ?, paid = ? WHERE id = ?";
    private static final String SQL_SELECT_ALL_FINES = "SELECT * FROM fines";
    private static final String SQL_SELECT_FINE_BY_ID = "SELECT * FROM fines WHERE id = ?";

    @Override
    protected String getSelectQuery() {
        return SQL_SELECT_FINE_BY_ID;
    }

    @Override
    protected String getSelectAllQuery() {
        return SQL_SELECT_ALL_FINES;
    }

    @Override
    protected String getCreateQuery() {
        return SQL_CREATE_FINE;
    }

    @Override
    protected String getUpdateQuery() {
        return SQL_UPDATE_FINE;
    }

    @Override
    protected String getDeleteQuery() {
        return SQL_DELETE_FINE;
    }

    @Override
    protected List<Fine> parseResultSet(ResultSet rs) {
        List<Fine> list = new ArrayList<>();
        try {
            while (rs.next()) {
                Fine fine = new Fine()
                        .setId(rs.getInt(ID))
                        .setOrder(new Order().setId(rs.getInt(ORDER)))
                        .setCost(rs.getDouble(COST))
                        .setPaid(rs.getBoolean(PAID));
                list.add(fine);
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
    protected void prepareStatementForInsert(PreparedStatement st, Fine fine) {
        try {
            st.setInt(1, fine.getOrder().getId());
            st.setDouble(2, fine.getCost());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement st, Fine fine) {
        try {
            st.setDouble(1, fine.getCost());
            st.setBoolean(2, fine.getPaid());
            st.setInt(3, fine.getId());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    PgFineDao(Connection con) {
        super(con);
    }

}