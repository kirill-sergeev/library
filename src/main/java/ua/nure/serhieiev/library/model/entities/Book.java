package ua.nure.serhieiev.library.model.entities;

import java.time.LocalDate;
import java.util.List;

public class Book implements Identified{

    private static final long serialVersionUID = 7688715476225409138L;

    private Integer id;
    private Integer quantity;
    private Integer available;
    private Long isbn;
    private String title;
    private String description;
    private LocalDate publicationDate;
    private Publisher publisher;
    private List<Author> authors;
    private List<Genre> genres;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public Book setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Book setQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public Integer getAvailable() {
        return available;
    }

    public Book setAvailable(Integer available) {
        this.available = available;
        return this;
    }

    public Long getIsbn() {
        return isbn;
    }

    public Book setIsbn(Long isbn) {
        this.isbn = isbn;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Book setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Book setDescription(String description) {
        this.description = description;
        return this;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public Book setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
        return this;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public Book setPublisher(Publisher publisher) {
        this.publisher = publisher;
        return this;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public Book setAuthors(List<Author> authors) {
        this.authors = authors;
        return this;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public Book setGenres(List<Genre> genres) {
        this.genres = genres;
        return this;
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof Book) && (id != null)
                ? id.equals(((Book) other).id)
                : (other == this);
    }

    @Override
    public int hashCode() {
        return (id != null)
                ? (this.getClass().hashCode() + id.hashCode())
                : super.hashCode();
    }

    @Override
    public String toString() {
        return "Book{" + "id=" + id +
                ", quantity=" + quantity +
                ", available=" + available +
                ", isbn=" + isbn +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", publicationDate=" + publicationDate +
                ", publisher=" + publisher +
                ", authors=" + authors +
                ", genres=" + genres +
                '}';
    }
}
