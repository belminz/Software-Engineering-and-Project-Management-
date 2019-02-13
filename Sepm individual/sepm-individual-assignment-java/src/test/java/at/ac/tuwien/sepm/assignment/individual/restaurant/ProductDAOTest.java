package at.ac.tuwien.sepm.assignment.individual.restaurant;

import at.ac.tuwien.sepm.assignment.individual.restaurant.dao.*;
import at.ac.tuwien.sepm.assignment.individual.restaurant.dao.impl.ProductDAOJDBC;
import at.ac.tuwien.sepm.assignment.individual.restaurant.entities.Product;
import at.ac.tuwien.sepm.assignment.individual.restaurant.util.DBUtil;
import javafx.collections.ObservableList;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static org.hamcrest.core.Is.is;

public class ProductDAOTest {

    private Connection connection = null;
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private ProductDAO productDAO;


    @Before
    public void setUpConnection() throws SQLException {
        ProductDAO productDAO = new ProductDAOJDBC();
        setProductDAO(productDAO);

        connection = DBUtil.getConnection();
        assert connection != null;
        connection.setAutoCommit(false);
        LOG.debug("Connection created!");
    }

    @After
    public void tearDownConnection() throws SQLException {
        connection = DBUtil.getConnection();
        assert connection != null;
        connection.rollback();
        LOG.debug("Connection rollback!");
    }

