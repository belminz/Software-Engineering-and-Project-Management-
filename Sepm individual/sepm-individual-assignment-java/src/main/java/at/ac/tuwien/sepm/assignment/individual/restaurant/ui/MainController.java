package at.ac.tuwien.sepm.assignment.individual.restaurant.ui;
import at.ac.tuwien.sepm.assignment.individual.restaurant.entities.Product;
import at.ac.tuwien.sepm.assignment.individual.restaurant.service.ProductService;
import at.ac.tuwien.sepm.assignment.individual.restaurant.service.ServiceException;
import at.ac.tuwien.sepm.assignment.individual.restaurant.service.impl.ProductServiceImpl;
import at.ac.tuwien.sepm.assignment.individual.restaurant.ui.alerts.AlertClass;
import at.ac.tuwien.sepm.assignment.individual.restaurant.util.DBUtil;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;

public class MainController {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private AlertClass alert = new AlertClass();

    @FXML
    private TableView<Product> foodTableView;
    @FXML
    private TableColumn<Product, String> name;
    @FXML
    private TableColumn<Product, Integer> bruttoPrice;
    @FXML
    private TableColumn<Product, String> category;

    @FXML
    private TextField tf_nameSearch;
    @FXML
    private TextField tf_nettoMinSearch;
    @FXML
    private TextField tf_nettoMaxSearch;
    @FXML
    private TextField tf_bruttoMinSearch;
    @FXML
    private TextField tf_bruttoMaxSearch;
    @FXML
    private ComboBox category_comboSearch;


    private ProductService productService = new ProductServiceImpl();
    private ObservableList<Product> productList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        fillProductTable();
        // Select multiple product
        foodTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        LOG.info("INFO: Showing all Product types.");

        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        bruttoPrice.setCellValueFactory(new PropertyValueFactory<>("bruttoPrice"));
        category.setCellValueFactory(new PropertyValueFactory<>("category"));

