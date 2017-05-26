package ua.nure.serhieiev.library.dao;

import ua.nure.serhieiev.library.model.entities.Publisher;

import java.util.List;

public interface PublisherDao extends GenericDao<Publisher>{

    List<Publisher> getByTitle(String title);

}
