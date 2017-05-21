package ua.nure.serhieiev.library.service;

import ua.nure.serhieiev.library.dao.DaoFactory;
import ua.nure.serhieiev.library.dao.PublisherDao;
import ua.nure.serhieiev.library.dao.NotFoundException;
import ua.nure.serhieiev.library.model.Publisher;
import ua.nure.serhieiev.library.service.util.Pagination;

import java.util.*;

public final class PublisherService {

    public static List<Publisher> getAll() {
        List<Publisher> publishers;
        try (DaoFactory df = DaoFactory.getInstance()) {
            PublisherDao publisherDao = df.getPublisherDao();
            publishers = publisherDao.getAll();
        } catch (NotFoundException e) {
            publishers = Collections.emptyList();
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
        return publishers;
    }

    public static Map<Integer, List<Publisher>> getRange(Pagination pagination) {
        Map<Integer, List<Publisher>> publishersMap = new HashMap<>(1);
        List<Publisher> publishers;
        Integer count;
        try (DaoFactory df = DaoFactory.getInstance()) {
            PublisherDao publisherDao = df.getPublisherDao();
            count = publisherDao.count();
            checkPagination(pagination, count);
            publishers = publisherDao.getRange(pagination);
            publishersMap.put(count, publishers);
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
        return publishersMap;
    }

    private static void checkPagination(Pagination pagination, Integer count) {
        List<String> fields = Arrays.asList("title", "");
        if ((pagination.getOffset()) >= count) {
            throw new ApplicationException("Too high offset!");
        }
        if (!fields.contains(pagination.getSortBy())) {
            throw new ApplicationException("Bad sort field!");
        }
    }

    private PublisherService() {
    }

}
