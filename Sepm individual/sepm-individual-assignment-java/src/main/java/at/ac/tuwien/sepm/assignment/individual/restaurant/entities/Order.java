package at.ac.tuwien.sepm.assignment.individual.restaurant.entities;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Date;
import java.time.LocalDateTime;

public class Order {

    private Integer orderId;
    private Integer tableNumber;
    private LocalDateTime timeOfCreation;
    private String status;
    private LocalDateTime billDate;
    private String payMethod;
    private Integer billNumber;
    private ObservableList<OrderedProduct> productsToBeAdded = FXCollections.observableArrayList();

    public Order(Integer tableNumber, LocalDateTime timeOfCreation, String status, LocalDateTime billDate, String payMethod) {
        this.tableNumber = tableNumber;
        this.timeOfCreation = timeOfCreation;
        this.status = status;
        this.billDate = billDate;
        this.payMethod = payMethod;
    }

    public Order(Integer tableNumber, LocalDateTime timeOfCreation,String status,LocalDateTime billDate) {
        this.tableNumber = tableNumber;
        this.timeOfCreation = timeOfCreation;
        this.status = status;
        this.billDate= billDate;
    }
    public Order(){};

    public Order(Integer tableNumber, String status, LocalDateTime bill_date) {
        this.tableNumber = tableNumber;
        this.status = status;
        this.billDate = bill_date;
    }

    public Integer getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(Integer billNumber) {
        this.billNumber = billNumber;
    }

    public LocalDateTime getBillDate() {
        return billDate;
    }

    public void setBillDate(LocalDateTime billDate) {
        this.billDate = billDate;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public Integer getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(Integer tableNumber) {
        this.tableNumber = tableNumber;
    }

    public LocalDateTime getTimeOfCreation() {
        return timeOfCreation;
    }

    public void setTimeOfCreation(LocalDateTime timeOfCreation) {
        this.timeOfCreation = timeOfCreation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getId() {
        return orderId;
    }

    public void setId(Integer orderId) {
        this.orderId = orderId;
    }

    public ObservableList<OrderedProduct> getProductsToBeAdded() {
        return productsToBeAdded;
    }

    public void setProductsToBeAdded(ObservableList<OrderedProduct> productsToBeAdded) {
        this.productsToBeAdded = productsToBeAdded;
    }

    @Override
    public String toString() {
        return "Order{" +
            "orderId=" + orderId +
            ", tableNumber=" + tableNumber +
            ", timeOfCreation=" + timeOfCreation +
            ", status='" + status + '\'' +
            ", billDate=" + billDate +
            ", payMethod='" + payMethod + '\'' +
            ", billNumber=" + billNumber +
            ", productsToBeAdded=" + productsToBeAdded +
            '}';
    }
}
