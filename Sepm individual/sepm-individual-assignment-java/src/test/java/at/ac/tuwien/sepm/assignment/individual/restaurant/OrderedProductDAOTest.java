package at.ac.tuwien.sepm.assignment.individual.restaurant;

import at.ac.tuwien.sepm.assignment.individual.restaurant.dao.*;
import at.ac.tuwien.sepm.assignment.individual.restaurant.dao.impl.OrderedProductDAOJDBC;
import at.ac.tuwien.sepm.assignment.individual.restaurant.entities.OrderedProduct;
import at.ac.tuwien.sepm.assignment.individual.restaurant.service.OrderedProductService;
import at.ac.tuwien.sepm.assignment.individual.restaurant.service.ServiceException;
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

public class OrderedProductDAOTest {

    protected OrderedProductService orderedProductService;
    private Connection connection = null;
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private OrderedProductDAO orderedProductDAO;

    @Before
    public void setUpConnection() throws SQLException {
        OrderedProductDAO orderedProductDAO = new OrderedProductDAOJDBC();
        setOrderedProductDAO(orderedProductDAO);

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

    public void setOrderedProductDAO(OrderedProductDAO orderedProductDAO) {
        this.orderedProductDAO = orderedProductDAO;
    }


    /**
     * create Ordered Product tests...
     */

    @Test(expected = DaoException.class)
    public void createOrderNull() throws DaoException {
        orderedProductDAO.create(null);
    }

    // public OrderedProduct(Integer id, Integer pid, Integer orderID, String name, Double bruttoPrice, Integer amount) {
    @Test
    public void createValidOrderedProduct() throws DaoException {
        OrderedProduct orderedProduct = new OrderedProduct(10, 2, 2, "Wiener Snitzel", 5.0, 2);
        orderedProductDAO.create(orderedProduct);
        ObservableList<OrderedProduct> orderedProducts = orderedProductDAO.findOrderId(orderedProduct.getOrderID());
        for (OrderedProduct orderedProduct1 : orderedProducts) {
            if (orderedProduct.getId().equals(orderedProduct1.getId())) {
                Assert.assertEquals(orderedProduct.getId(), orderedProduct1.getId());
            }
        }
    }

    @Test(expected = DaoException.class)
    public void createWithInvalidParameters() throws DaoException {
        OrderedProduct orderedProduct = new OrderedProduct(1, null, null, "pommes", 5.0, 1);
        orderedProductDAO.create(orderedProduct);
    }

    /**
     * update ordered products tests
     */
    @Test(expected = DaoException.class)
    public void updateorderedProductsNull() throws DaoException {
        orderedProductDAO.update(null);
    }

    @Test(expected = NullPointerException.class)
    public void updateOrderedProductWithInvalidValues() throws ServiceException, NullPointerException {
        OrderedProduct orderedProduct = new OrderedProduct(1, null, null, "pommes", 5.0, 1);
        try {
            orderedProductService.update(orderedProduct);
        } catch (ServiceException e) {
            LOG.info(e.getMessage());
            // throw new DaoException(e.getMessage());
        }
    }

    /**
     * 3. Delete OrderedProduct test
     */

    @Test(expected = NullPointerException.class)
    public void deleteWithNullShouldThrowException() throws NullPointerException {
        try {
            orderedProductService.delete(null);
        } catch (ServiceException e) {
            LOG.error(e.getMessage());
        }
    }

}
