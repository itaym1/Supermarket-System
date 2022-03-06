package BusinessLayer.Supplier;

public class Product {
    private int productID;
    private int supplierID;
    private String name;
    private String category;
    private double price;
    private int pidSuperLee;

    public Product(int productID, int supplierID, String name, String category, double price,int pidSuperLee){
        this.productID = productID;
        this.supplierID = supplierID;
        this.name = name;
        this.category = category;
        this.price = price;
        this.pidSuperLee = pidSuperLee;
    }

    public int getSupplierID() {
        return supplierID;
    }

    public String toString(){
        return "product ID: " + productID + ",  product Name: " + name + ",  Price: " + price;
    }

    public double getPrice() {
        return price;
    }

    public int getProductID() {
        return productID;
    }

    public int getPidSuperLee() {
        return pidSuperLee;
    }
}
