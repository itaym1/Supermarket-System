package DataAccessLayer.Supplier_Inv.DTO;

import BusinessLayer.Inventory.Sale;

import java.time.LocalDateTime;

public class SaleDTO {
    private int itemID;
    private LocalDateTime date;
    private double price;
    private double cost;
    private int amount;

    public SaleDTO(Sale s) {
        this.itemID = s.getItemId();
        this.date = s.getSaleDate();
        this.price = s.getSalePrice();
        this.cost = s.getItemCost();
        this.amount = s.getAmount();
    }

    public SaleDTO(int itemID, LocalDateTime date, double price, double cost, int amount) {
        this.itemID = itemID;
        this.date = date;
        this.price = price;
        this.cost = cost;
        this.amount = amount;
    }

    public int getAmount() { return amount; }

    public int getItemID() {
        return itemID;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public double getPrice() {
        return price;
    }

    public double getCost() {
        return cost;
    }
}
