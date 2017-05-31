package ua.nure.serhieiev.library.model.entities;

public class Publisher implements Identified {

    private static final long serialVersionUID = 5231397774850418143L;

    private Integer id;
    private String title;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public Publisher setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Publisher setTitle(String title) {
        this.title = title;
        return this;
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof Publisher) && (id != null)
                ? id.equals(((Publisher) other).id)
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
        return "Publisher{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }

}
