package BusinessLayer.Inventory;

import java.time.LocalDateTime;

public class Sale {
    private LocalDateTime saleDate;
    private Double itemCost;
    private Double salePrice;
    private int itemId;
    private int amount;

    public Sale(int itemId, double itemCost, double salePrice, int amount) {
        this.itemId = itemId;
        this.itemCost = itemCost;
        this.salePrice = salePrice;
        this.saleDate = LocalDateTime.now();
        this.amount = amount;
    }

    public Sale(int itemId, double itemCost, double salePrice, LocalDateTime date,int amount) {
        this.itemId = itemId;
        this.itemCost = itemCost;
        this.salePrice = salePrice;
        this.saleDate = date;
        this.amount = amount;
    }

    public int getAmount() { return amount; }

    public LocalDateTime getSaleDate() {
        return saleDate;
    }

    public Double getItemCost() {
        return itemCost;
    }

    public Double getSalePrice() {
        return salePrice;
    }

    public int getItemId() {
        return itemId;
    }

    public String toString() {
        String str = "Item ID:\t" + itemId + "\n" +
                "Sale date:\t" + saleDate + "\n" +
                "Item cost:\t" + itemCost + "\n" +
                "Sale Price:\t" + salePrice + "\n" +
                "Amount:\t" + amount;

        return str;
    }

}
