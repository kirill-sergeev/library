package ua.nure.serhieiev.library.model.entities;

public class Publisher implements Identified {

    Integer id;
    String title;

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
    public String toString() {
        return "Publisher{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }

}
