package at.ac.tuwien.sepm.assignment.individual.restaurant.service.impl;
import at.ac.tuwien.sepm.assignment.individual.restaurant.dao.DaoException;
import at.ac.tuwien.sepm.assignment.individual.restaurant.dao.OrderedProductDAO;
import at.ac.tuwien.sepm.assignment.individual.restaurant.dao.impl.OrderedProductDAOJDBC;
import at.ac.tuwien.sepm.assignment.individual.restaurant.entities.OrderedProduct;
import at.ac.tuwien.sepm.assignment.individual.restaurant.service.OrderedProductService;
import at.ac.tuwien.sepm.assignment.individual.restaurant.service.ServiceException;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

public class OrderedProductServiceImpl implements OrderedProductService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    OrderedProductDAO orderedProductDAO = new OrderedProductDAOJDBC();

    @Override
    public void saveOrderedProduct(OrderedProduct orderedProduct) throws ServiceException {
        if (orderedProduct.getName()==null || orderedProduct.getOrderID()==null || orderedProduct.getProductID()==null){
            throw new ServiceException("Some of parameters of ordered Product is null");
        }
        LOG.info("Saving order:{}", orderedProduct);
        try {
            orderedProductDAO.create(orderedProduct);
            LOG.info("Order is saved:{}", orderedProduct);
        } catch (DaoException dao) {
            LOG.error("Error while saving order");
            throw new ServiceException(dao.getMessage());
        }
    }

    @Override
    public void update(OrderedProduct orderedProduct) throws ServiceException {
        if (orderedProduct.getName()==null || orderedProduct.getOrderID()==null || orderedProduct.getProductID()==null){
            throw new ServiceException("Some of parameters of ordered Product is null");
        }
        try {
            orderedProductDAO.update(orderedProduct);
            LOG.info("Order updated {}",orderedProduct);
        } catch (DaoException e) {
            LOG.info("Error while updating ordered Product");
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void delete(OrderedProduct orderedProduct) throws ServiceException {
        LOG.info("Deleting order");
        try {
            orderedProductDAO.delete(orderedProduct);
        } catch (DaoException e) {
            LOG.error("Error while deleting products",e.getMessage());
            throw new ServiceException(e.getMessage());

        }
        LOG.info("Order deleted {}",orderedProduct);
    }

    @Override
    public ObservableList<OrderedProduct> findOrderId(Integer orderId) throws ServiceException {
        LOG.info("Finding ordered id...");
        try {
            return orderedProductDAO.findOrderId(orderId);
        } catch (DaoException e) {
            LOG.error("Cannot find order id",e.getMessage());

        }
        return null;
    }
}


