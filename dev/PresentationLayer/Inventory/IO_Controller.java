package PresentationLayer.Inventory;

import BusinessLayer.FacadeController;
import BusinessLayer.Inventory.FacadeInv;
import BusinessLayer.Inventory.FaultyItem;
import BusinessLayer.ResponseT;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class IO_Controller {
    private FacadeInv invCtrl;
    private INV_IO io;
    private FacadeController facadeController;

    public IO_Controller() {
        invCtrl = FacadeInv.getInstance();
        facadeController = FacadeController.getInstance();
    }

    public void mainMenu(int action) {
        io = INV_IO.getInstance();
        //add sale
        if (action == 1){ accRejOrder(); }
        //add faulty
        if (action == 2){ io.reportMenu(); }
        //add item
        if (action == 3){ io.stockMenu(); }
    }

    public void stockMenu(int action) {
        if (action == 1) { addSale(); }

        if (action == 2) { addFaulty(); }

        if (action == 3) { addItem(); }

        if ( action == 4) { addCategory(); }

        if( action == 5) { io.editMenu(); }
    }

    public void accRejOrder() {
        HashMap<Integer, Integer> delivery = facadeController.facadeDelivery.getOrder();
        if(delivery != null) {
            io.print(delivery.toString());
        } else {
            io.print("No such order");
            return;
        }
        String accRej = io.getString("Do you accept or reject order (accept = 'y', reject = 'n'");
        Map<Integer, Integer> faultyItems = new HashMap<>();
        if (accRej.equals("y")) {
            do {
                int itemId = io.getInt("Enter Item ID or -1 if there are no more faulty items");
                if (itemId == -1) {
                    break;
                }
                int amount = io.getInt("Enter Amount");
                faultyItems.put(itemId, amount);
            } while (true);
            facadeController.acceptOrder(delivery, faultyItems);
        } else if (accRej.equals("n")) {
            facadeController.rejectOrder();
        } else {
            io.print("please enter y/n");
        }
    }

    public void reportsMenu(int action) {
        if (action == 1) { invReport(); }

        if ( action == 2) { faultyReport(); }

        if( action == 3) { catReport(); }
    }

    public void editMenu(int action) {

        if(action == 1) { discountItem(); }

        if(action == 2) { discountCategory(); }

        if(action == 3) { manuDiscount(); }

        if(action == 4) { removeItem(); }

        if(action == 5) { changeShelf(); }

        if(action == 6) { moveToShelf(); }
    }

    private void badInput(String msg){
        io.badInput("The input you have entered is invalid\n" + msg);
    }

    private void addSale() {
        try {
            int id = io.getInt("Enter item ID:");
            int amount = io.getInt("Enter amount: ");
            if (id < 0 || amount < 0) {
                throw new RuntimeException("id and amount most be larger than 0");
            }
            invCtrl.addSale(id, amount);
            io.print("Sale added");
        } catch (RuntimeException err) {
            io.print(err.getMessage());
        }
    }

    private void addFaulty() {
        try {
            int itemId = io.getInt("Enter item ID:");
            int opt = io.getInt("Where was the faulty item found (1 - shelf , 2 - storage)");
            int amountOfFaulty = io.getInt("How many items are faulty");
            if((opt != 1 && opt != 2) || itemId < 0 || amountOfFaulty < 0) {
                throw new RuntimeException("one of the inputs you have entered is invalid");
            }
            invCtrl.addFaulty(itemId, opt, amountOfFaulty);
            io.print("Faulty item added");
        } catch (RuntimeException err) {
            io.print(err.getMessage());
        }
    }

    private void addCategory() {
        try {
            String catName = io.getString("Enter catagory name");
            String subCatOf = io.getString("Enter the name of the catagory above" + catName +": (enter 0 if there isn't one)");
            if (subCatOf.equals("0")) {
                invCtrl.addCategory(catName);
            }
            else {
                invCtrl.addSubCategory(catName, subCatOf);
            }
            io.print("Category added");
        } catch (RuntimeException err) {
            io.print(err.getMessage());
        }
    }

    private void addItem() {
        try {
            int id = io.getInt("Enter item ID:");
            String name = io.getString("Enter item Name:");
            double price = io.getDouble("Enter item price:");
            double cost = io.getDouble("Enter item cost:");
            int shelf = io.getInt("Enter item shelf");
            String man = io.getString("Enter item manufacturer:");
            int shQuant = io.getInt("Enter amount on shelf");
            int stQuant = io.getInt("Enter amount in storage");
            String catName = io.getString("Enter item category:");
            int min = io.getInt("Enter minimum amount left before getting alert");
            if (id < 0 || price < 0 || cost < 0 || shQuant < 0 || stQuant < 0 || shelf < 0 || min < 0) {
                throw new RuntimeException("failed - the numbers you entered should all be positive");
            }
            invCtrl.addItem(id, name, price, cost, shelf, man, shQuant, stQuant, min, catName);
            io.print("Item added");
        } catch (RuntimeException err) {
            io.print(err.getMessage());
        }
    }

    private void discountItem() {
        try {
            LocalDate start = io.getDate("Enter discount starting date");
            LocalDate end = io.getDate("Enter discount end date");
            int dis = io.getInt("Enter the amount of discount");
            int itemId = io.getInt("Enter Item ID");
            if (itemId < 0 || dis < 0 || dis > 100) {
                throw new RuntimeException("failed - illegal id or discount");
            }
            invCtrl.addItemDiscount(start, end, dis, itemId);
            io.print("Discount added");
        } catch (RuntimeException err) {
            io.print(err.getMessage());
        }
    }

    private void faultyReport() {
        try {
            LocalDate date = io.getDate("Enter faulty report starting date");
            io.print(invCtrl.getFaultyReport(date));
        } catch (RuntimeException err) {
            io.print(err.getMessage());
        }
    }

    private void discountCategory() {
        try {
            LocalDate start = io.getDate("Enter discount starting date");
            LocalDate end = io.getDate("Enter discount end date");
            int dis = io.getInt("Enter the amount of discount");
            if(dis < 0 || dis > 100) {
                throw new RuntimeException("discount should be between 0 - 100");
            }
            String category  = io.getString("Enter Category name");
            invCtrl.addCategoryDiscount(start, end, dis, category);
        } catch (RuntimeException err) {
            io.print(err.getMessage());
        }

    }

    private void addToStorage() {
        try {
            int itemID = io.getInt("Enter item id");
            int amountToAdd = io.getInt("Enter amount to add");
            if (itemID < 0 || amountToAdd < 0) {
                throw new RuntimeException("item id and amount should be positive");
            }
            invCtrl.addToStorage(itemID, amountToAdd);
            io.print("Added to storage");
        } catch (RuntimeException err) {
            io.print(err.getMessage());
        }
    }

    private void manuDiscount() {
        try {
            LocalDate start = io.getDate("Enter discount starting date");
            LocalDate end = io.getDate("Enter discount end date");
            int dis = io.getInt("Enter the amount of discount");
            int itemId = io.getInt("Enter Item ID");
            if (itemId < 0 || dis < 0 || dis > 100) {
                throw new RuntimeException("failed - illegal id or discount");
            }
            invCtrl.addManuDiscount(start, end, dis, itemId);
            io.print("Discount added");
        } catch (RuntimeException err) {
            io.print(err.getMessage());
        }
    }

    private void changeShelf() {
        try {
            int itemId = io.getInt("Enter Item ID");
            int newShelf = io.getInt("Enter the new shelf for the item");
            if(itemId < 0 || newShelf < 0) {
                throw new RuntimeException("id and shelf should be positive numbers");
            }
            invCtrl.changeShelf(itemId, newShelf);
            io.print("Changed Shelf");
        } catch (RuntimeException err) {
            io.print(err.getMessage());
        }
    }

    private void moveToShelf(){
        try {
            int itemId = io.getInt("Enter Item ID");
            int amountToMove = io.getInt("Enter the amount of items to move");
            if (itemId < 0 || amountToMove < 0) {
                throw new RuntimeException("item id and amount should be positive");
            }
            invCtrl.moveToShelf(itemId, amountToMove);
            io.print("Items moved to shelf");
        } catch (RuntimeException err) {
            io.print(err.getMessage());
        }
    }

    private void removeItem() {
        try {
            int itemId = io.getInt("Enter Item ID");
            if (itemId < 0) {
                throw new RuntimeException("item id should be positive");
            }
            invCtrl.removeItem(itemId);
            io.print("Item removed");
        } catch (RuntimeException err) {
            io.print(err.getMessage());
        }
    }

    public void catReport() {
        try {
            List<String> cats = io.getList("Enter the names of the categories to view");
            ResponseT<HashMap<Integer, Integer>> reportResponse = invCtrl.catReport(cats);
            ResponseT<StringBuilder> orderResponse = facadeController.ordersByLack(reportResponse.value);
            io.print(reportResponse.ErrorMessage);
            if (!orderResponse.ErrorOccured())
                io.print(orderResponse.value.toString());
            else
                io.print(orderResponse.ErrorMessage);
        } catch (RuntimeException err) {
            io.print(err.getMessage());
        }
    }

    private void invReport() {
        try {
            ResponseT<HashMap<Integer, Integer>> reportResponse = invCtrl.stkReport();
            ResponseT<StringBuilder> orderResponse = facadeController.ordersByLack(reportResponse.value);
            io.print(reportResponse.ErrorMessage);
            if (!orderResponse.ErrorOccured())
                io.print(orderResponse.value.toString());
            else
                io.print(orderResponse.ErrorMessage);
        } catch (RuntimeException e) {
            io.print(e.getMessage());
        }

    }
}
