package ua.nure.serhieiev.library.dao.jdbc.postgres;

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
            //throw new IllegalStateException("Missing JNDI!", e);
        }
    }

    static int var = 0;
    private static DataSource dataSource;
    private Connection con;

    private static Connection getConnection() {
        Connection con = null;
        System.out.println(++var);
        try {
            if (dataSource == null) {
                con = DriverManager.getConnection
                        ("jdbc:postgresql://localhost:5432/library?user=postgres&password=kirill");
                return con;
            }
            con = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
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

