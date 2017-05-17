package ua.nure.serhieiev.library.dao;

import ua.nure.serhieiev.library.dao.jdbc.postgres.PgDaoFactory;

public abstract class DaoFactory implements AutoCloseable{

    public static final int POSTGRESQL = 1;
    private static int defaultFactory = POSTGRESQL;

    public static DaoFactory getInstance(int whichFactory) {
        switch (whichFactory) {
            case POSTGRESQL:
                return new PgDaoFactory();
            default:
                throw new IllegalArgumentException();
        }
    }

    public static DaoFactory getInstance() {
        return getInstance(defaultFactory);
    }

    public static void changeDefaultFactory(int whichFactory) {
        defaultFactory = whichFactory;
    }

    public abstract TransactionManager getTransactionManager();

    public abstract AuthorDao getAuthorDao();
    public abstract BookDao getBookDao();
    public abstract FineDao getFineDao();
    public abstract GenreDao getGenreDao();
    public abstract OrderDao getOrderDao();
    public abstract PublisherDao getPublisherDao();
    public abstract UserDao getUserDao();

}
