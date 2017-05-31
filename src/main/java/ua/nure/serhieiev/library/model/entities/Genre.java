package ua.nure.serhieiev.library.model.entities;

public class Genre implements Identified {

    private static final long serialVersionUID = 7070967959735927042L;

    private Integer id;
    private String title;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public Genre setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Genre setTitle(String title) {
        this.title = title;
        return this;
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof Genre) && (id != null)
                ? id.equals(((Genre) other).id)
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
        return "Genre{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }

}
