package at.ac.tuwien.sepm.assignment.individual.restaurant.service;
import at.ac.tuwien.sepm.assignment.individual.restaurant.dao.DaoException;
import at.ac.tuwien.sepm.assignment.individual.restaurant.entities.Order;
import at.ac.tuwien.sepm.assignment.individual.restaurant.entities.OrderedProduct;
import javafx.collections.ObservableList;

public interface OrderService {

    /**
     * Method is saving order in database (Over its DAO interface)
     * @param order object (order) that is going to be saved
     * @throws ServiceException return own ServiceException
     */
    void saveOrder(Order order) throws ServiceException;

    /**
     * Method is updating order in database (Over its DAO interface)
     * @param order object (order) that is going to be updated
     * @throws ServiceException return own ServiceException:
     */
    void update(Order order) throws ServiceException;

    /**
     * Method for finding all saved orders in DB
     * @return ObservableList of all orders with status "open".
     * @throws ServiceException return own ServiceException
     */
    ObservableList<Order> findAllOrders() throws ServiceException;

    /**
     * Method for finding all saved bills in DB
     * @return ObservableList of all bills with status "billed".
     * @throws ServiceException return own ServiceException
     */
    ObservableList<Order> findAllBills() throws ServiceException;

    /**
     * Method that is used in order to know if the order is paid or not.
     * Status of order is going to be changed in "billed".
     * @throws ServiceException return own ServiceException
     */
    void billOrder(Order order) throws ServiceException;
}
