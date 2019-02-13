package at.ac.tuwien.sepm.assignment.individual.restaurant.ui;

import at.ac.tuwien.sepm.assignment.individual.restaurant.entities.Order;
import at.ac.tuwien.sepm.assignment.individual.restaurant.entities.OrderedProduct;
import at.ac.tuwien.sepm.assignment.individual.restaurant.entities.Product;
import at.ac.tuwien.sepm.assignment.individual.restaurant.service.*;
import at.ac.tuwien.sepm.assignment.individual.restaurant.service.impl.OrderServiceImpl;
import at.ac.tuwien.sepm.assignment.individual.restaurant.service.impl.OrderedProductServiceImpl;
import at.ac.tuwien.sepm.assignment.individual.restaurant.service.impl.ProductServiceImpl;
import at.ac.tuwien.sepm.assignment.individual.restaurant.ui.alerts.AlertClass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.Iterator;

public class AddNewOrderController {



    private AlertClass alert = new AlertClass();
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private boolean isUpdate = false;
    @FXML
    private TextField enterTableNumber;
    @FXML
    private Text enterTNumber;
    @FXML
    private Button pay;
    @FXML
    private Button back;
    @FXML
    private TableView<OrderedProduct> tableProductToOrder;
    @FXML
    private TableColumn<OrderedProduct, Integer> amount;
    @FXML
    private TableColumn<OrderedProduct, String> nameOrderPr;

    @FXML
    private TableView<Product> productTableView;
    @FXML
    private TableColumn<Product, String> name;
    @FXML
    private TableColumn<Product, Double> bruttoPrice;
    @FXML
    private TableColumn<Product, String> category;

    public TableView<Product> getProductTableView() {
        return productTableView;
    }

    public Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private ProductService productService = new ProductServiceImpl();
    private OrderService orderService = new OrderServiceImpl();
    private Order order;
    private ObservableList<Product> productsList = FXCollections.observableArrayList();
    private ObservableList<Order> orderList = FXCollections.observableArrayList();
    private OrderedProductService orderedProductService = new OrderedProductServiceImpl();
    private ObservableList<Order> findAllBills = FXCollections.observableArrayList();
    private int biggestBillNumber = 0;

    @FXML
    private void initialize() {
        try {
           findAllBills=orderService.findAllBills();
        } catch (ServiceException e) {
            LOG.error(e.getMessage());
        }
        for (Order order : findAllBills){
            int current = order.getBillNumber();
            if (current>biggestBillNumber){
                biggestBillNumber = current;
            }
        }


        productTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableProductToOrder.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        category.setCellValueFactory(new PropertyValueFactory<>("category"));
        bruttoPrice.setCellValueFactory(new PropertyValueFactory<>("bruttoPrice"));
        nameOrderPr.setCellValueFactory(new PropertyValueFactory<>("name"));
        amount.setCellValueFactory(new PropertyValueFactory<>("amount"));


        fillProductTable();

        if (order != null) {

            enterTableNumber.setText(order.getTableNumber().toString());
            tableProductToOrder.setItems(order.getProductsToBeAdded());
            isUpdate = true;
        } else {
            isUpdate = false;
        }
    }



    private void fillProductTable() {
        try {
            productsList = productService.findAll();
        } catch (ServiceException e) {
        LOG.error(e.getMessage());
        }
        productTableView.setItems(productsList);

    }

    public void onBackClick(ActionEvent actionEvent) {
        LOG.info("Back button clicked");
        stage.close();
    }


