package ua.nure.serhieiev.library.dao;

public interface TransactionManager extends AutoCloseable {

    void start();

    void rollback();

    void close();

}
