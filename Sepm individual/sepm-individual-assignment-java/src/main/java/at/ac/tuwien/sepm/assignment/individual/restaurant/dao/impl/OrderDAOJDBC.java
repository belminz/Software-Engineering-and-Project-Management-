package at.ac.tuwien.sepm.assignment.individual.restaurant.dao.impl;

import at.ac.tuwien.sepm.assignment.individual.restaurant.dao.DaoException;
import at.ac.tuwien.sepm.assignment.individual.restaurant.dao.OrderDAO;
import at.ac.tuwien.sepm.assignment.individual.restaurant.entities.Order;
import at.ac.tuwien.sepm.assignment.individual.restaurant.entities.OrderedProduct;
import at.ac.tuwien.sepm.assignment.individual.restaurant.entities.Product;
import at.ac.tuwien.sepm.assignment.individual.restaurant.util.DBUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class OrderDAOJDBC implements OrderDAO {


    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void create(Order order) throws DaoException {

        LOG.info("Create order: {}", order);
        try {
        if (order.getTableNumber()<0){
            LOG.error("Table number <0");
            throw new DaoException("Table number has to be >0");
        }
            String orderInsertString = "INSERT INTO Orders (orderId,tableNumber,timeOfCreation,status,billDate,payMethod) VALUES (default,?,?,?,?,?)";
            PreparedStatement preparedStatement = DBUtil.getConnection().prepareStatement(orderInsertString, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, order.getTableNumber());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.setString(3, order.getStatus());
            preparedStatement.setTimestamp(4,Timestamp.valueOf(order.getBillDate()));
            preparedStatement.setString(5,order.getPayMethod());
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            generatedKeys.next();
            order.setId(generatedKeys.getInt(1));

            LOG.info("Order successfully added to database {}", order);
        }
        catch (SQLException | NullPointerException sql) {
            LOG.error("Unable to create new order in database, sql exception.");
            throw new DaoException("SQL Exception!");
        }
    }

    @Override
    public void update(Order order) throws DaoException {
        LOG.info("Updating Order to DB {}", order);
        try {
            String orderUpdateString = "UPDATE Orders SET tableNumber=?, status=?,billDate=?,payMethod=? WHERE orderId=?";
            PreparedStatement preparedStatement = DBUtil.getConnection().prepareStatement(orderUpdateString, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, order.getTableNumber());
            preparedStatement.setString(2, order.getStatus());
            preparedStatement.setTimestamp(3,Timestamp.valueOf(order.getBillDate()));
            preparedStatement.setString(4,order.getPayMethod());
            preparedStatement.setInt(5, order.getId());
            preparedStatement.executeUpdate();
            LOG.info("Successfully updated to Database {}", order);
        } catch (NullPointerException | SQLException e) {
            LOG.error("SQL Exception while updating Order {}",e.getMessage());
            throw new DaoException("Null pointer or sql exception");
        }
    }

    @Override
    public ObservableList<Order> findAllOrders() throws DaoException {
        LOG.info("Finding orders...");
        ObservableList<Order> orderObservableList = FXCollections.observableArrayList();
        try {
            PreparedStatement preparedStatement = DBUtil.getConnection().prepareStatement("SELECT * FROM Orders WHERE status='open'");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getInt(1));
                order.setTableNumber(rs.getInt(2));
                order.setTimeOfCreation(rs.getTimestamp(3).toLocalDateTime());
                order.setStatus(rs.getString(4));
                order.setProductsToBeAdded(findOrderedProducts(order.getId()));
                orderObservableList.add(order);
            }
            rs.close();
            preparedStatement.close();
        } catch (SQLException e) {
            LOG.error("SQL Exception while loading order");
            throw new DaoException(e.getMessage());
        }
        LOG.info("Order is  loaded from DB");
        return orderObservableList;
    }

    @Override
    public ObservableList<Order> findAllBills() throws DaoException {
        LOG.info("Finding bills....");
        ObservableList<Order> bills = FXCollections.observableArrayList();
        try {
            PreparedStatement preparedStatement = DBUtil.getConnection().prepareStatement("SELECT * FROM Orders WHERE status='billed'");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getInt(1));
                order.setTableNumber(rs.getInt(2));
                order.setTimeOfCreation(rs.getTimestamp(3).toLocalDateTime());
                order.setStatus(rs.getString(4));
                order.setBillDate(rs.getTimestamp(5).toLocalDateTime());
                order.setPayMethod(rs.getString(6));
                order.setBillNumber(rs.getInt(7));
                order.setProductsToBeAdded(findOrderedProducts(order.getId()));
                bills.add(order);
            }
            rs.close();
            preparedStatement.close();
        } catch (SQLException e) {
            LOG.error("SQL Exception while loading bills");
            throw new DaoException(e.getMessage());
        }
        LOG.info("Bills are  loaded from DB");
        return bills;
    }


    @Override
    public ObservableList<OrderedProduct> findOrderedProducts(int orderID) throws DaoException {
        LOG.info("Finding orders that are going to be payed");
        ObservableList<OrderedProduct> observableList = FXCollections.observableArrayList();
        try {
            PreparedStatement preparedStatement = DBUtil.getConnection().prepareStatement("SELECT * FROM OrderedProducts WHERE orderId = ?");
            preparedStatement.setInt(1, orderID);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                OrderedProduct orderedProduct = new OrderedProduct();
                orderedProduct.setId(rs.getInt(1));
                orderedProduct.setProductID(rs.getInt(2));
                orderedProduct.setOrderID(rs.getInt(3));
                orderedProduct.setAmount(rs.getInt(4));
                orderedProduct.setName(rs.getString(5));
                orderedProduct.setBruttoPrice(rs.getDouble(6));
                observableList.add(orderedProduct);
            }
            rs.close();
            preparedStatement.close();
        } catch (SQLException e) {
            LOG.error("SQL Exception while loading orderedProduct");
            throw new DaoException(e.getMessage());
        }
        LOG.info("OrderedProduct orderID is  loaded from DB ");
        return observableList;
    }

    @Override
    public void billOrder(Order order) throws DaoException {
        LOG.info("Trying to bill order {}",order);
        try {
            PreparedStatement ps = DBUtil.getConnection().prepareStatement("UPDATE Orders SET timeOfCreation = ?,status = ? ,billDate=?,payMethod = ?, billNumber = ? WHERE orderId = ? AND status='open'");
            ps.setTimestamp(1,Timestamp.valueOf(order.getTimeOfCreation()));
            ps.setString(2,order.getStatus());
            ps.setTimestamp(3,Timestamp.valueOf(order.getBillDate()));
            ps.setString(4,order.getPayMethod());
            ps.setInt(5,order.getBillNumber());
            ps.setInt(6,order.getId());
            ps.executeUpdate();
        } catch (SQLException | NullPointerException sql){
            LOG.error("SQL EXCEPTION while billing order");
            throw new DaoException(sql.getMessage());
        }
        LOG.info("Successfully billed order{}",order);
    }
}

