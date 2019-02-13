package at.ac.tuwien.sepm.assignment.individual.restaurant.service.impl;
import at.ac.tuwien.sepm.assignment.individual.restaurant.dao.DaoException;
import at.ac.tuwien.sepm.assignment.individual.restaurant.dao.ProductDAO;
import at.ac.tuwien.sepm.assignment.individual.restaurant.dao.impl.ProductDAOJDBC;
import at.ac.tuwien.sepm.assignment.individual.restaurant.entities.Product;
import at.ac.tuwien.sepm.assignment.individual.restaurant.service.ProductService;
import at.ac.tuwien.sepm.assignment.individual.restaurant.service.ServiceException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.invoke.MethodHandles;

public class ProductServiceImpl implements ProductService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private ProductDAO productDAO = new ProductDAOJDBC();

    @Override
    public void saveProduct(Product product) throws ServiceException {
        if (product.getName()==null  || product.getNettoPrice()==null ||product.getTax()==null || product.getCategory()==null){
            throw new ServiceException("Some or all parameters of product are null!");
        }
        LOG.info("Saving product:{}", product);
        try {
            productDAO.create(product);
            LOG.info("Product is saved:{}", product);
        } catch (DaoException dao) {
            LOG.error("Error while saving product");
            throw new ServiceException(dao.getMessage());
        }
    }

    @Override
    public ObservableList<Product> findAll() throws ServiceException {
        try {
            return productDAO.findAllProducts();
        } catch (DaoException e) {
            LOG.error("Error in finding products");
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void updateProduct(Product product) throws ServiceException {
        if (product.getName()==null  || product.getNettoPrice()==null ||product.getTax()==null || product.getCategory()==null){
            throw new ServiceException("Some or all parameters of product are null!");
        }
        try {
            productDAO.update(product);
            LOG.info("Product updated");
        } catch (DaoException e) {
            LOG.error("Error while updating products");
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void deleteMoreProducts(ObservableList<Product> productObservableList) throws ServiceException {
        LOG.info("Deleting more products{}", productObservableList);
        try {
            for (Product product : productObservableList) {
                if (product.isProductDeleted()) {
                    throw new ServiceException("Product is already deleted");
                } else {
                    productDAO.delete(product);
                }
            }
            LOG.info("Selected products are deleted successfully{}");
        } catch (DaoException dao) {
            LOG.error("Failure while deleting more products");
            throw new ServiceException(dao.getMessage());
        }
    }


    @Override
    public ObservableList<Product> search(Product product) throws ServiceException {
        ObservableList<Product> products = FXCollections.observableArrayList();
        if (product.getName() ==null || product.getCategory()==null || product.getNettoPriceMin() ==null || product.getNettoPriceMax()==null){
            throw new ServiceException("Empty search fields");
        }
        try {
            LOG.info("Searching for products...");
            products = productDAO.search(product);
        } catch (DaoException dao) {
            LOG.error("Error: Products cannot be found.",dao.getMessage());
        }
        return products;
    }

    @Override
    public Product findID(Integer ID) throws ServiceException {
        try {
            return  productDAO.findID(ID);
        } catch (DaoException e) {
            LOG.error("Cannot find id",e.getMessage());
        }
        return null;
    }
}




