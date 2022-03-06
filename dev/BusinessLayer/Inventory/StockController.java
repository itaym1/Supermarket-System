package BusinessLayer.Inventory;

import BusinessLayer.ResponseT;
import DataAccessLayer.Supplier_Inv.Mapper;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class StockController {
    private List<Category> categories;
    private Mapper mapper;

    public StockController() {
        mapper = Mapper.getInstance();
        ResponseT<List<Category>> catRes = mapper.loadCategory();
        if (catRes.ErrorOccured() || catRes.value == null) {
            categories = new LinkedList<>();
        } else {
            categories = catRes.value;
        }
    }

    private Category getCategory(String name) {
        for (Category c : categories) {
            Category curr = c.getCategory(name);
            if (curr != null && curr.getCategory(name) != null)
                return curr;
        }
        return null;
    }

    public void addSubCategory(String name, String superName) {
        Category superCategory = getCategory(superName);
        if (superCategory == null)
            throw new RuntimeException("Cannot find Category: "+superName);
        if( getCategory(name) != null)
            throw new RuntimeException("Category "+name+" already exists");
        Category cat = new Category(name);
        superCategory.addSubCategory(cat);
        mapper.addSubCategory(cat, superName);
    }

    public void addCategory(String name) {
        if (categories.contains(name))
            throw new RuntimeException("Category "+name+" already exists");
        Category cat = new Category(name);
        categories.add(cat);
        mapper.addCategory(cat);
    }

    public Item getItem(int id) {
        for (Category c : categories) {
            Item i = c.getItem(id);
            if (i != null)
                return i;
        }
        return null;
    }

    public void addItem(int id, String name, double price, double cost, int shelfNum, String manufacturer, int shelfQuantity, int storageQuantity, int minAlert, String catName) {
        Category c = getCategory(catName);
        if (c == null)
            throw new RuntimeException("Cannot find Category: "+catName);
        if (getItem(id) != null)
            throw new RuntimeException("Item id "+id+"+ already exists");
        Item i = new Item(id, name, price, cost, shelfNum, manufacturer, shelfQuantity, storageQuantity, minAlert);
        c.addItem(i);
        if(mapper.addItem(i, c).ErrorOccured()) {

        }
    }

    public void removeItem(int id) {
        Category c = getCategory(id);
        if (c == null)
            throw new RuntimeException("Cannot find Item id: "+id);
        Item toRemove = c.removeItem(id);
        mapper.deleteItem(toRemove, c);
    }

    public void removeFromShelf(int id, int amount) {
        Category c = getCategory(id);
        if (c == null)
            throw new RuntimeException("Cannot find Item id: "+id);
        if(!c.removeFromShelf(id, amount))
            throw new RuntimeException("Amount is too large");
        mapper.updateItem(getItem(id));
    }

    public void removeFromStorage(int id, int amount) {
        Category c = getCategory(id);
        if (c == null)
            throw new RuntimeException("Cannot find Item id: "+id);
        c.removeFromStorage(id, amount);
        mapper.updateItem(getItem(id));
    }

    public Category getCategory(int id) {
        for (Category c : categories) {
            Category output = c.getCategory(id);
            if (output != null)
                return output;
        }
        return null;
    }

    public void addToStorage(int id, int amount) {
        Category c = getCategory(id);
        if (c == null)
            throw new RuntimeException("Cannot find Item id: "+id);
        c.addToStorage(id, amount);
        mapper.updateItem(getItem(id));
    }

    public void moveToShelf(int id, int amount) {
        Category c = getCategory(id);
        if (c == null)
            throw new RuntimeException("Cannot find Item id: "+id);
        if (!c.moveToShelf(id, amount))
            throw new RuntimeException("Amount is too large");
        mapper.updateItem(getItem(id));
    }

    public void changeShelf(int id, int shelf) {
        Category c = getCategory(id);
        if (c == null)
            throw new RuntimeException("Cannot find Item id: "+id);
        c.changeShelf(id, shelf);
        mapper.updateItem(getItem(id));
    }

    public void addItemDiscount(LocalDate start, LocalDate end, int discountPr, int id) {
        Category c = getCategory(id);
        if (c == null)
            throw new RuntimeException("Cannot find Item id: "+id);
        Discount d = new Discount(start, end, discountPr);
        c.addItemDiscount(d, id);
        mapper.addPriceDiscount(d, id);
    }

    public void addCategoryDiscount(LocalDate start, LocalDate end, int discountPr, String catName) {
        Category c = getCategory(catName);
        if (c == null)
            throw new RuntimeException("Cannot find Category: "+catName);
        Discount d = new Discount(start, end, discountPr);
        ResponseT<List<Item>> resItem = c.addCategoryDiscount(d);
        for(Item i : resItem.value) {
            mapper.addPriceDiscount(d, i.getId());
        }
    }

    public void addManuDiscount(LocalDate start, LocalDate end, int discountPr, int id) {
        Category c = getCategory(id);
        if (c == null)
            throw new RuntimeException("Cannot find Item id: "+id);
        Discount d = new Discount(start, end, discountPr);
        c.addManuDiscount(d, id);
        mapper.addCostDiscount(d, id);
    }

    public ResponseT<HashMap<Integer, Integer>> stkReport() {
        StringBuilder sb = new StringBuilder("\n");
        HashMap<Integer, Integer> lackProducts = new HashMap<>();
        for (Category c : categories) {
            sb.append(c.toString());
            lackProducts.putAll(c.getLackItems());
        }

        return new ResponseT<>(lackProducts, sb.toString());
    }

    public String showAllItemsInSuper() {
        StringBuilder sb = new StringBuilder("\n");
        for (Category c : categories) {
            sb.append(c.toStringNameID());
        }
        return sb.toString();
    }

    public ResponseT<HashMap<Integer, Integer>> catReport(List<String> catNames) {
        StringBuilder sb = new StringBuilder("\nReport of categories: "+catNames.toString()+"\n");
        HashMap<Integer, Integer> lackProducts = new HashMap<>();
        for (String cat : catNames) {
            Category c = getCategory(cat);
            if (c == null)
                sb.append("Could not found category: "+cat+"\n");
            else {
                lackProducts.putAll(c.getLackItems());
                sb.append(c.toString() + "\n");
            }
        }
        return new ResponseT<>(lackProducts ,sb.toString());
    }

    public int QuantityBiggerThenInvMin(int quantity, int productID) {
        Item item = getItem(productID);
        if(item != null){
            return item.checkMinAmount(quantity);
        }
        return -1;
    }
}
