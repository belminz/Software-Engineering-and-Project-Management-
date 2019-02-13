package at.ac.tuwien.sepm.assignment.individual.restaurant.dao.impl;
import at.ac.tuwien.sepm.assignment.individual.restaurant.dao.DaoException;
import at.ac.tuwien.sepm.assignment.individual.restaurant.dao.OrderedProductDAO;
import at.ac.tuwien.sepm.assignment.individual.restaurant.entities.Order;
import at.ac.tuwien.sepm.assignment.individual.restaurant.entities.OrderedProduct;
import at.ac.tuwien.sepm.assignment.individual.restaurant.util.DBUtil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class OrderedProductDAOJDBC implements OrderedProductDAO {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void create(OrderedProduct orderedProduct) throws DaoException {
        LOG.info("Create ordered product: {}", orderedProduct);
        try {
            String orderedProductInsertString = "INSERT INTO OrderedProducts (id,pid,orderId,amount,name,bruttoPrice) VALUES (default,?,?,?,?,?)";
            PreparedStatement preparedStatement = DBUtil.getConnection().prepareStatement(orderedProductInsertString, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, orderedProduct.getProductID());
            preparedStatement.setInt(2, orderedProduct.getOrderID());
            preparedStatement.setInt(3, orderedProduct.getAmount());
            preparedStatement.setString(4, orderedProduct.getName());
            preparedStatement.setDouble(5, orderedProduct.getBruttoPrice());

            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            generatedKeys.next();
            orderedProduct.setId(generatedKeys.getInt(1));

            LOG.info("Order successfully added to database {}", orderedProduct);
        } catch (SQLException | NullPointerException sql) {
            LOG.error("Unable to create new ordered product in database {}, sql exception.");
            throw new DaoException("SQL Exception!");
        }
    }

    @Override
    public void update(OrderedProduct orderedProduct) throws DaoException {
        LOG.info("Updating orderedProduct to DB {}", orderedProduct);
        try {
            String orderUpdateString = "UPDATE OrderedProducts SET amount=?  WHERE id=?";
            PreparedStatement preparedStatement = DBUtil.getConnection().prepareStatement(orderUpdateString, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, orderedProduct.getAmount());
            preparedStatement.setInt(2, orderedProduct.getId());
            preparedStatement.executeUpdate();
            LOG.info("Successfully updated to Database {}", orderedProduct);
        } catch (NullPointerException | SQLException e) {
            LOG.error("SQL Exception while updating Order");
            throw new DaoException("Null pointer or sql exception");
        }
    }

    @Override
    public void delete(OrderedProduct orderedProduct) throws DaoException {
        String productDeleteString = "DELETE FROM OrderedProducts WHERE orderId = ?";
        LOG.info("Product is going to be deleted {}", orderedProduct);
        try {
            PreparedStatement preparedStatement = DBUtil.getConnection().prepareStatement(productDeleteString, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, orderedProduct.getOrderID());
            preparedStatement.executeUpdate();
            LOG.info("Product deleted from db{}", orderedProduct);
        } catch (NullPointerException | SQLException e) {
            LOG.error("SQL Exeption while deleting Products");
            throw new DaoException(e.getMessage());
        }
        LOG.info("Product successfully deleted{}",orderedProduct);
    }

    @Override
    public ObservableList<OrderedProduct> findOrderId(Integer orderId) throws DaoException {
        LOG.info("finding order id in db");
        ObservableList<OrderedProduct> orderedProducts = FXCollections.observableArrayList();
        try {
            PreparedStatement preparedStatement = DBUtil.getConnection().prepareStatement("SELECT * FROM OrderedProducts WHERE orderId = " + orderId + ";");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                OrderedProduct orderedProduct1 = new OrderedProduct();
                orderedProduct1.setId(rs.getInt(1));
                orderedProduct1.setProductID(rs.getInt(2));
                orderedProduct1.setOrderID(rs.getInt(3));
                orderedProduct1.setAmount(rs.getInt(4));
                orderedProduct1.setName(rs.getString(5));
                orderedProduct1.setBruttoPrice(rs.getDouble(6));
                orderedProducts.add(orderedProduct1);
            }
            rs.close();
            preparedStatement.close();
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            throw new DaoException(e.getMessage());
        }
        if (!orderedProducts.isEmpty()) {
            LOG.info("Ordered products id found", orderId);
        } else {
            LOG.info("No ordered products with id {}", orderId);
        }
        return orderedProducts;
    }
}