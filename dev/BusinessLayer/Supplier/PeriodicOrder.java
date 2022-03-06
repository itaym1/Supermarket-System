package BusinessLayer.Supplier;

import java.time.LocalDate;
import java.util.HashMap;

public class PeriodicOrder {
    private int pOrderID;
    private int interval;
    private LocalDate dateOfSupply; // the last date of supply
    private HashMap<Integer,Integer> products; // <productID: Integer, quantity: Integer>

    public PeriodicOrder(int pOrderID, LocalDate dateOfSupply,int interval){
        this.pOrderID = pOrderID;
        this.dateOfSupply = dateOfSupply;
        this.interval = interval;
        this.products = new HashMap<>();
    }
    public PeriodicOrder(int pOrderID, int interval, LocalDate dateOfSupply, HashMap<Integer,Integer> prods){
        this.pOrderID = pOrderID;
        this.interval = interval;
        this.dateOfSupply = dateOfSupply;
        this.products = prods;
    }

    public HashMap<Integer, Integer> getProducts() {
        return products;
    }

    public int getpOrderID() {
        return pOrderID;
    }

    public LocalDate getDateOfSupply() {
        return dateOfSupply;
    }

    public void setDateOfSupply(LocalDate dateOfSupply) {
        this.dateOfSupply = dateOfSupply;
    }

    public String toString(){
        String prods = ""+'\n';
        for(Integer i :products.keySet()){
            prods += '\n' + "Product ID: " + i + " , Quantity: " + products.get(i);
        }
        return '\n' +"Order ID: " + pOrderID + ",   interval : " + interval + ",   Start Date: " + dateOfSupply + ",   Products:" + prods +'\n';
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }
}
