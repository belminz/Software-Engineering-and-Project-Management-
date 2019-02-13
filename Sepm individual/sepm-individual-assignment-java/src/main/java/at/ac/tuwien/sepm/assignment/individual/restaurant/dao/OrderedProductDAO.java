package at.ac.tuwien.sepm.assignment.individual.restaurant.dao;
import at.ac.tuwien.sepm.assignment.individual.restaurant.entities.OrderedProduct;
import at.ac.tuwien.sepm.assignment.individual.restaurant.service.ServiceException;
import javafx.collections.ObservableList;


public interface OrderedProductDAO {


    /**
     * Add new OrderedProduct in database
     * @param orderedProduct object (orderedProduct) that is going to be added in database
     * @throws DaoException own-database exception
     */
    void create (OrderedProduct orderedProduct) throws DaoException;

    /**
     * Update orderedProduct in database
     * @param orderedProduct object (orderedProduct) that is going to be updated in database
     * @throws DaoException own-database exception
     */
    void update (OrderedProduct orderedProduct) throws DaoException;

    /**
     * Delete OrderedProduct from database
     * @param orderedProduct object (orderedProduct) that is going to be deleted from database
     * @throws DaoException own-database exception
     */
    void delete (OrderedProduct orderedProduct) throws DaoException;

    /**
     * Method finds all orderedProducts with the same order id as param orderId
     * used to fill all bills details table
     * @param orderId
     * @return all orderedProducts with the same orderId as param orderId
     * @throws DaoException - own DaoException
     */
      ObservableList<OrderedProduct> findOrderId(Integer orderId) throws DaoException;
}

