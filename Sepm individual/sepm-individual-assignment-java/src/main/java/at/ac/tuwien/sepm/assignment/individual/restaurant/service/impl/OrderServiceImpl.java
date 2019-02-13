package at.ac.tuwien.sepm.assignment.individual.restaurant.service.impl;

import at.ac.tuwien.sepm.assignment.individual.restaurant.dao.DaoException;
import at.ac.tuwien.sepm.assignment.individual.restaurant.dao.OrderDAO;
import at.ac.tuwien.sepm.assignment.individual.restaurant.dao.impl.OrderDAOJDBC;
import at.ac.tuwien.sepm.assignment.individual.restaurant.entities.Order;
import at.ac.tuwien.sepm.assignment.individual.restaurant.service.OrderService;
import at.ac.tuwien.sepm.assignment.individual.restaurant.service.ServiceException;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

public class OrderServiceImpl implements OrderService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    OrderDAO orderDAO = new OrderDAOJDBC();

    @Override
    public void saveOrder(Order order) throws ServiceException {
        if (order==null){
            LOG.info("Order Null");
            throw new ServiceException("Order is null");
        }
        LOG.info("Saving order:{}", order);
        try {
            orderDAO.create(order);
            LOG.info("Order is saved:{}", order);
        } catch (DaoException dao) {
            LOG.error("Error while saving order");
            throw new ServiceException(dao.getMessage());
        }
    }


    @Override
    public void update(Order order) throws ServiceException {
        if (order==null){
            LOG.info("Order Null");
            throw new ServiceException("Order is null");
        }
        LOG.info("Updating order {}" ,order);
        try {
            orderDAO.update(order);
            LOG.info("Order updated");
        } catch (DaoException e) {
            LOG.error("Error while trying to update product",e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public ObservableList<Order> findAllOrders() throws ServiceException {
        LOG.info("Finding all orders..");
        try {
            return orderDAO.findAllOrders();
        } catch (DaoException e) {
            LOG.error("Error in finding products");
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public ObservableList<Order> findAllBills() throws ServiceException {
        LOG.info("Finding all Bills..");
        try {
            return orderDAO.findAllBills();
        } catch (DaoException e) {
            LOG.error("Error in finding bills");
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void billOrder(Order order) throws ServiceException {
        if (order==null){
            LOG.info("Order Null");
            throw new ServiceException("Order is null");
        }
        order.setStatus("billed");
        try {
            orderDAO.billOrder(order);
            LOG.info("Order billed");
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
    }
}


