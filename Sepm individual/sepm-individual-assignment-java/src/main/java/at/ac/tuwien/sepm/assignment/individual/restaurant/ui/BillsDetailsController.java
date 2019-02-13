package at.ac.tuwien.sepm.assignment.individual.restaurant.ui;

import at.ac.tuwien.sepm.assignment.individual.restaurant.entities.Order;
import at.ac.tuwien.sepm.assignment.individual.restaurant.entities.OrderedProduct;
import at.ac.tuwien.sepm.assignment.individual.restaurant.entities.Product;
import at.ac.tuwien.sepm.assignment.individual.restaurant.service.*;
import at.ac.tuwien.sepm.assignment.individual.restaurant.service.impl.OrderedProductServiceImpl;
import at.ac.tuwien.sepm.assignment.individual.restaurant.service.impl.ProductServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

public class BillsDetailsController {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private ObservableList<OrderedProduct> orderedProduct = FXCollections.observableArrayList();
    private OrderedProductService orderedProductService = new OrderedProductServiceImpl();

    Stage stage = new Stage();
    private Order order;

    @FXML
    private TextField tf_billNumber;
    @FXML
    private TextField tf_tableNumber;
    @FXML
    private TextField tf_timeOfCreation;
    @FXML
    private TextField tf_billDate;
    @FXML
    private TextField tf_sumOfProducts;
    @FXML
    private TextField tf_payedWith;
    @FXML
    private TableView<OrderedProduct> tableProductToOrder;
    @FXML
    private TableColumn<OrderedProduct, String> name;
    @FXML
    private TableColumn<OrderedProduct, Double> bruttoPrice;
    @FXML
    private TableColumn<OrderedProduct, Integer> amount;

    @FXML
    private TextField tf_taxSum1;
    @FXML
    private TextField tf_taxSum2;

    private ProductService productService = new ProductServiceImpl();

    public void setStage(Stage stage){
    this.stage = stage;
}
public void setOrder(Order order ){
    this.order=order;
}



public void initialize(){
    tf_timeOfCreation.setDisable(true);
    tf_sumOfProducts.setDisable(true);
    tf_payedWith.setDisable(true);
    tf_billDate.setDisable(true);
    tf_billNumber.setDisable(true);
    tf_tableNumber.setDisable(true);
    tf_taxSum1.setDisable(true);
    tf_taxSum2.setDisable(true);


    // set text fields
    tf_billNumber.setText(String.valueOf(this.order.getBillNumber()));
    tf_tableNumber.setText(String.valueOf(this.order.getTableNumber()));
    tf_timeOfCreation.setText(String.valueOf(this.order.getTimeOfCreation().toLocalDate() + " " +this.order.getTimeOfCreation().toLocalTime()).substring(0,19));
    tf_billDate.setText(String.valueOf(this.order.getBillDate().toLocalDate() + " " + this.order.getBillDate().toLocalTime()).substring(0,19));
    tf_payedWith.setText(String.valueOf(this.order.getPayMethod()));

    // set table columns
    name.setCellValueFactory(new PropertyValueFactory<>("name"));
    bruttoPrice.setCellValueFactory(new PropertyValueFactory<>("bruttoPrice"));
    amount.setCellValueFactory(new PropertyValueFactory<>("amount"));


    try {
        orderedProduct = orderedProductService.findOrderId(this.order.getId());
    } catch (ServiceException e) {
        LOG.error(e.getMessage());
    }
    tableProductToOrder.setItems(orderedProduct);


    tf_sumOfProducts.setText(String.valueOf(Double.toString(Math.floor(calcSum() * 100) / 100.0)));

   tf_taxSum1.setText(Double.toString(Math.floor(calcTax10() * 100) / 100.0));
    tf_taxSum2.setText(Double.toString(Math.floor(calcTax20() * 100) / 100.0));
}
   private Double calcSum() {
        double sum = 0.0;
        for(OrderedProduct orderedProduct :tableProductToOrder.getItems()){
            sum=sum+orderedProduct.getBruttoPrice()*orderedProduct.getAmount();

        }
         return sum;
    }

    private Double calcTax10() {
        double tax10 = 0.0;
        for (OrderedProduct orderedProduct : order.getProductsToBeAdded()) {
            Product product = null;
            try {
                product = productService.findID(orderedProduct.getProductID());
            } catch (ServiceException e) {
                LOG.error(e.getMessage());
            }
            if (product != null && product.getTax().equals("10%")) {
                tax10 += (product.getBruttoPrice() - product.getNettoPrice()) * orderedProduct.getAmount();
            }
        }
        return tax10;
    }

    private Double calcTax20 () {
        double tax20 = 0.0;
        for (OrderedProduct orderedProduct : order.getProductsToBeAdded()) {
            Product product = null;
            try {
                product = productService.findID(orderedProduct.getProductID());
            } catch (ServiceException e) {
                LOG.error(e.getMessage());
            }
            if (product != null && product.getTax().equals("20%")) {
                tax20 += (product.getBruttoPrice() - product.getNettoPrice()) * orderedProduct.getAmount();
            }
        }
        return tax20;
    }

}
