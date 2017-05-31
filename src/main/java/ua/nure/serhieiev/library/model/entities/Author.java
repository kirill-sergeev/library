package ua.nure.serhieiev.library.model.entities;

public class Author implements Identified {

    private static final long serialVersionUID = -4399004977176241611L;

    private Integer id;
    private String name;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public Author setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Author setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof Author) && (id != null)
                ? id.equals(((Author) other).id)
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
        return "Author{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

}
