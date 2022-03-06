package DataAccessLayer.Supplier_Inv.DTO;

public class ProductsInPeriodicDTO {

    private Integer periodicID;
    private Integer productID;
    private Integer quantity;

    public ProductsInPeriodicDTO(Integer periodicID, Integer productID, Integer quantity) {
        this.periodicID = periodicID;
        this.productID = productID;
        this.quantity = quantity;
    }

    public Integer getPeriodicID() {
        return periodicID;
    }

    public Integer getProductID() {
        return productID;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
