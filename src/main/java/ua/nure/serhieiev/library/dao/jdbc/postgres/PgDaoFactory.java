package ua.nure.serhieiev.library.dao.jdbc.postgres;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.nure.serhieiev.library.controller.LogoutServlet;
import ua.nure.serhieiev.library.dao.*;
import ua.nure.serhieiev.library.dao.jdbc.JdbcTransactionManager;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PgDaoFactory extends DaoFactory {

    static {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/postgresql");
        } catch (NamingException e) {
            throw new IllegalStateException("Missing JNDI!", e);
        }
    }

    private static DataSource dataSource;
    private Connection con;

    private static Connection getConnection() {
        Connection con;
        try {
            con = dataSource.getConnection();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return con;
    }

    public PgDaoFactory() {
        con = getConnection();
    }

    @Override
    public TransactionManager getTransactionManager() {
        return new JdbcTransactionManager(con);
    }
    @Override
    public AuthorDao getAuthorDao() {
        return new PgAuthorDao(con);
    }
    @Override
    public BookDao getBookDao() {
        return new PgBookDao(con);
    }
    @Override
    public FineDao getFineDao() {
        return new PgFineDao(con);
    }
    @Override
    public GenreDao getGenreDao() {
        return new PgGenreDao(con);
    }
    @Override
    public OrderDao getOrderDao() {
        return new PgOrderDao(con);
    }
    @Override
    public PublisherDao getPublisherDao() {
        return new PgPublisherDao(con);
    }
    @Override
    public UserDao getUserDao() {
        return new PgUserDao(con);
    }

    @Override
    public void close() {
        try {
            con.close();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

}

