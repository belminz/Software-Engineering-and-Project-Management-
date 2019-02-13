package at.ac.tuwien.sepm.assignment.individual.restaurant.dao.impl;
import at.ac.tuwien.sepm.assignment.individual.restaurant.dao.DaoException;
import at.ac.tuwien.sepm.assignment.individual.restaurant.dao.ProductDAO;
import at.ac.tuwien.sepm.assignment.individual.restaurant.entities.Product;
import at.ac.tuwien.sepm.assignment.individual.restaurant.util.DBUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.invoke.MethodHandles;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ProductDAOJDBC implements ProductDAO {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void create(Product product) throws DaoException {
        LOG.info("Create product: {}", product);
        try {
            String foodInsertString = "INSERT INTO Food (pid,name,description,nettoprice,timeOfCreation,category,tax,edit_date,isProductDeleted) VALUES (default,?,?,?,?,?,?,?,?)";
            PreparedStatement preparedStatement = DBUtil.getConnection().prepareStatement(foodInsertString, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, product.getName());
            preparedStatement.setString(2, product.getDescription());
            preparedStatement.setInt(3, product.getNettoPrice());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(product.getDateOfCreation()));
            preparedStatement.setString(5, product.getCategory());
            preparedStatement.setString(6, product.getTax());
            preparedStatement.setTimestamp(7, Timestamp.valueOf(product.getEdit_date()));
            preparedStatement.setBoolean(8, product.isProductDeleted());
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            generatedKeys.next();
            product.setId(generatedKeys.getInt(1));
            LOG.info("product successfully added to database {}", product);
        } catch (SQLException | NullPointerException sqlnull) {
            LOG.error("Unable to create new product in database, sql exception.",sqlnull);
            throw new DaoException(sqlnull.getMessage());
        }
    }


    @Override
    public ObservableList<Product> findAllProducts() throws DaoException {
        ObservableList<Product> observableList = FXCollections.observableArrayList();
        try {
            PreparedStatement ps = DBUtil.getConnection().prepareStatement("SELECT * FROM Food WHERE isProductDeleted='false'");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt(1));
                product.setName(rs.getString(2));
                product.setDescription(rs.getString(3));
                product.setNettoPrice(rs.getInt(4));
                product.setDateOfCreation(rs.getTimestamp(5).toLocalDateTime());
                product.setCategory(rs.getString(6));
                product.setTax(rs.getString(7));
                product.setEdit_date(rs.getTimestamp(8).toLocalDateTime());
                product.setProductDeleted(rs.getBoolean(9));
                product.setBruttoPrice(product.calculateBrutto());
                observableList.add(product);
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            LOG.error("SQL Exception while loading food");
            throw new DaoException(e.getMessage());
        }
        LOG.info("Product is  loaded from DB");
        return observableList;
    }


    @Override
    public void update(Product product) throws DaoException {
        LOG.info("Updating Product to DB {}", product);
        try {
            String foodUpdateString = "UPDATE Food SET name=?,description=?,nettoprice=?,category=?,tax=?, edit_date=?,isProductDeleted=? WHERE pid=?";
            PreparedStatement preparedStatement = DBUtil.getConnection().prepareStatement(foodUpdateString, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, product.getName());
            preparedStatement.setString(2, product.getDescription());
            preparedStatement.setInt(3, product.getNettoPrice());
            preparedStatement.setString(4, product.getCategory());
            preparedStatement.setString(5, product.getTax());
            preparedStatement.setTimestamp(6, Timestamp.valueOf(product.getEdit_date()));
            preparedStatement.setBoolean(7, product.isProductDeleted());
            preparedStatement.setInt(8, product.getId());

            preparedStatement.executeUpdate();
            LOG.info("Successfully updated to Database {}", product);
        } catch (NullPointerException | SQLException e) {
            LOG.error("SQL Exception while updating Products");
            throw new DaoException("Null pointer or sql exception");
        }
    }

    //Mark with setBool and setLong flags for delete and ID;
    @Override
    public void delete(Product product) throws DaoException {
        String productDeleteString = "UPDATE Food SET isProductDeleted=? WHERE pid=?";
        LOG.info("Product is going to be deleted {}", product);
        try {
            PreparedStatement preparedStatement = DBUtil.getConnection().prepareStatement(productDeleteString, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setBoolean(1, true);
            preparedStatement.setLong(2, product.getId());
            preparedStatement.executeUpdate();
            LOG.info("Product deleted from db{}", product);
        } catch (NullPointerException | SQLException e) {
            LOG.error("SQL Exeption while deleting Products");
            throw new DaoException(e.getMessage());
        }

    }

    @Override
    public ObservableList<Product> search(Product product) throws DaoException {
        LOG.info("Searching for products in Db...");
        String productSearch = "SELECT * FROM Food WHERE ";
        ObservableList<Product> productObservableList = FXCollections.observableArrayList();
        try {
            if (!product.getName().equals("")) {
                productSearch +="name LIKE '%" + product.getName() + "%' AND ";
            }
            if (!product.getNettoPriceMin().equals("") ) {
                productSearch +="nettoPrice >= " + product.getNettoPriceMin() + " AND ";
            }
            if (!product.getNettoPriceMax().equals("")) {
                productSearch +="nettoPrice < " + product.getNettoPriceMax() + " AND ";
            }
            if (!product.getCategory().equals("")) {
                productSearch +="category = '" + product.getCategory() + "' AND ";
            }
            productSearch += "isProductDeleted = false;";

            if (productSearch.endsWith("AND ")) {
                productSearch = productSearch.substring(0, productSearch.length() - 4);
            }
            productSearch +=";";
            PreparedStatement preparedStatement = DBUtil.getConnection().prepareStatement(productSearch);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Product product1= new Product();
                product1.setId(rs.getInt(1));
                product1.setName(rs.getString(2));
                product1.setDescription(rs.getString(3));
                product1.setNettoPrice(rs.getInt(4));
                product1.setDateOfCreation(rs.getTimestamp(5).toLocalDateTime());
                product1.setCategory(rs.getString(6));
                product1.setTax(rs.getString(7));
                product1.setEdit_date(rs.getTimestamp(8).toLocalDateTime());
                product1.setProductDeleted(rs.getBoolean(9));
                product1.setBruttoPrice(product1.calculateBrutto());

                productObservableList.add(product1);
            }
            rs.close();
            preparedStatement.close();
        } catch (SQLException sql) {
            LOG.error("SQL  Exception in searching for products");
            throw new DaoException(sql.getMessage());
        }
        return productObservableList;
    }



    @Override
    public Product findID(Integer ID) throws DaoException {
        LOG.info("finding products with the same IDs");
                Product product = new Product();
        try {
            PreparedStatement ps = DBUtil.getConnection().prepareStatement("SELECT * FROM Food WHERE pid = " + ID + ";");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                product.setId(rs.getInt(1));
                product.setName(rs.getString(2));
                product.setDescription(rs.getString(3));
                product.setNettoPrice(rs.getInt(4));
                product.setDateOfCreation(rs.getTimestamp(5).toLocalDateTime());
                product.setCategory(rs.getString(6));
                product.setTax(rs.getString(7));
                product.setEdit_date(rs.getTimestamp(8).toLocalDateTime());
                product.setProductDeleted(rs.getBoolean(9));
                product.setBruttoPrice(product.calculateBrutto());
            }
            rs.close();
        } catch (SQLException e) {
            LOG.error("SQL Exception while loading food");
            throw new DaoException("Error while finding id of product");
        }
        LOG.info("ID found in DB");
        return product;
    }
}