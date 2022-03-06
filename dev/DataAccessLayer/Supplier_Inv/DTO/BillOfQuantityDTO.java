package DataAccessLayer.Supplier_Inv.DTO;

public class BillOfQuantityDTO {

    private Integer supplierID;
    private Integer productID;
    private Integer minQuantity;
    private Integer percentDiscount;

    public BillOfQuantityDTO(Integer ID, Integer productID, Integer minQuantity, Integer percentDiscount) {
        this.supplierID = ID;
        this.productID = productID;
        this.minQuantity = minQuantity;
        this.percentDiscount = percentDiscount;
    }

    public Integer getSupplierID() {
        return supplierID;
    }

    public Integer getProductID() { return productID; }

    public Integer getMinQuantity() {
        return minQuantity;
    }

    public Integer getPercentDiscount() {
        return percentDiscount;
    }

    @Override
    public String toString() {
        return "BillOfQuantityDTO{" +
                "supplierID: '" + this.supplierID + '\'' +
                "productID: '" + this.productID + '\'' +
                ", minimum quantity: '" + this.minQuantity + '\'' +
                ", percent discount: '" + this.percentDiscount + '\'' +
                '}';
    }
}
