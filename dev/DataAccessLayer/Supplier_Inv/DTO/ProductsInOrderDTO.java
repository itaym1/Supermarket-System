package DataAccessLayer.Supplier_Inv.DTO;

public class ProductsInOrderDTO {

    private Integer orderID;
    private Integer productID;
    private Integer quantity;
    private Integer supplierID;

    public ProductsInOrderDTO(Integer orderID, Integer productID, Integer quantity, Integer supplierID) {
        this.orderID = orderID;
        this.productID = productID;
        this.quantity = quantity;
        this.supplierID = supplierID;
    }

    public Integer getOrderID() {
        return orderID;
    }

    public Integer getProductID() {
        return productID;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getSupplierID() {
        return supplierID;
    }

    @Override
    public String toString() {
        return "ProductsInOrderDTO{" +
                "orderID: '" + this.orderID + '\'' +
                ", productID: '" + this.productID + '\'' +
                ", quantity: '" + this.quantity + '\'' +
                ", supplierID: '" + this.supplierID + '\'' +
                '}';
    }
}
