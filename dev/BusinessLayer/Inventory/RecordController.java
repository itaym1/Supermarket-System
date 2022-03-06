package BusinessLayer.Inventory;

import BusinessLayer.ResponseT;
import DataAccessLayer.Supplier_Inv.Mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class RecordController {
    private List<FaultyItem> faultyItems;
    private List<Sale> sales;
    private Mapper mapper;

    public RecordController(){
        mapper = Mapper.getInstance();
        ResponseT<List<FaultyItem>> resFault = mapper.loadFaulty();
        ResponseT<List<Sale>> resSale = mapper.loadSales();
        if (!resFault.ErrorOccured() && !resSale.ErrorOccured() && resSale.value != null && resFault.value != null) {
            faultyItems = resFault.value;
            sales = resSale.value;
        } else {
            faultyItems = new LinkedList<FaultyItem>();
            sales = new LinkedList<Sale>();
        }
    }

    //adds a sale to list of sales and return Sale object
    public boolean addSale(int itemId, double itemCost, double salePrice, int amount) {
        Sale newSale = new Sale(itemId, itemCost, salePrice, amount);
        if (mapper.addSale(newSale).ErrorOccured() )
            return false;
        return sales.add(newSale);
    }

    //adds a faulty item to list of faulty items returns faulty item object
    public boolean addFaulty(int itemId, LocalDate expDate, int amountOfFaulty) {
        FaultyItem newFI = new FaultyItem(itemId, expDate, amountOfFaulty);
        if (mapper.addFaulty(newFI).ErrorOccured() )
            return false;
        return faultyItems.add(newFI);
    }

    //returns a string that
    public String faultyReport(LocalDate from) {
        return faultyReport(from, LocalDate.now());
    }

    public String faultyReport(LocalDate from, LocalDate to) {
        StringBuilder str = new StringBuilder();
        int i = 1;
        for (FaultyItem fi : faultyItems) {
            if ((fi.getExpDate().isAfter(from) || fi.getExpDate().isEqual(from))
                    && (fi.getExpDate().isBefore(to) || fi.getExpDate().isEqual(to))) {
                str.append(i++).append(")\n").append(fi).append("\n");
            }
        }
        return str.toString();
    }

    public String saleReport(LocalDateTime from) {
        return saleReport(from, LocalDateTime.now());
    }

    public String saleReport(LocalDateTime from, LocalDateTime to) {
        String str = "";
        int i = 1;
        for(Sale s : sales) {
            if ((s.getSaleDate().isAfter(from) || s.getSaleDate().isEqual(from))
                    && (s.getSaleDate().isBefore(to) || s.getSaleDate().isEqual(to))) {
                str = i + ")\n" + s + "\n";
            }
        }
        return str;
    }
}
