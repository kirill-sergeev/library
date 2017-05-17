package ua.nure.serhieiev.library.model;

public class Genre implements Identified {

    Integer id;
    String title;

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
    public String toString() {
        return "Genre{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }

}
