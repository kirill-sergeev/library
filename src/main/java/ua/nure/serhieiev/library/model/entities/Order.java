package ua.nure.serhieiev.library.model.entities;

import java.time.LocalDate;
import java.util.List;

public class Order implements Identified{

    Integer id;
    User reader;
    User librarian;
    LocalDate orderDate;
    LocalDate returnDate;
    Boolean internal;
    List<Book> books;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public Order setId(Integer id) {
        this.id = id;
        return this;
    }

    public User getReader() {
        return reader;
    }

    public Order setReader(User reader) {
        this.reader = reader;
        return this;
    }

    public User getLibrarian() {
        return librarian;
    }

    public Order setLibrarian(User librarian) {
        this.librarian = librarian;
        return this;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public Order setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
        return this;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public Order setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
        return this;
    }

    public Boolean getInternal() {
        return internal;
    }

    public Order setInternal(Boolean internal) {
        this.internal = internal;
        return this;
    }

    public List<Book> getBooks() {
        return books;
    }

    public Order setBooks(List<Book> books) {
        this.books = books;
        return this;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", reader=" + reader +
                ", librarian=" + librarian +
                ", orderDate=" + orderDate +
                ", returnDate=" + returnDate +
                ", internal=" + internal +
                ", books=" + books +
                '}';
    }

}
