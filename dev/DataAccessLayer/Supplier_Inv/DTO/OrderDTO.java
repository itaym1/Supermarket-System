package DataAccessLayer.Supplier_Inv.DTO;

import java.time.LocalDate;

public class OrderDTO {

    private Integer orderID;
    private Integer supplierID;
    private boolean delivered;
    private LocalDate supplyDate;
    private double price;

    public OrderDTO(Integer orderID, Integer supplierID, boolean delivered, LocalDate supplyDate, double price) {
        this.orderID = orderID;
        this.supplierID = supplierID;
        this.delivered = delivered;
        this.supplyDate = supplyDate;
        this.price = price;
    }

    public Integer getOrderID() {
        return orderID;
    }

    public Integer getSupplierID() {
        return supplierID;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public LocalDate getSupplyDate() {
        return supplyDate;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "orderID: '" + this.orderID + '\'' +
                ", supplierID: '" + this.supplierID + '\'' +
                ", delivered: '" + this.delivered + '\'' +
                ", supplyDate: '" + this.supplierID + '\'' +
                ", price: '" + this.price +
                '}';
    }
}
