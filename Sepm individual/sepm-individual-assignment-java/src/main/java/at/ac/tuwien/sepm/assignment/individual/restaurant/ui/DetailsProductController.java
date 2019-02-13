package at.ac.tuwien.sepm.assignment.individual.restaurant.ui;

import at.ac.tuwien.sepm.assignment.individual.restaurant.entities.Product;
import at.ac.tuwien.sepm.assignment.individual.restaurant.service.ProductService;
import at.ac.tuwien.sepm.assignment.individual.restaurant.service.ServiceException;
import at.ac.tuwien.sepm.assignment.individual.restaurant.service.impl.ProductServiceImpl;
import at.ac.tuwien.sepm.assignment.individual.restaurant.ui.alerts.AlertClass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.Collections;

public class DetailsProductController {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private ProductService productService = new ProductServiceImpl();
    private AlertClass alert  = new AlertClass();

    @FXML
    private Button delete;
    @FXML
    private TextField tf_name;
    @FXML
    private TextField tf_description;
    @FXML
    private TextField tf_nettoprice;
    @FXML
    private CheckBox starter;
    @FXML

    private CheckBox maindish;
    @FXML
    private CheckBox dessert;
    @FXML
    private CheckBox tenPercent;
    @FXML
    private CheckBox twentyPercent;
    @FXML
    private Button addProduct;
    @FXML
    private Button exit;
    @FXML
    private TextField tf_timeOfCreation;
    @FXML
    private TextField tf_lastModified;
    @FXML
    private TextField tf_bruttoPrice;

    public Stage stage;

    Product product;

    public void setProduct(Product product) {
        this.product = product;
    }

    @FXML
    private void initialize() {
        addProduct.setDisable(false);
        tf_bruttoPrice.setDisable(true);
        if (product != null) {
            tf_name.setText(product.getName());
            tf_nettoprice.setText(product.getNettoPrice().toString());
            tf_description.setText(product.getDescription());
            tf_bruttoPrice.setText(product.getBruttoPrice().toString());

            tf_timeOfCreation.setText((product.getTimeOfCreation().toLocalDate() + " " + product.getTimeOfCreation().toLocalTime()).substring(0,19));
            tf_lastModified.setText((product.getEdit_date().toLocalDate() + " " + product.getEdit_date().toLocalTime()).substring(0,19));
            if (product.getCategory().equals("Starter")) {
                starter.setSelected(true);
            } else if (product.getCategory().equals("Main dish")) {
                maindish.setSelected(true);
            } else {
                dessert.setSelected(true);
            }
            if (product.getTax().equals("10%")){
                tenPercent.setSelected(true);
            } else {
                twentyPercent.setSelected(true);
            }
            tf_timeOfCreation.setEditable(false);
            tf_lastModified.setEditable(false);
        }
    }

    @FXML
    void handleTenPercent(ActionEvent actionEvent) {
        if (tenPercent.isSelected()){
            twentyPercent.setSelected(false);
        }
    }
    @FXML
    void handleTwentyPercent(ActionEvent actionEvent) {
        if (twentyPercent.isSelected()){
            tenPercent.setSelected(false);
        }
    }

    @FXML void handleStarter(){
        if (starter.isSelected()){
            maindish.setSelected(false);
            dessert.setSelected(false);
        }
    }
    @FXML void handleMainDish(){
        if (maindish.isSelected()){
            starter.setSelected(false);
            dessert.setSelected(false);
        }

    }
    @FXML void handleDessert() {
        if (dessert.isSelected()) {
            starter.setSelected(false);
            maindish.setSelected(false);
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private boolean isNumber (String isNumber) {
        if (isNumber.isEmpty()) {
            alert.showInfo("Price cannot be empty!");
            LOG.error("Field price initialized with null.");
            return false;
        }
        try {
            int isNumberTemp = Integer.valueOf(isNumber);

            if (isNumberTemp < 1) {
                alert.showWarning("Netto price is positive number!");
                LOG.error("ERROR: Zero or negative integer value used.");
                return false;
            }
        }
        catch(NumberFormatException nuFE) {
            alert.showInfo("Please enter positive number!");
            LOG.error("ERROR: Not a numeric value set for Netto price.");
            return false;
        }
        return true;
    }

    public void addProduct(ActionEvent actionEvent) {
        LOG.info("Add product clicked");
        if (!tf_name.getText().isEmpty() && !tf_nettoprice.getText().isEmpty() ) {
            if (isNumber(tf_nettoprice.getText())){
                String typeOfFooD;
                String tax;

                if (starter.isSelected()) {
                    typeOfFooD = "Starter";
                }
                else if (maindish.isSelected()) {
                    typeOfFooD = "Main dish";
                }
                else  if (dessert.isSelected()) {
                    typeOfFooD = "Dessert";
                }else {
                    alert.showWarning("Category cannot be empty.");
                    LOG.error("Category empty!");
                    return;
                }
                if (tenPercent.isSelected()) {
                    tax = "10%";
                } else if (twentyPercent.isSelected()){
                    tax = "20%";
                }else {
                    alert.showWarning("Tax cannot be empty");
                    LOG.error("tax empty");
                    return;
                }
                Product newProduct = new Product(tf_name.getText(), tf_description.getText(), Integer.parseInt(tf_nettoprice.getText()), typeOfFooD, tax);
                newProduct.setDateOfCreation(LocalDateTime.now());
                newProduct.setEdit_date(LocalDateTime.now());
                newProduct.setProductDeleted(false);
                try {
                    if (product == null) {
                        productService.saveProduct(newProduct);
                    } else {
                        newProduct.setId(product.getId());
                        productService.updateProduct(newProduct);
                    }
                    LOG.info("Successfully added!");
                    stage.close();
                } catch (ServiceException e) {
                    LOG.error(e.getMessage());
                }
            }
        } else {
            alert.showWarning("Name and Netto price are required fields!");
            LOG.error("Name and netto have to be entered");
        }
    }

    public void onExitClicked(ActionEvent actionEvent) {
        stage.close();
    }

        private ObservableList<Product> products = FXCollections.observableArrayList();

    public void onDeleteProductClicked(ActionEvent actionEvent) {
        products.add(product);
        try {
            if (alert.showConfirmation("Do you want to delete product?")) {
                productService.deleteMoreProducts(products);
                Stage stage = (Stage) delete.getScene().getWindow();
                stage.close();
            }
        } catch (ServiceException e) {
            LOG.error(e.getMessage());
        }
    }

    public void onAddToOrderClicked(ActionEvent actionEvent) {
        LOG.info("Edit order clicked");
        Stage stage = new Stage();
        stage.setTitle("Edit Order");
        try {
            AddNewOrderController addNewOrderController = new AddNewOrderController();
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/addNewOrderWithoutPay.fxml"));
            loader.setControllerFactory(param -> param.isInstance(addNewOrderController) ? addNewOrderController : null);
            Pane mainPane = loader.load();
            Scene scene = new Scene(mainPane);
            stage.setScene(scene);
            addNewOrderController.getProductTableView().setItems(FXCollections.observableList(Collections.singletonList(product)));
            addNewOrderController.setStage(stage);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            LOG.error("ERROR by edit");
        }
    }
}

