package ua.nure.serhieiev.library.service;

import ua.nure.serhieiev.library.dao.PublisherDao;
import ua.nure.serhieiev.library.dao.DaoFactory;
import ua.nure.serhieiev.library.dao.PublisherDao;
import ua.nure.serhieiev.library.dao.NotFoundException;
import ua.nure.serhieiev.library.model.entities.Publisher;
import ua.nure.serhieiev.library.model.entities.Publisher;
import ua.nure.serhieiev.library.model.Pagination;

import java.util.*;

public final class PublisherService {

    public static List<Publisher> getAll(Pagination pagination) {
        List<Publisher> publishers;
        try (DaoFactory df = DaoFactory.getInstance()) {
            PublisherDao publisherDao = df.getPublisherDao();
            publishers = publisherDao.getAll(pagination);
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
        return publishers;
    }

    public static List<Publisher> getByTitle(String title) {
        if (title == null || title.trim().length() < 3){
            throw new ApplicationException("Title must be longer than 2 characters!");
        }
        List<Publisher> publishers;
        try (DaoFactory df = DaoFactory.getInstance()) {
            PublisherDao publisherDao = df.getPublisherDao();
            publishers = publisherDao.getByTitle(title);
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
        return publishers;
    }

    static void checkPublisher(DaoFactory df, Publisher publisher) {
        PublisherDao publisherDao = df.getPublisherDao();
        try {
            for (Publisher withSameTitle : publisherDao.getByTitle(publisher.getTitle())) {
                if (publisher.getTitle().equalsIgnoreCase(withSameTitle.getTitle())) {
                    publisher.setId(withSameTitle.getId());
                    break;
                }
            }
        } catch (NotFoundException e) {

        }
        if (publisher.getId() == null) {
            publisherDao.save(publisher);
        }
    }

    private PublisherService() {
    }

}