    public void onPayClick(ActionEvent actionEvent) {
        order.setBillNumber(order.getBillNumber());
        biggestBillNumber++;
        LOG.info("On pay button clicked");
        if (isNumberTableNumber(enterTableNumber.getText())) {
            ObservableList<OrderedProduct> selectedProducts = tableProductToOrder.getItems();

            if (!selectedProducts.isEmpty()) {
                if (alert.showConfirmation("Do you want to bill order?")) {
                    String paymentMethod = alert.paymentMethod();
                    if (paymentMethod.equals("cash")) {
                        order.setTableNumber(Integer.parseInt(enterTableNumber.getText()));
                        order.setTimeOfCreation(order.getTimeOfCreation());
                        order.setStatus("billed");
                        order.setBillDate(LocalDateTime.now());
                        order.setPayMethod("cash");
                        order.setBillNumber(biggestBillNumber);
                        try {
                            orderService.billOrder(order);
                        } catch (ServiceException e) {
                            LOG.error(e.getMessage());
                        }

                    } else if (paymentMethod.equals("card")) {
                        order.setTableNumber(Integer.parseInt(enterTableNumber.getText()));
                        order.setTimeOfCreation(order.getTimeOfCreation());
                        order.setStatus("billed");
                        order.setBillDate(LocalDateTime.now());
                        order.setPayMethod("card");
                        order.setBillNumber(biggestBillNumber);
                        try {
                            orderService.billOrder(order);
                        } catch (ServiceException e) {
                            LOG.error(e.getMessage());
                        }
                    }
                    try {
                        if (isUpdate) {
                            orderService.update(order);
                            for (OrderedProduct orderedProduct : order.getProductsToBeAdded()) {
                                if (orderedProduct.getId() == null) {
                                    orderedProduct.setOrderID(order.getId());
                                    orderedProductService.saveOrderedProduct(orderedProduct);
                                } else {
                                    orderedProductService.update(orderedProduct);
                                }
                            }
                        } else {
                            orderService.saveOrder(order);
                            for (OrderedProduct orderedProduct : order.getProductsToBeAdded()) {
                                orderedProduct.setOrderID(order.getId());

                                orderedProductService.saveOrderedProduct(orderedProduct);
                            }
                        }
                    } catch (ServiceException e) {
                        LOG.error(e.getMessage());
                    }
                    alert.showInfo("Order is paid.");
                    LOG.info("Order successfully paid");
                    Stage stage = (Stage) pay.getScene().getWindow();
                    stage.close();
                }
            } else {
                alert.showInfo("Please select one or more products!");
                LOG.error("ERROR: No product selected.");
            }
        }
    }

    public void onAddToOrderClicked(ActionEvent actionEvent) {
            LOG.info("On add to order button clicked");
            try {
                if (productTableView.getSelectionModel().getSelectedItem() == null) {
                    alert.showWarning("Please select  product to order.");
                    LOG.error("Nothing selected");
                }
            } catch (NullPointerException n) {
                LOG.error(n.getMessage());
            }
            if (this.order == null) {
                this.order = new Order();
            }
            Product product = productTableView.getSelectionModel().getSelectedItem();
            //When one new product is beeing added
            if (order.getProductsToBeAdded().size() == 0 && product != null) {
                OrderedProduct orderedProduct = new OrderedProduct();
                orderedProduct.setName(product.getName());
                orderedProduct.setProductID(product.getPid());
                orderedProduct.setAmount(1);
                orderedProduct.setBruttoPrice(product.getBruttoPrice());
                order.getProductsToBeAdded().add(orderedProduct);
                tableProductToOrder.setItems(order.getProductsToBeAdded());
            } else {
                int countFoundProducts = 0;
                //When that product is added multiple times
                    for (OrderedProduct orderedProduct : order.getProductsToBeAdded()) {
                        if (product != null && orderedProduct.getProductID().equals(product.getPid())) {
                            orderedProduct.setName(product.getName());
                            orderedProduct.setBruttoPrice(product.getBruttoPrice());
                            orderedProduct.setAmount(orderedProduct.getAmount() + 1);
                            tableProductToOrder.refresh();
                            countFoundProducts++;
                        }
                }
                //When new product is also added.
                if (countFoundProducts == 0 && product != null) {
                    OrderedProduct orderedProduct = new OrderedProduct();
                    orderedProduct.setProductID(product.getPid());
                    orderedProduct.setAmount(1);
                    orderedProduct.setBruttoPrice(product.getBruttoPrice());
                    orderedProduct.setName(product.getName());
                    order.getProductsToBeAdded().add(orderedProduct);
                    tableProductToOrder.setItems(order.getProductsToBeAdded());
                }
        }
    }

