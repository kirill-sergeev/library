package ua.nure.serhieiev.library.model;

public class Author implements Identified {

    Integer id;
    String name;

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
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

}
