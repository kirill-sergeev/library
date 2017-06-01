package ua.nure.serhieiev.library.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.nure.serhieiev.library.dao.DaoFactory;
import ua.nure.serhieiev.library.dao.NotFoundException;
import ua.nure.serhieiev.library.dao.PublisherDao;
import ua.nure.serhieiev.library.model.Pagination;
import ua.nure.serhieiev.library.model.entities.Publisher;

import java.util.List;

public final class PublisherService {

    private static final Logger logger = LoggerFactory.getLogger(PublisherService.class);

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

    protected static void checkPublisher(DaoFactory df, Publisher publisher) {
        PublisherDao publisherDao = df.getPublisherDao();
        try {
            for (Publisher withSameTitle : publisherDao.getByTitle(publisher.getTitle())) {
                if (publisher.getTitle().equalsIgnoreCase(withSameTitle.getTitle())) {
                    publisher.setId(withSameTitle.getId());
                    break;
                }
            }
        } catch (NotFoundException e) {
            logger.info("Publisher {} not fount.", publisher.getTitle());
        }
        if (publisher.getId() == null) {
            publisherDao.save(publisher);
        }
    }

    private PublisherService() {
    }

}
