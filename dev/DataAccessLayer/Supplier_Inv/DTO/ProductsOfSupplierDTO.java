package DataAccessLayer.Supplier_Inv.DTO;

public class ProductsOfSupplierDTO {

    private Integer productID;
    private Integer supplierID;
    private String name;
    private String category;
    private double price;
    private int pidSuperLee;

    public ProductsOfSupplierDTO(Integer productID, Integer supplierID, String name, String category, double price, int pidSuperLee) {
        this.productID = productID;
        this.supplierID = supplierID;
        this.name = name;
        this.category = category;
        this.price = price;
        this.pidSuperLee = pidSuperLee;
    }

    public Integer getProductID() {
        return productID;
    }

    public Integer getSupplierID() {
        return supplierID;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public int getPidSuperLee() {
        return pidSuperLee;
    }

    @Override
    public String toString() {
        return "ProductsOfSupplierDTO{" +
                "productID: '" + this.productID + '\'' +
                ", supplierID: '" + this.supplierID + '\'' +
                ", name: '" + this.name + '\'' +
                ", category: '" + this.category + '\'' +
                ", price: '" + this.price +
                '}';
    }
}
