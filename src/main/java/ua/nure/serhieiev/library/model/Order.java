package ua.nure.serhieiev.library.model;

import java.time.LocalDate;
import java.util.List;

public class Order implements Identified{

    Integer id;
    User user;
    LocalDate orderDate;
    LocalDate expectedReturnDate;
    LocalDate returnDate;
    List<Book> books;

    public Integer getId() {
        return id;
    }

    public Order setId(Integer id) {
        this.id = id;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Order setUser(User user) {
        this.user = user;
        return this;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public Order setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
        return this;
    }

    public LocalDate getExpectedReturnDate() {
        return expectedReturnDate;
    }

    public Order setExpectedReturnDate(LocalDate expectedReturnDate) {
        this.expectedReturnDate = expectedReturnDate;
        return this;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public Order setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
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
                ", user=" + user +
                ", orderDate=" + orderDate +
                ", expectedReturnDate=" + expectedReturnDate +
                ", returnDate=" + returnDate +
                ", books=" + books +
                '}';
    }

}
