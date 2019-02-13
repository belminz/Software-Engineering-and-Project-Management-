package at.ac.tuwien.sepm.assignment.individual.restaurant.service;
import at.ac.tuwien.sepm.assignment.individual.restaurant.entities.Product;
import javafx.collections.ObservableList;

/**

 */
public interface ProductService {

    /**
     *  Method is saving product in database
     * @param product object (product) that is going to be saved
     * @throws ServiceException return own ServiceException:
     */
    void saveProduct(Product product) throws ServiceException;

    /**
     *  Method for finding all saved products
     * @throws ServiceException return own ServiceException:
     */
    ObservableList<Product> findAll() throws ServiceException;

    /**
     * Update Product in database
     * @param product object (products) that is going to be updated in database
     * @throws ServiceException own-Service exception
     */
    void updateProduct(Product product) throws ServiceException;


    /**
     * Delete Product(s) from database
     * @param productObservableList object (products) that is going to be deleted from database
     * @throws ServiceException own-Service exception
     */
    void deleteMoreProducts(ObservableList<Product> productObservableList) throws ServiceException;

    /**
     * Method finds product with the same id as ID in Database.
     * @param ID
     * @return Product with the same id as param ID
     * @throws ServiceException - own Service exception (For null values or other  "error" cases)
     */
    Product findID(Integer ID) throws ServiceException;


    /**
     * Method is searching for searched products in db
     * @param product that is going to be searched for
     * @return Product/s with the same characteristics as we have entered in search fields
     * @throws ServiceException - own Service exception (For null values or other  "error" cases)
     */

    ObservableList<Product> search(Product product) throws ServiceException;
}
