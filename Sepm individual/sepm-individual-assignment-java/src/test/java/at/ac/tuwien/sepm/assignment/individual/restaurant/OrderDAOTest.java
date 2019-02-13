package at.ac.tuwien.sepm.assignment.individual.restaurant;

import at.ac.tuwien.sepm.assignment.individual.restaurant.dao.*;
import at.ac.tuwien.sepm.assignment.individual.restaurant.dao.impl.OrderDAOJDBC;
import at.ac.tuwien.sepm.assignment.individual.restaurant.entities.Order;
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

public class OrderDAOTest {

    private Connection connection = null;
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private OrderDAO orderDAO;

    @Before
    public void setUpConnection() throws SQLException {
        OrderDAO orderDAO = new OrderDAOJDBC();
        setOrderDAO(orderDAO);

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

    public void setOrderDAO(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }


    /**
     *  create Orders tests
     */

    @Test(expected = DaoException.class)
    public void createOrderNull() throws DaoException {
        orderDAO.create(null);
    }

    @Test
    public void createValidOrder() throws DaoException {
        Order order = new Order(10, LocalDateTime.now(), "open", LocalDateTime.now(), null);
        orderDAO.create(order);
        ObservableList<Order> orders = orderDAO.findAllOrders();
        for (Order order1 : orders) {
            if (order.getId().equals(order1.getId())) {
                Assert.assertEquals(order.getTableNumber(), order1.getTableNumber());
                Assert.assertEquals(order.getStatus(), order1.getStatus());
            }
        }
    }



    @Test(expected = DaoException.class)
    public void createOrderInvalidTableNumber() throws DaoException {
        Order order = new Order(-101, LocalDateTime.now(), "open", LocalDateTime.now(), null);
        orderDAO.create(order);
    }

    @Test(expected = DaoException.class)
    public void createOrderInvalidStatus() throws DaoException {
        Order order = new Order(101, LocalDateTime.now(), "closed", LocalDateTime.now(), null);
        orderDAO.create(order);
    }

    @Test(expected = DaoException.class)
    public void createWithTableNumberNull() throws DaoException {
        Order order = new Order(null, LocalDateTime.now(), "open", LocalDateTime.now(), null);
        orderDAO.create(order);
    }

    /**
     * Order updates tests
     */
    @Test(expected = DaoException.class)
    public void updateOrderNull() throws DaoException {
        orderDAO.update(null);
    }
    //    public Order(Integer tableNumber, LocalDateTime timeOfCreation, String status, LocalDateTime billDate, String payMethod) {

    @Test
    public void updateWithValidValues() throws DaoException {
        Order order = new Order(10,LocalDateTime.now(),"open",LocalDateTime.now(),null);
        orderDAO.create(order);
        order.setTableNumber(101);
        orderDAO.update(order);
        ObservableList<Order> orders = orderDAO.findAllOrders();
        for (Order order1 : orders) {
            if (order.getId().equals(order1.getId())) {
                Assert.assertEquals(order.getStatus(), order1.getStatus());
                Assert.assertEquals(order.getPayMethod(), order1.getPayMethod());
            }
        }
}
/*
    @Test (expected = DaoException.class)
    public void updateWithInvalidValues() throws DaoException {
        Order order = new Order(10,LocalDateTime.now(),"open",LocalDateTime.now(),null);
        orderDAO.create(order);
        order.setStatus("bad status");
        orderDAO.update(order);
    }*/

    @Test
    public void billOrderValidInputs() throws DaoException {
        Order order = new Order(10,LocalDateTime.now(),"open",LocalDateTime.now(),null);
        orderDAO.create(order);
        order.setStatus("billed");
        order.setBillNumber(501);
        orderDAO.billOrder(order);
    }
    @Test (expected = DaoException.class)
    public void billOrderInvalidInputs() throws DaoException {
        Order order = new Order(10,LocalDateTime.now(),"open",LocalDateTime.now(),null);
        orderDAO.create(order);
        orderDAO.billOrder(order);
    }

}

