package at.ac.tuwien.sepm.assignment.individual.restaurant.service;
import at.ac.tuwien.sepm.assignment.individual.restaurant.entities.OrderedProduct;
import javafx.collections.ObservableList;

public interface OrderedProductService {

    /**
     * Method is saving orderedProduct in database (Over its DAO interface)
     * @param orderedProduct object (product) that is going to be saved
     * @throws ServiceException return own ServiceException:
     */
    void saveOrderedProduct(OrderedProduct orderedProduct) throws ServiceException;

    /**
     * Method is updating orderedProduct in database (Over its DAO interface)
     * @param orderedProduct object (product) that is going to be updated
     * @throws ServiceException return own ServiceException:
     */
    void update (OrderedProduct orderedProduct) throws ServiceException;


    /**
     * Method is deleting orderedProduct in database (Over its DAO interface)
     * @param orderedProduct object (product) that is going to be deleted
     * @throws ServiceException return own ServiceException:
     */
    void delete (OrderedProduct orderedProduct) throws ServiceException;


    /**
     * Method finds all orderedProducts with the same order id as param orderId
     * used to fill all bills table
     * @param orderId
     * @return all orderedProducts with the same orderId as param orderId
     * @throws ServiceException - own service  exception
     */
    ObservableList<OrderedProduct> findOrderId(Integer orderId) throws ServiceException;

}
