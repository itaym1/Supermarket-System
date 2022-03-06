package BusinessLayer.Inventory;

import BusinessLayer.ResponseT;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Category {
    private String name;
    private List<Category> subCategories;
    private List<Item> items;

    public Category(String name) {
        this.name = name;
        subCategories = new LinkedList<>();
        items = new LinkedList<>();
    }

    public String getName() {
        return name;
    }

    public void addItem(int id, String name, double price, double cost, int shelfNum, String manufacturer, int shelfQuantity, int storageQuantity, int minAlert) {
        items.add(new Item(id, name, price, cost, shelfNum, manufacturer, shelfQuantity, storageQuantity, minAlert));
    }

    public void addItem(Item i) {
        items.add(i);
    }

    public void addItemDiscount(LocalDate start, LocalDate end, int discountPr, int id) {
        Item i = getItem(id);
        i.addPriceDiscount(new Discount(start, end, discountPr));
    }

    public void addItemDiscount(Discount d, int id) {
        Item i = getItem(id);
        i.addPriceDiscount(d);
    }

    public boolean addToStorage(int id, int amount) {
        Item i = getItem(id);
        if (i == null)
            return false;
        return i.addToStorage(amount);
    }

    public boolean moveToShelf(int id, int amount) {
        Item i = getItem(id);
        if (i == null)
            return false;
        return i.moveToShelf(amount);
    }

    public boolean changeShelf(int id, int shelf) {
        Item i = getItem(id);
        if (i == null)
            return false;
        return i.changeShelf(shelf);
    }

    public ResponseT<List<Item>> addCategoryDiscount(LocalDate start, LocalDate end, int discountPr) {
        Discount d = new Discount(start, end, discountPr);
        for(Item i : items) {
            i.addPriceDiscount(d);
        }
        return new ResponseT<>(items);
    }

    public ResponseT<List<Item>> addCategoryDiscount(Discount d) {
        for(Item i : items) {
            i.addPriceDiscount(d);
        }
        return new ResponseT<>(items);
    }

    public void addManuDiscount(LocalDate start, LocalDate end, int discountPr, int id) {
        Item i = getItem(id);
        if (i == null)
            return;
        i.addCostDiscount(new Discount(start, end, discountPr));
    }

    public void addManuDiscount(Discount d, int id) {
        Item i = getItem(id);
        if (i == null)
            return;
        i.addCostDiscount(d);
    }

    public void addSubCategory(Category sub) {
        subCategories.add(sub);
    }

    public Item getItem(int id) {
        for (Item i:items) {
            if (i.getId() == id)
                return i;
        }
        for (Category c : subCategories) {
            Item i = c.getItem(id);
            if (i != null)
                return i;
        }
        return null;
    }

    public Category getCategory(String name) {
        if (this.name.equals(name))
            return this;
        for (Category c: subCategories) {
            Category curr = c.getCategory(name);
            if (curr != null)
                return curr;
        }
        return null;
    }

    public Category getCategory(int id) {
        for(Item i : items) {
            if(i.getId() == id) {
                return this;
            }
        }
        for (Category c : subCategories) {
            if (c.getCategory(id) != null)
                return c;
        }
        return null;
    }

    public String toString(String tabs) {
        StringBuilder output = new StringBuilder("\n" + tabs + "Category: " + name + "\n");
        for (Item i : items) {
            output.append(i.toString(tabs + "\t")+"\n");
        }
        for (Category c : subCategories) {
            output.append(c.toString(tabs+"\t"));
        }
        return output.toString()+"\n";
    }

    public String toStringNameID() {
        StringBuilder output = new StringBuilder("\n" + "Category: " + name + "\n");
        for (Item i : items) {
            output.append(i.toString( "\t")+"\n");
        }
        for (Category c : subCategories) {
            output.append(c.toString("\t"));
        }
        return output.toString()+"\n";
    }

    @Override
    public String toString() {
        return toString("");
    }

    public Item removeItem(int id) {
        Item i = getItem(id);
        System.out.println(i.toString(""));
        if (i == null)
            return null;
        if(!items.remove(i)) {
            return i;
        };
        return i;
    }

    public boolean removeFromShelf(int id, int amount) {
        Item i = getItem(id);
        if (i==null)
            return false;
        return i.removeFromShelf(amount);
    }

    public boolean removeFromStorage(int id, int amount) {
        Item i = getItem(id);
        if (i == null)
            return false;
        return i.removeFromStorage(amount);
    }

    public HashMap<Integer,Integer> getLackItems() {
        HashMap<Integer, Integer> itemAmounts = new HashMap<>();
        for (Item i : items) {
            if (i.belowMin()) {
                itemAmounts.put(i.getId(), i.checkMinAmount(0));
            }
        }
        for (Category c : subCategories) {
            itemAmounts.putAll(c.getLackItems());
        }
        return itemAmounts;
    }

}