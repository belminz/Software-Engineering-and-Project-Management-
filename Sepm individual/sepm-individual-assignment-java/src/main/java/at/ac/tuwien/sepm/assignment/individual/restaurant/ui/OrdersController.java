package at.ac.tuwien.sepm.assignment.individual.restaurant.ui;

import at.ac.tuwien.sepm.assignment.individual.restaurant.entities.Order;
import at.ac.tuwien.sepm.assignment.individual.restaurant.entities.Product;
import at.ac.tuwien.sepm.assignment.individual.restaurant.service.*;
import at.ac.tuwien.sepm.assignment.individual.restaurant.service.impl.OrderServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class OrdersController {


    @FXML
    private TableView<Order> orderTableView;
    @FXML
    private TableColumn<Order, String> tableNumber;
    @FXML
    private TableColumn<Order, LocalDateTime> timeOfCreation;

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private OrderService orderService = new OrderServiceImpl();
    private ObservableList<Order> ordersList = FXCollections.observableArrayList();



    @FXML
    private void initialize() {
        LOG.info("INFO: Showing all Order types.");
        fillOrderTable();
        tableNumber.setCellValueFactory(new PropertyValueFactory<>("tableNumber"));
        timeOfCreation.setCellValueFactory(new PropertyValueFactory<>("timeOfCreation"));
        timeOfCreation.setSortType(TableColumn.SortType.DESCENDING);

        //Date formatting
        timeOfCreation.setCellFactory((TableColumn<Order, LocalDateTime> column) -> {
            return new TableCell<Order, LocalDateTime>() {
                @Override
                protected void updateItem(LocalDateTime item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item.getYear() +"-"+item.getMonthValue()+ "-" +  item.getDayOfMonth() + " "  + " " + item.getHour() + ":" + item.getMinute());
                    }
                }
            };
        });
    }

    private void fillOrderTable(){
        LOG.info("Loading all Orders!");
        try {
            ordersList = orderService.findAllOrders();
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        orderTableView.setItems(ordersList);
        orderTableView.getSortOrder().add(timeOfCreation);


    }

    public void editOrder(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() >= 2) {
            LOG.info("Edit order clicked");
            Stage stage = new Stage();
            stage.setTitle("Edit Order");
            try {
                AddNewOrderController addNewOrderController = new AddNewOrderController();
                addNewOrderController.setOrder(orderTableView.getSelectionModel().getSelectedItem());
                FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/addNewOrder.fxml"));
                loader.setControllerFactory(param -> param.isInstance(addNewOrderController) ? addNewOrderController : null);
                Pane mainPane = loader.load();
                Scene scene = new Scene(mainPane);
                stage.setScene(scene);

                addNewOrderController.setStage(stage);
                stage.setResizable(false);
                stage.setOnCloseRequest(event -> {
                    fillOrderTable();
                });
                stage.setOnHiding(event -> {
                    fillOrderTable();
                    }
                );
                stage.show();
            } catch (IOException e) {
                LOG.error("ERROR by edit");
            }
        }
    }
}
