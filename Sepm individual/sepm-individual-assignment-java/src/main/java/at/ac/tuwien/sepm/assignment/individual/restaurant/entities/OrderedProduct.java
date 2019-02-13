package at.ac.tuwien.sepm.assignment.individual.restaurant.entities;

public class OrderedProduct {

    private Integer id;
    private Integer pid;
    private Integer orderId;
    private String name;
    private Double bruttoPrice;
    private Integer amount = 0;

    public OrderedProduct(Integer id, Integer pid, Integer orderID, String name, Double bruttoPrice, Integer amount) {
        this.id = id;
        this.pid = pid;
        this.orderId = orderID;
        this.name = name;
        this.bruttoPrice = bruttoPrice;
        this.amount = amount;
    }
    public OrderedProduct (Integer pid, Integer orderId, Integer amount ,String name , Double bruttoPrice){
        this.pid=pid;
        this.orderId=orderId;
        this.amount=amount;
        this.name=name;
        this.bruttoPrice=bruttoPrice;
    }

    public OrderedProduct() {
    }

    public OrderedProduct(Integer pid) {
        this.pid = pid;
    }

    public OrderedProduct(Integer id, Integer pid, Integer ooid, Integer amount, String name, Double bruttoPrice) {
        this.id = id;
        this.pid=pid;
        this.orderId =ooid;
        this.amount = amount;
        this.name=name;
        this.bruttoPrice=bruttoPrice;
    }

    public OrderedProduct(Integer pid, Integer oid) {
        this.pid=pid;
        this.orderId=oid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductID() {
        return pid;
    }

    public void setProductID(Integer pid) {
        this.pid = pid;
    }

    public Integer getOrderID() {
        return orderId;
    }

    public void setOrderID(Integer orderID) {
        this.orderId = orderID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getBruttoPrice() {
        return bruttoPrice;
    }

    public void setBruttoPrice(Double bruttoPrice) {
        this.bruttoPrice = bruttoPrice;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

  /*  public Integer getBruttoPrice() {
        return bruttoPrice;
    }

    public void setBruttoPrice(Integer bruttoPrice) {
        this.bruttoPrice = bruttoPrice;
    }*/


    @Override
    public String toString() {
        return "OrderedProduct{" +
            "id=" + id +
            ", pid=" + pid +
            ", orderId=" + orderId +
            ", name='" + name + '\'' +
            ", bruttoPrice=" + bruttoPrice +
            ", amount=" + amount +
            '}';
    }
}
