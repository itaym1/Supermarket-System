package DataAccessLayer.Supplier_Inv.DTO;

import BusinessLayer.Inventory.Item;

public class ItemDTO {
    private int id;
    private String name;
    private Double price;
    private Double cost;
    private int shelfNum;
    private String manufacturer;
    private int shelfQuantity;
    private int storageQuantity;
    private int minAlert;


    public ItemDTO(Item blItem) {
        this.id = blItem.getId();
        this.name = blItem.getName();
        this.price = blItem.getPrice();
        this.cost = blItem.getCost();
        this.shelfNum = blItem.getShelfNum();
        this.manufacturer = blItem.getManufacturer();
        this.shelfQuantity = blItem.getShelfQuantity();
        this.storageQuantity = blItem.getStorageQuantity();
        this.minAlert = blItem.getMinAlert();
    }

    public ItemDTO(int id, String name, double price, double cost, int shelfNum, String manufacturer, int shelfQuantity, int storageQuantity, int minAlert) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.cost = cost;
        this.shelfNum = shelfNum;
        this.manufacturer = manufacturer;
        this.shelfQuantity = shelfQuantity;
        this.storageQuantity = storageQuantity;
        this.minAlert = minAlert;
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

    public int getShelfQuantity() {
        return shelfQuantity;
    }

    public int getStorageQuantity() {
        return storageQuantity;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public int getShelfNum() {
        return shelfNum;
    }

    public int getMinAlert() {
        return minAlert;
    }

    public Double getCost() {
        return cost;
    }

    public String toString(String tabs) {
        String s = tabs + "Id: " + id +
                "\n" + tabs + "Name: " + name +
                "\n" + tabs + "Shelf Num: " + shelfNum +
                "\n" + tabs + "Manufacturer: " + manufacturer +
                "\n" + tabs + "Quantity: " + (shelfQuantity + storageQuantity) +
                "\n" + tabs + "Shelf Quantity: " + shelfQuantity +
                "\n" + tabs + "Storage Quantity: " + storageQuantity + "\n";
        if (shelfQuantity+storageQuantity <= minAlert)
            s += tabs + "Item is below the minimum that was set by: " + (minAlert-(shelfQuantity+storageQuantity)) + "\n";
        return s;
    }

}
