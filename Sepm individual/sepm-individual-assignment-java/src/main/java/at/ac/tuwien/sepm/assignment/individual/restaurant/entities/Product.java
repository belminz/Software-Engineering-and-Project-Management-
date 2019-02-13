package at.ac.tuwien.sepm.assignment.individual.restaurant.entities;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Product {

    private String name;
    private String description;
    private Integer nettoPrice;
    private Double bruttoPrice;
    private LocalDateTime timeOfCreation;
    private Integer pid;
    private String category;
    private String tax;
    private LocalDateTime edit_date;
    private boolean isProductDeleted;
    private Integer amount;


    public Product(){};

    public Product(Integer pid,String name, String description, Integer nettoPrice, LocalDateTime timeOfCreation, String category,String tax, LocalDateTime edit_date) {
        this.pid = pid;
        this.name = name;
        this.description = description;
        this.nettoPrice = nettoPrice;
        this.timeOfCreation = timeOfCreation;
        this.category = category;
        this.tax = tax;
        this.edit_date = edit_date;
        //Calculate bruttoPrice if we have netto price, formula.
        this.bruttoPrice = calculateBrutto();
    }
    public Product(Integer pid, String name, String description, Integer nettoPrice, LocalDateTime timeOfCreation,String category,String tax,LocalDateTime edit_date,boolean isProductDeleted){
        this.pid=pid;
        this.name=name;
        this.description=description;
        this.nettoPrice=nettoPrice;
        this.timeOfCreation=timeOfCreation;
        this.category=category;
        this.tax=tax;
        this.edit_date=edit_date;
        this.isProductDeleted=isProductDeleted;
    }


    public Product(String name, String description, Integer nettoPrice,String category, String tax) {
        this.name = name;
        this.description = description;
        this.nettoPrice = nettoPrice;
        this.category = category;
        this.tax = tax;
        this.bruttoPrice = calculateBrutto();

    }

    private String nettoPriceMin;
    private String nettoPriceMax;
    /**
     *  This constructor is just used for "search" story/function.
     */

    public Product(String name, String category, String nettoPriceMin, String nettoPriceMax) {
        this.name = name;
        this.category = category;
        this.nettoPriceMin =nettoPriceMin;
        this.nettoPriceMax = nettoPriceMax;
    }

    public String getNettoPriceMin() {
        return nettoPriceMin;
    }

    public void setNettoPriceMin(String nettoPriceMin) {
        this.nettoPriceMin = nettoPriceMin;
    }

    public String getNettoPriceMax() {
        return nettoPriceMax;
    }

    public void setNettoPriceMax(String nettoPriceMax) {
        this.nettoPriceMax = nettoPriceMax;
    }

    public Double getBruttoPrice() {
        return bruttoPrice;
    }

    public void setBruttoPrice(Double bruttoPrice) {
        this.bruttoPrice = bruttoPrice;
    }

    public LocalDateTime getTimeOfCreation() {
        return timeOfCreation;
    }

    public void setTimeOfCreation(LocalDateTime timeOfCreation) {
        this.timeOfCreation = timeOfCreation;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public void setProductDeleted(boolean isProductDeleted) {
        this.isProductDeleted = isProductDeleted;
    }

    public boolean isProductDeleted() {
        return isProductDeleted;
    }

    public LocalDateTime getEdit_date() {
        return edit_date;
    }

    public void setEdit_date(LocalDateTime edit_date) {
        this.edit_date = edit_date;
    }




    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getId() {
        return pid;
    }

    public void setId(Integer pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getNettoPrice() {
        return nettoPrice;
    }

    public void setNettoPrice(Integer nettoPrice) {
        this.nettoPrice = nettoPrice;
    }

    public LocalDateTime getDateOfCreation() {
        return timeOfCreation;
    }

    public void setDateOfCreation(LocalDateTime timeOfCreation) {
        this.timeOfCreation = timeOfCreation;
    }


    public Double calculateBrutto() {
        if (this.tax == null) {
            throw new NullPointerException();
        }
        return this.nettoPrice + this.nettoPrice*(Double.parseDouble(this.tax.substring(0,2))/100);
    }

    @Override
    public String toString() {
        return "Product{" +
            "name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", nettoPrice=" + nettoPrice +
            ", bruttoPrice=" + bruttoPrice +
            ", timeOfCreation=" + timeOfCreation +
            ", pid=" + pid +
            ", category='" + category + '\'' +
            ", tax='" + tax + '\'' +
            ", edit_date=" + edit_date +
            ", isProductDeleted=" + isProductDeleted +
            ", amount=" + amount +
            '}';
    }
}
