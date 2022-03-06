package BusinessLayer.Supplier;

import java.time.LocalDate;
import java.util.HashMap;

public class Order {
    private int orderID;
    private int supplierID;
    private boolean delivered;
    private HashMap<Integer, Integer> products; // <productID: Integer, quantity: Integer>
    private double price;
    private LocalDate date;

    public Order(int orderID, int supplierID, boolean delivered, HashMap<Integer, Integer> products) {
        this.orderID = orderID;
        this.supplierID = supplierID;
        this.delivered = delivered;
        this.products = products;
        this.price = 0;
        this.date = LocalDate.now();
    }

    public String toString() {
        return '\n' + "Order ID: " + orderID + "\tSupplier ID: " + supplierID + "\tDate: " + date;
    }

    public int getOrderID() {
        return orderID;
    }

    public int getSupplierID() {
        return supplierID;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public double getPrice() {
        return price;
    }

    public LocalDate getDate() {
        return date;
    }

    public String showAllDetails() {
        String delivered = "No";
        if (this.delivered)
            delivered = "Yes";
        String prods = "";
        for (Integer i : products.keySet()) {
            prods += '\n' + "Product ID: " + i + " , Quantity: " + products.get(i);
        }
        return '\n' + "Order ID: " + orderID + ",  Supplier ID: " + supplierID + ",  Delivered: " + delivered + ",  Date: " + date + '\n'
                + "Products:" + prods + '\n' + "Price: " + price;
    }


    public void setPrice(double price) {
        this.price = price;
    }

    public HashMap<Integer, Integer> getProducts() {
        return products;
    }
}