    public void setProductDAO(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    /**
     * User story 1
     */

    @Test(expected = DaoException.class)
    public void createProductWithNull() throws DaoException {
        productDAO.create(null);
    }
    //public Product(String name, String description, Integer nettoPrice,String category, String tax) {

    @Test(expected = DaoException.class)
    public void createWithInvalidNettoPrice() throws DaoException {
        Product product = new Product("Wiener Schnitzel", "this is description", -1, "Main dish", "10%");
        productDAO.create(product);
    }

    @Test
    public void createProductWithAllValidInputs() throws DaoException {
        Product product = new Product();
        product.setName("Wiener");
        product.setDescription("Hello");
        product.setCategory("Main dish");
        product.setNettoPrice(10);
        product.setTax("10%");
        product.setProductDeleted(false);
        product.setTimeOfCreation(LocalDateTime.now());
        product.setBruttoPrice(product.calculateBrutto());
        product.setEdit_date(LocalDateTime.now());

        ObservableList<Product> products = productDAO.findAllProducts();
        Assert.assertFalse(products.contains(product));

        productDAO.create(product);
        ObservableList<Product> products1 = productDAO.findAllProducts();
        for (Product product1 : products1) {
            if (product1.getId().equals(product.getId())) {
                Assert.assertEquals(product.getCategory(), product1.getCategory());
                Assert.assertEquals(product.getName(), product1.getName());
            }
        }
    }


    @Test
    public void createWithDescriptionNull() throws DaoException {
        Product product = new Product("Wiener Schnitzel", null, 10, "Main dish", "10%");
        product.setTimeOfCreation(LocalDateTime.now());
        product.setEdit_date(LocalDateTime.now());
        productDAO.create(product);

        ObservableList<Product> products = productDAO.findAllProducts();
        Assert.assertFalse(products.contains(product));
        productDAO.create(product);
        ObservableList<Product> products1 = productDAO.findAllProducts();
        for (Product product1 : products1) {
            if (product1.getId().equals(product.getId())) {
                Assert.assertEquals(product.getCategory(), product1.getCategory());
                Assert.assertEquals(product.getName(), product1.getName());
            }
        }
    }

    /**
     * User story 2
     */
    @Test(expected = DaoException.class)
    public void createWithCategoryNull() throws DaoException {
        Product product = new Product("Wiener Schnitzel", "hello wien", 10, null, "10%");
        productDAO.create(product);
    }
    @Test(expected = DaoException.class)
    public void createWithInvalidCategory() throws DaoException {
        Product product = new Product("Wiener Schnitzel", "hello wien", 10, "Vienna", "10%");
        productDAO.create(product);
    }
    @Test(expected = DaoException.class)
    public void updateWithCategoryNull() throws DaoException {
        Product product = new Product("Wiener Schnitzel", "hello wien", 10, null, "10%");
        productDAO.update(product);
    }
    @Test(expected = DaoException.class)
    public void updateWithInvalidCategory() throws DaoException {
        Product product = new Product("Wiener Schnitzel", "hello wien", 10, "Vienna", "10%");
        productDAO.update(product);
    }

    /**
     * User story 3
     */
    @Test(expected = NullPointerException.class)
    public void createWithTaxNull() throws DaoException,IllegalArgumentException {
        Product product = new Product("Wiener Schnitzel", "hello wien", 10, "Starter", null);
        productDAO.create(product);
    }
    @Test(expected = DaoException.class)
    public void createWithInvalidTax() throws DaoException {
        Product product = new Product("Wiener Schnitzel", "hello wien", 10, "Starter", "100%");
        productDAO.create(product);
    }
    @Test(expected = NullPointerException.class)
    public void updateWithTaxNull() throws DaoException,IllegalArgumentException {
        Product product = new Product("Wiener Schnitzel", "hello wien", 10, "Starter", null);
        productDAO.update(product);
    }
    @Test(expected = DaoException.class)
    public void updateWithInvalidTax() throws DaoException {
        Product product = new Product("Wiener Schnitzel", "hello wien", 10, "Starter", "101%");
        productDAO.update(product);
    }

    /**
     * User story 4
     */
    @Test(expected = DaoException.class)
    public void updateProductWithNull() throws DaoException {
        productDAO.update(null);
    }
    @Test(expected = DaoException.class)
    public void updateProductWithInvalidInput() throws DaoException {
        Product product = new Product("Wiener","hello wien",10,"Starter","10%");
        productDAO.create(product);
        product.setName(null);
        productDAO.update(product);
    }
   @Test
    public void updateWithAllValidInputs() throws DaoException {
         Product product = new Product("Wiener","hello wien",10,"Starter","10%");
         product.setProductDeleted(false);
         product.setTimeOfCreation(LocalDateTime.now());
         product.setBruttoPrice(product.calculateBrutto());
         product.setEdit_date(LocalDateTime.now());
        productDAO.create(product);
        product.setName("Wiener Schnitzel new edition");
        productDAO.update(product);

        ObservableList<Product> products = productDAO.findAllProducts();
        for (Product product1 : products) {
            if (product.getId().equals(product1.getId())){
                Assert.assertEquals(product.getCategory(), product1.getCategory());
                Assert.assertEquals(product.getName(), product1.getName());
            }
        }
    }
    @Test
    public void updateTimeOfCreationShouldBeSameAsBefore() throws DaoException{
        Product product = new Product("Wiener","hello wien",10,"Starter","10%");
        product.setProductDeleted(false);
        product.setTimeOfCreation(LocalDateTime.now());
        product.setBruttoPrice(product.calculateBrutto());
        product.setEdit_date(LocalDateTime.now());
        productDAO.create(product);
        product.setTimeOfCreation(LocalDateTime.now());
        productDAO.update(product);
    }

    /**
     * User story 5
     */

    @Test (expected = DaoException.class)
    public void deleteWithNull() throws DaoException {
        productDAO.delete(null);
    }
    @Test
    public void deleteValidProduct() throws DaoException {
        Product product = new Product("Wiener","hello wien",10,"Starter","10%");
        product.setProductDeleted(false);
        product.setTimeOfCreation(LocalDateTime.now());
        product.setBruttoPrice(product.calculateBrutto());
        product.setEdit_date(LocalDateTime.now());
        productDAO.create(product);
        ObservableList<Product> products = productDAO.findAllProducts();
        for (Product product1 : products) {
            if (product.getId().equals(product1.getId())){
                Assert.assertEquals(product.getCategory(), product1.getCategory());
                Assert.assertEquals(product.getName(), product1.getName());
            }
        }
        productDAO.delete(product);

        System.out.println("OVO OBRISANO " + product);
        products = productDAO.findAllProducts();
        Assert.assertFalse(products.contains(product));
    }

}
