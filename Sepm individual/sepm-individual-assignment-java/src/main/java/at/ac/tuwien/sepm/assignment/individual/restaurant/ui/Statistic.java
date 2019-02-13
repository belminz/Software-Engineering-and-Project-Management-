package at.ac.tuwien.sepm.assignment.individual.restaurant.ui;

import at.ac.tuwien.sepm.assignment.individual.restaurant.entities.Order;
import at.ac.tuwien.sepm.assignment.individual.restaurant.service.*;
import at.ac.tuwien.sepm.assignment.individual.restaurant.service.impl.OrderServiceImpl;
import at.ac.tuwien.sepm.assignment.individual.restaurant.service.impl.OrderedProductServiceImpl;
import at.ac.tuwien.sepm.assignment.individual.restaurant.service.impl.ProductServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;


public class Statistic {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    private OrderService orderService = new OrderServiceImpl();
    ObservableList<Order> findAllBills = FXCollections.observableArrayList();
    private ProductService productService = new ProductServiceImpl();
    private OrderedProductService orderedProductService = new OrderedProductServiceImpl();



    @FXML
    private BarChart<String,Integer> barChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;
    @FXML
    private DatePicker dp_BeginDate;
    @FXML
    private DatePicker dp_EndDate;
    @FXML
    private Button showStatisticButton;


    private Stage stage;



        @FXML
    public void initialize(){
            try {
                findAllBills=orderService.findAllBills();
            } catch (ServiceException e) {
                LOG.error(e.getMessage());
            }
    }

    public void onShowStatisticButtonClicked(ActionEvent actionEvent) {

    }
}
