package at.ac.tuwien.sepm.assignment.individual.restaurant.dao;
import at.ac.tuwien.sepm.assignment.individual.restaurant.entities.Order;
import at.ac.tuwien.sepm.assignment.individual.restaurant.entities.OrderedProduct;
import at.ac.tuwien.sepm.assignment.individual.restaurant.service.ServiceException;
import javafx.collections.ObservableList;

public interface OrderDAO {


    /**
     * Add new Order in database
     * @param order object (order) that is going to be added in database
     * @throws DaoException own-database exception
     */
    void create(Order order) throws DaoException;

    /**
     * Update Order in database
     * @param order object (order) that is going to be updated in database
     * @throws DaoException own-database exception
     */
    void update (Order order) throws DaoException;

    /**
     * Iterates through List of Orders that are in database
     * @throws DaoException own-database exception
     * @return ObservableList of all orders with status "open".
     */
    ObservableList<Order> findAllOrders() throws DaoException;

    /**
     * Method for finding all saved bills
     * @return ObservableList of all bills with status "billed".
     * @throws DaoException return own Database exception.
     */
    ObservableList<Order> findAllBills() throws DaoException;


    /**
     * Method for finding all ordered Products (by passing order id)
     * @return ObservableList of all ordered products, in order
     * to show it in findAllBills() and findAllOrders() method.
     * @throws DaoException return own Database exception.
     */
    ObservableList<OrderedProduct> findOrderedProducts(int orderID) throws DaoException;


    /**
     * Method that is used in order to know if the order is paid or not.
     * Status of order is going to be changed in "billed".
     * @throws DaoException return own DAOException:
     */
    void billOrder(Order order) throws DaoException;

}
