package BusinessLayer.Inventory;

import DataAccessLayer.Supplier_Inv.DTO.ItemDTO;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class Item {
    private int id;
    //added price, expDate and cost that were not in the UML
    private String name;
    private Double price;
    private Double cost;
    private int shelfNum;
    private String manufacturer;
    private int shelfQuantity;
    private int storageQuantity;
    private List<Discount> priceDiscounts;
    private List<Discount> costDiscounts;
    private int minAlert;

    public Item(ItemDTO dbItem) {
        id = dbItem.getId();
        name = dbItem.getName();
        price = dbItem.getPrice();
        cost = dbItem.getCost();
        shelfNum = dbItem.getShelfNum();
        manufacturer = dbItem.getManufacturer();
        shelfQuantity = dbItem.getShelfQuantity();
        storageQuantity = dbItem.getStorageQuantity();
        minAlert = dbItem.getMinAlert();
        priceDiscounts = new LinkedList<>();
        costDiscounts = new LinkedList<>();
    }

    public Item(int id, String name, double price, double cost, int shelfNum, String manufacturer, int shelfQuantity, int storageQuantity, int minAlert) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.cost = cost;
        this.shelfNum = shelfNum;
        this.manufacturer = manufacturer;
        this.shelfQuantity = shelfQuantity;
        this.storageQuantity = storageQuantity;
        priceDiscounts = new LinkedList<>();
        costDiscounts = new LinkedList<>();
        this.minAlert = minAlert;
    }

    public boolean addToStorage(int amount) {
        storageQuantity += amount;
        return true;
    }

    public boolean removeFromShelf(int amount) {
        if (amount > shelfQuantity)
            return false;
        shelfQuantity -= amount;
        return true;
    }

    public int getShelfNum() {
        return shelfNum;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public int getStorageQuantity() {
        return storageQuantity;
    }

    public int getShelfQuantity() {
        return shelfQuantity;
    }

    public List<Discount> getPriceDiscounts() {
        return priceDiscounts;
    }

    public List<Discount> getCostDiscounts() {
        return costDiscounts;
    }

    public int getMinAlert() {
        return minAlert;
    }

    public boolean removeFromStorage(int amount) {
        if (amount > storageQuantity)
            return false;
        storageQuantity -= amount;
        return true;
    }

    public boolean moveToShelf(int amount) {
        if (amount > storageQuantity) {
            return false;
        }
        storageQuantity -= amount;
        shelfQuantity += amount;
        return true;
    }

    public boolean changeShelf(int newShelf) {
        shelfNum = newShelf;
        return true;
    }

    public void addPriceDiscount(Discount d) {
        priceDiscounts.add(d);
    }

    public void addCostDiscount(Discount d) {
        costDiscounts.add(d);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public Double getCostWithDis() {
        double currCost = cost;
        for(Discount dis : costDiscounts) {
            LocalDate now = LocalDate.now();
            if (now.isAfter(dis.getStart()) && now.isBefore(dis.getEnd()))
                currCost -= currCost*dis.getDiscountPr()/100;
        }
        return currCost;
    }

    public Double getPriceWithDis() {
        double currPrice = price;
        for(Discount dis : priceDiscounts) {
            LocalDate now = LocalDate.now();
            if (now.isAfter(dis.getStart()) && now.isBefore(dis.getEnd()))
                currPrice -= currPrice*dis.getDiscountPr()/100;
        }
        return currPrice;
    }

    public int checkMinAmount(int amount) { // check if amount+quantity >= 2*minAlert
        if((getShelfQuantity() + getStorageQuantity() + amount) >= getMinAlert()*2)
            return amount;
        return (getMinAlert()*2)-(getShelfQuantity() + getStorageQuantity());
    }

    public Double getCost() {
        return cost;
    }

    public boolean belowMin() {
        return shelfQuantity+storageQuantity <= minAlert;
    }

    public String toString(String tabs) {
        String s = tabs + "Id: " + id +
                "\n" + tabs + "Name: " + name +
                "\n" + tabs + "Shelf Num: " + shelfNum +
                "\n" + tabs + "Manufacturer: " + manufacturer +
                "\n" + tabs + "Quantity: " + (shelfQuantity + storageQuantity) +
                "\n" + tabs + "Shelf Quantity: " + shelfQuantity +
                "\n" + tabs + "Storage Quantity: " + storageQuantity + "\n";
        if (belowMin())
            s += tabs + "Item is below the minimum that was set by: " + (minAlert-(shelfQuantity+storageQuantity)) + "\n";
        return s;
    }

    public String toStringNameID() {
        String s =  "Id: " + id +
                "\n"  + "Name: " + name;
        return s;
    }


}