        ObservableList<String> category = FXCollections.observableArrayList("Starter", "Main Dish", "Dessert");
        category_comboSearch.setItems(category);
    }

    private void fillProductTable() {
        LOG.info("Loading all Products!");
        try {
            productList = productService.findAll();
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }

        foodTableView.setItems(productList);
    }

    //On add Product clicked
    public void addProductClicked(ActionEvent actionEvent) {
        LOG.info("Add Product clicked");
        Stage stage = new Stage();
        stage.setTitle("Add New Product");
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/addNewProduct.fxml"));
            Pane mainPane = loader.load();
            Scene scene = new Scene(mainPane);
            stage.setScene(scene);
            AddNewProductController addNewProductController = loader.getController();
            addNewProductController.setStage(stage);
            stage.setResizable(false);
            stage.show();
            stage.setTitle("Add new product");
            stage.setOnHiding(event -> fillProductTable());
        } catch (IOException e) {
            LOG.error("ERROR: addNewProduct.fxml could not be found.");
        }
    }

    public void onOrdersClicked(ActionEvent actionEvent) {
        LOG.info("On orders clicked");
        Stage stage = new Stage();
        stage.setTitle("All orders");
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/orders.fxml"));
            Pane mainPane = loader.load();
            Scene scene = new Scene(mainPane);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            LOG.error("ERROR: orders.fxml could not be found.");
        }
    }

    public void onAddNewOrderClicked(ActionEvent actionEvent) {
        LOG.info("On add new order clicked");
        Stage stage = new Stage();
        stage.setTitle("Create new order");
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/addNewOrderWithoutPay.fxml"));
            Pane mainPane = loader.load();
            Scene scene = new Scene(mainPane);
            stage.setScene(scene);
            AddNewOrderController addNewOrderController = loader.getController();
            addNewOrderController.setStage(stage);
            stage.show();
        } catch (IOException e) {
            LOG.error("ERROR: addNewOrderWithoutPay.fxml could not be found.");
        }
    }

    public void onDeleteProductClicked(ActionEvent actionEvent) {
        LOG.info("Delete button clicked");
        Product nothingSelected = foodTableView.getSelectionModel().getSelectedItem();
        if (nothingSelected == null) {
            alert.showInfo("You have to select at least one product to delete.");
        } else {
            ObservableList<Product> selectedItems = foodTableView.getSelectionModel().getSelectedItems();
            if (alert.showConfirmation("Do you want to delete product/products?")) {
                try {
                    productService.deleteMoreProducts(selectedItems);
                    fillProductTable();
                    LOG.info("Product deleted!");
                } catch (Exception e) {
                    LOG.error(e.getMessage());
                    alert.showWarning(e.getMessage());
                }
            }
        }
    }

    public void editProduct(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() >= 2) {
            LOG.info("Edit Product clicked");
            Stage stage = new Stage();
            stage.setTitle("Edit Product");

            try {
                DetailsProductController detailsProductController = new DetailsProductController();
                detailsProductController.setProduct(foodTableView.getSelectionModel().getSelectedItem());
                FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/detailsProduct.fxml"));
                loader.setControllerFactory(param -> param.isInstance(detailsProductController) ? detailsProductController : null);
                Pane mainPane = loader.load();
                Scene scene = new Scene(mainPane);
                stage.setScene(scene);
                detailsProductController.setStage(stage);
                stage.setResizable(false);
                stage.show();
                stage.setOnHiding(event -> fillProductTable());
            } catch (IOException e) {
                LOG.error("ERROR by edit " + e.getMessage());
            }
        }
    }

    public void onExitButtonClicked(ActionEvent actionEvent) {
        LOG.info("Exit button clicked");
        boolean yes = alert.showConfirmation("Do you want to leave application?");
        if (yes) {
            DBUtil.closeConnection();
            Platform.exit();
        }
    }


    public void nettoPriceOnEdit(TableColumn.CellEditEvent<Product, Integer> productIntegerCellEditEvent) {

    }

    public void nameOnEdit(TableColumn.CellEditEvent<Product, String> productStringCellEditEvent) {
    }

    public void descriptionOnEdit(TableColumn.CellEditEvent<Product, String> productStringCellEditEvent) {

    }


    public void onSearchProductClicked(ActionEvent actionEvent) throws ServiceException {
        LOG.info("Search button clicked ...");
        String name = null;
        String nettoPriceMin = null;
        String nettoPriceMax = null;
        String category = null;
        ObservableList<Product> searchedProducts = productService.findAll();
        if (tf_nameSearch.getText().isEmpty() && tf_bruttoMinSearch.getText().isEmpty() && tf_bruttoMaxSearch.getText().isEmpty() && tf_nettoMinSearch.getText().isEmpty()
            && tf_nettoMaxSearch.getText().isEmpty() && category_comboSearch.getSelectionModel().isEmpty()) {
            LOG.error("All searching parameters are null");
            return;
        }

        if (!tf_nameSearch.getText().equals("")) {
            name = tf_nameSearch.getText();
        } else {
            name = "";
        }
        if (!tf_nettoMinSearch.getText().equals("") || !tf_nettoMaxSearch.getText().equals("")) {
            try {
                if (!tf_nettoMinSearch.getText().equals("") && isNumber(tf_nettoMinSearch.getText())) {
                    nettoPriceMin = String.valueOf(Integer.parseInt(tf_nettoMinSearch.getText()));
                } else {
                    nettoPriceMin="";
                }
                if (!tf_nettoMaxSearch.getText().equals("") && isNumber(tf_nettoMaxSearch.getText())) {
                    nettoPriceMax = String.valueOf(Integer.parseInt(tf_nettoMaxSearch.getText()));

                }else{
                    nettoPriceMax="";
                }
            } catch (NumberFormatException e) {
                //  showWarning("Values of number fields must be a number!");
                LOG.error("Invalid input in text field(s)");
                foodTableView.setItems(searchedProducts);
                return;
            }
        }else {
            tf_nettoMinSearch.setText("");
            tf_nettoMaxSearch.setText("");
            nettoPriceMin="";
            nettoPriceMax="";
        }

        if (!category_comboSearch.getSelectionModel().isEmpty()) {
            category = category_comboSearch.getValue().toString();
        } else {
            category = "";
        }
        Product product1 = new Product(name,category,nettoPriceMin,nettoPriceMax);
        ObservableList<Product> productSearchResults = productService.search(product1);

        if (!tf_bruttoMinSearch.getText().equals("") || !tf_bruttoMaxSearch.getText().equals("")) {
            try {
                if (!tf_bruttoMinSearch.getText().equals("") && isNumber(tf_bruttoMinSearch.getText())) {
                    productSearchResults.removeIf(product -> product.getBruttoPrice() <= Integer.parseInt(tf_bruttoMinSearch.getText()));
                }
                if (!tf_bruttoMaxSearch.getText().equals("") && isNumber(tf_bruttoMaxSearch.getText())) {
                    productSearchResults.removeIf(product -> product.getBruttoPrice() > Integer.parseInt(tf_bruttoMaxSearch.getText()));

                }
            } catch (NumberFormatException nfe){
                alert.showWarning("Values of number fields must be a number!");
                LOG.error("Invalid input in text field(s)");
                return;
            }
        }

        LOG.info("Searched product/s found!");
        foodTableView.setItems(productSearchResults);
    }

    private boolean isNumber(String isNumber) {
        if (isNumber.equals("")){
            return true;
        }
        try {
            int isNumberTemp = Integer.valueOf(isNumber);

            if (isNumberTemp < 1) {
                alert.showWarning("Netto/Brutto price is positive number!");
                LOG.error("ERROR: Zero or negative integer value used.");
                return false;
            }
        }
        catch(NumberFormatException nfe) {
            alert.showInfo("Please enter positive number > 0");
            LOG.error("ERROR: Not a numeric value set for Netto/Brutto price.");
            return false;
        }
        return true;
    }


    public void onRefreshTableClicked(ActionEvent actionEvent) {
        LOG.info("On refresh clicked");
        try {
            productList = productService.findAll();
        } catch (ServiceException e) {
           LOG.error(e.getMessage());
        }
        tf_nameSearch.clear();
        tf_bruttoMaxSearch.clear();
        tf_bruttoMinSearch.clear();
        tf_nettoMinSearch.clear();
        tf_nettoMaxSearch.clear();
        foodTableView.setItems(productList);
        LOG.info("Product table is successfully refreshed.");
    }

    public void onAllBillsClicked(ActionEvent actionEvent) {
        LOG.info("On all bills clicked");
        Stage stage = new Stage();
        stage.setTitle("All bills");
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/bills.fxml"));
            Pane mainPane = loader.load();
            Scene scene = new Scene(mainPane);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            LOG.error("ERROR: bills.fxml could not be found.");
        }

    }

    public void onStatisticButtonClicked(ActionEvent actionEvent) {
        LOG.info("On statistics clicked");
        Stage stage = new Stage();
        stage.setTitle("BarChart Statistics");
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/statistic.fxml"));
            Pane mainPane = loader.load();
            Scene scene = new Scene(mainPane);
            stage.setScene(scene);
            stage.setWidth(700);
            stage.setHeight(700);
            stage.show();
        } catch (IOException e) {
            LOG.error("ERROR: statistic.fxml could not be found.");
        }
    }
}
