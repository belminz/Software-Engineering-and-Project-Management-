package at.ac.tuwien.sepm.assignment.individual.restaurant.dao;

import at.ac.tuwien.sepm.assignment.individual.restaurant.entities.Product;
import at.ac.tuwien.sepm.assignment.individual.restaurant.service.ServiceException;
import javafx.collections.ObservableList;

public interface ProductDAO {


    /**
     * Add new Product in database
     * @param product object (products) that is going to be added in database
     * @throws DaoException own-database exception
     */
    void create (Product product) throws DaoException;

    /**
     * Iterates through List of Products that are in database
     * @throws DaoException own-database exception
     */
    ObservableList <Product> findAllProducts() throws DaoException;

    /**
     * Update Product in database
     * @param product object (products) that is going to be updated in database
     * @throws DaoException own-database exception
     */
    void update(Product product) throws DaoException;

    /**
     * Delete Product from database
     * @param product object (products) that is going to be deleted from database
     * @throws DaoException own-database exception
     */
    void delete (Product product) throws DaoException;



    /**
     * Method finds product with the same id as ID in Database.
     * @param ID
     * @return Product with the same id as param ID
     * @throws DaoException - own Database exception (For null values or other  "error" cases)
     */
    Product findID(Integer ID) throws DaoException;



    /**
     * Method is searching for searched products in db
     * @param product that is going to be searched for
     * @return Product/s with the same characteristics as we have entered in search fields
     * @throws DaoException - own Service exception (For null values or other  "error" cases)
     */
    ObservableList<Product> search(Product product) throws DaoException;


}
