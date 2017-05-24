package ua.nure.serhieiev.library.dao;

import ua.nure.serhieiev.library.model.Order;
import ua.nure.serhieiev.library.model.User;
import ua.nure.serhieiev.library.service.util.Pagination;

import java.util.List;

public interface OrderDao extends GenericDao<Order>{

    int count(User reader);

    List<Order> getUnconfirmed();

    List<Order> getRangeByReader(User reader, Pagination pagination);

}
