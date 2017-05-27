package ua.nure.serhieiev.library.dao;

import ua.nure.serhieiev.library.model.entities.Order;
import ua.nure.serhieiev.library.model.entities.User;
import ua.nure.serhieiev.library.model.Pagination;

import java.util.List;

public interface OrderDao extends GenericDao<Order>{

    List<Order> getUnconfirmed(Pagination pagination);

    List<Order> getCurrent(Pagination pagination);

    List<Order> getClosed(Pagination pagination);

    List<Order> getByReader(Pagination pagination, Integer readerId);

}