    public void onDeleteFromOrderClicked(ActionEvent actionEvent) {
        LOG.info("On delete button clicked");
        OrderedProduct selectedItemsForDelete = tableProductToOrder.getSelectionModel().getSelectedItem();
        if (selectedItemsForDelete == null) {
            alert.showWarning("Please select one or more products to delete.");
            LOG.error("ERROR: No order to delete is selected");
        }
        else {
            if (selectedItemsForDelete.getAmount() == 1) {
                tableProductToOrder.getItems().remove(selectedItemsForDelete);
            } else {
                selectedItemsForDelete.setAmount(selectedItemsForDelete.getAmount() - 1);
                tableProductToOrder.refresh();
            }
            LOG.info("Product successfully removed from order table");
        }
    }

    private boolean isNumberTableNumber(String isNumber) {
        if (isNumber.isEmpty()) {
            alert.showInfo("Table number cannot be empty!");
            LOG.error("Field table number initialized with null.");
            return false;
        }
        try {
            int isNumberTemp = Integer.valueOf(isNumber);

            if (isNumberTemp < 1) {
                alert.showInfo("Table number has to be positive number.");
                LOG.error("ERROR: Zero or negative integer value used.");
                return false;
            }
        } catch (NumberFormatException nfe) {
            alert.showInfo("Please enter positive number!");
            LOG.error("ERROR: Not a numeric value set for Table Number.");
            return false;
        }
        return true;
    }

    public void setOrder(Order order){
        this.order=order;
    }

    public void onSaveToAllOrdersClicked(ActionEvent actionEvent) {
        LOG.info("On save to all orders clicked");
        if (isNumberTableNumber(enterTableNumber.getText())) {
            try {
                ObservableList<OrderedProduct> selectedProducts = tableProductToOrder.getItems();
                if (!selectedProducts.isEmpty()) {
                    // create new order
                    order.setStatus("open");
                    order.setTimeOfCreation(order.getTimeOfCreation());
                    order.setBillDate(LocalDateTime.now());
                    boolean existingTable = false;
                    Integer newTableNumber = Integer.parseInt(enterTableNumber.getText());
                    if (!isUpdate || !order.getTableNumber().equals(newTableNumber)) {
                        orderList = orderService.findAllOrders();

                        for (Order order1 : orderList) {
                            if (newTableNumber.equals(order1.getTableNumber())) {
                                existingTable = true;
                                alert.showWarning("This table number is not free. Please select other table number");
                                LOG.info("Selected table is not free.");
                                break;
                            }
                        }
                    }
                    if (!existingTable) {
                        order.setTableNumber(newTableNumber);
                        if (isUpdate) {
                            orderService.update(order);
                            for (OrderedProduct orderedProduct : order.getProductsToBeAdded()) {
                                if (orderedProduct.getId() == null) {
                                    orderedProduct.setOrderID(order.getId());
                                    orderedProductService.saveOrderedProduct(orderedProduct);
                                } else {
                                    orderedProductService.update(orderedProduct);
                                }
                            }
                        } else {
                            orderService.saveOrder(order);
                            for (OrderedProduct orderedProduct : order.getProductsToBeAdded()) {
                                orderedProduct.setOrderID(order.getId());

                                orderedProductService.saveOrderedProduct(orderedProduct);
                            }
                        }
                        alert.showInfo("Your order is saved in All orders.");
                        LOG.info("Order saved to OrderTable successfully");
                    }
                }
                else {
                    alert.showWarning("You have to select product to order.");
                }
            } catch (ServiceException e) {
                LOG.error(e.getMessage());
            }
        }
        LOG.error("Table number not correct.");
    }
}



