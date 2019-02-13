package at.ac.tuwien.sepm.assignment.individual.restaurant.ui;

import at.ac.tuwien.sepm.assignment.individual.restaurant.entities.Product;
import at.ac.tuwien.sepm.assignment.individual.restaurant.service.ProductService;
import at.ac.tuwien.sepm.assignment.individual.restaurant.service.impl.ProductServiceImpl;
import at.ac.tuwien.sepm.assignment.individual.restaurant.service.ServiceException;
import at.ac.tuwien.sepm.assignment.individual.restaurant.ui.alerts.AlertClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;

public class AddNewProductController {


    private AlertClass alert = new AlertClass();
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private ProductService productService = new ProductServiceImpl();

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


    public Stage stage;

    Product product;

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    void handleTenPercent(ActionEvent actionEvent) {
        if (tenPercent.isSelected()) {
            twentyPercent.setSelected(false);
        }
    }

    @FXML
    void handleTwentyPercent(ActionEvent actionEvent) {
        if (twentyPercent.isSelected()) {
            tenPercent.setSelected(false);
        }
    }

    @FXML
    void handleStarter() {
        if (starter.isSelected()) {
            maindish.setSelected(false);
            dessert.setSelected(false);
        }
    }

    @FXML
    void handleMainDish() {
        if (maindish.isSelected()) {
            starter.setSelected(false);
            dessert.setSelected(false);
        }

    }

    @FXML
    void handleDessert() {
        if (dessert.isSelected()) {
            starter.setSelected(false);
            maindish.setSelected(false);
        }
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
        catch(NumberFormatException nfe) {
            alert.showInfo("Please enter positive integer number!");
            LOG.error("ERROR: Not a numeric value set for Netto price or not integer value set.");
            return false;
        }
        return true;
    }

    public void addProduct(ActionEvent actionEvent) {
        LOG.info("Add product clicked");
        if (!tf_name.getText().isEmpty() && !tf_nettoprice.getText().isEmpty() ) {
            if (isNumber(tf_nettoprice.getText())){
                addProduct.setDisable(false);
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
                    alert.showWarning("Category field cannot be empty.");
                    LOG.error("Category empty!");
                    return;
                }
                if (tenPercent.isSelected()) {
                    tax = "10%";
                } else if (twentyPercent.isSelected()){
                    tax = "20%";
                }else {
                    alert.showWarning("Tax field cannot be empty.");
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
}
