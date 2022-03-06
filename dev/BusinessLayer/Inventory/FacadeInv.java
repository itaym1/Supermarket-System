package BusinessLayer.Inventory;

import BusinessLayer.ResponseT;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

/**
 * This class is singleton
 */

public class FacadeInv {
    private RecordController recCtrl;
    private StockController stockCtrl;

    private static FacadeInv facadeInv = null;

    private FacadeInv() {
        recCtrl = new RecordController();
        stockCtrl = new StockController();
    }

    public static FacadeInv getInstance() {
        if (facadeInv == null)
            facadeInv = new FacadeInv();

        return facadeInv;
    }

    //adds a sale to the records, removes one item from shelf
    public boolean addSale(int id, int amount) {
        stockCtrl.removeFromShelf(id, amount);
        Item item = stockCtrl.getItem(id);
        return recCtrl.addSale(item.getId(), item.getCostWithDis(), item.getPriceWithDis(), amount);
    }

    //adds a sale to the records, removes one item from shelf if opt is 1 or from storage if opt is 2
    public boolean addFaulty(int id, int opt, int amountOfFaulty) {
        if(opt == 1) {
            stockCtrl.removeFromShelf(id, amountOfFaulty);
        } else
            stockCtrl.removeFromStorage(id, amountOfFaulty);
        return recCtrl.addFaulty(id, LocalDate.now() ,amountOfFaulty);
    }

    //adds a new Item to the System
    public void addItem(int id, String name, double price, double cost, int shelf, String man, int shQuan, int stQuan, int minAlert, String catName) {
        stockCtrl.addItem(id,name,price,cost,shelf,man,shQuan,stQuan, minAlert, catName);
    }

    //adds a category which is a sub category of `superName`
    public void addSubCategory(String name, String superName){
        stockCtrl.addSubCategory(name, superName);
    }

    //adds a category which is not a sub category of any
    public void addCategory(String name) {
        stockCtrl.addCategory(name);
    }

    //returns a report of all the faulty items that the System found or was reported to it
    public String getFaultyReport(LocalDate startDate) {
        return recCtrl.faultyReport(startDate);
    }

    //adds a discount for an item between the `start` and `end` date
    public void addItemDiscount(LocalDate start, LocalDate end, int dis, int itemID) {
        stockCtrl.addItemDiscount(start, end, dis, itemID);
    }

    //adds a discount for all the items in a category between the `start` and `end` date
    public void addCategoryDiscount(LocalDate start, LocalDate end, int dis, String category) {
        stockCtrl.addCategoryDiscount(start, end, dis, category);
    }

    //adds a new supply for an item to the storage
    public void addToStorage(int itemID, int amountToAdd) {
        stockCtrl.addToStorage(itemID, amountToAdd);
    }

    //adds a manufacturer discount for an item
    public void addManuDiscount(LocalDate start, LocalDate end, int dis, int itemId) {
        stockCtrl.addManuDiscount(start, end, dis, itemId);
    }

    //changes an item shelf
    public void changeShelf(int itemId, int newShelf) {
        stockCtrl.changeShelf(itemId, newShelf);
    }

    //moves `amountToMove` items from storage to shelf
    public void moveToShelf(int itemId, int amountToMove) {
        stockCtrl.moveToShelf(itemId, amountToMove);
    }

    //returns a report of all items in stock
    public ResponseT<HashMap<Integer, Integer>> stkReport() {
        return stockCtrl.stkReport();
    }

    public String showAllItemsInSuper() {
        return stockCtrl.showAllItemsInSuper();
    }

    public ResponseT<HashMap<Integer, Integer>> catReport(List<String> catNames) {
        return stockCtrl.catReport(catNames);
    }

    //removes an item from the system
    public void removeItem(int itemId) {
        stockCtrl.removeItem(itemId);
    }

    public int QuantityBiggerThenInvMin(int quantity, int productID) {
        return stockCtrl.QuantityBiggerThenInvMin(quantity,productID);
    }
}
