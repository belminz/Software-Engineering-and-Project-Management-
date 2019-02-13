package at.ac.tuwien.sepm.assignment.individual.restaurant.ui;

import at.ac.tuwien.sepm.assignment.individual.restaurant.entities.Order;
import at.ac.tuwien.sepm.assignment.individual.restaurant.service.OrderService;
import at.ac.tuwien.sepm.assignment.individual.restaurant.service.impl.OrderServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;

public class BillsController {


    @FXML
    private TableView<Order> billsTable;
    @FXML
    private TableColumn<Order, String> tableNumber;
    @FXML
    private TableColumn<Order, LocalDateTime> billDate;

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private ObservableList<Order> billsList = FXCollections.observableArrayList();
    private OrderService orderService = new OrderServiceImpl();

    @FXML
    public void initialize()  {
        fillBillsTable();
        LOG.info("INFO: Showing all Order types.");
        tableNumber.setCellValueFactory(new PropertyValueFactory<>("tableNumber"));
        billDate.setCellValueFactory(new PropertyValueFactory<>("billDate"));
        billDate.setSortType(TableColumn.SortType.DESCENDING);

        //Date formatting
        billDate.setCellFactory((TableColumn<Order, LocalDateTime> column) -> {
            return new TableCell<Order, LocalDateTime>() {
                @Override
                protected void updateItem(LocalDateTime item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item.getYear() +"-"+item.getMonthValue()+ "-" +  item.getDayOfMonth()+ " "  + " " + item.getHour() + ":" + item.getMinute());
                    }
                }
            };
        });
    }
    private void fillBillsTable(){
        LOG.info("Loading all Orders!");
        try {
            billsList = orderService.findAllBills();
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        billsTable.setItems(billsList);
        billsTable.getSortOrder().add(billDate);
    }

    public void editBill(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() >= 2) {
            LOG.info("Bill details clicked");
            Stage stage = new Stage();
            stage.setTitle("Bills detail");
            try {
                BillsDetailsController billsDetailsController = new BillsDetailsController();
                billsDetailsController.setOrder(billsTable.getSelectionModel().getSelectedItem());
                FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/billDetailsView.fxml"));
                loader.setControllerFactory(param -> param.isInstance(billsDetailsController) ? billsDetailsController : null);
                Pane mainPane = loader.load();
                Scene scene = new Scene(mainPane);
                stage.setScene(scene);

                billsDetailsController.setStage(stage);
                stage.setResizable(false);
                stage.show();
            } catch (IOException e) {
                LOG.error("ERROR cannot open billDetailsView.fxml");
            }
        }

    }
}
