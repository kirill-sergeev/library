package ua.nure.serhieiev.library.dao.jdbc;

import ua.nure.serhieiev.library.dao.DaoException;
import ua.nure.serhieiev.library.dao.TransactionManager;

import java.sql.Connection;
import java.sql.SQLException;

public class JdbcTransactionManager implements TransactionManager {

    private Connection con;

    public JdbcTransactionManager(Connection con) {
        this.con = con;
    }

    @Override
    public void start() {
        try {
            con.setAutoCommit(false);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void rollback() {
        try {
            con.rollback();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void close() {
        try {
            con.commit();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

}