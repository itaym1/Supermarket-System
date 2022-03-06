package BusinessLayer.Supplier;

import java.util.HashMap;

public class BillOfQuantities {

    private HashMap<Integer, Integer> minQuantityForDis; // <prodID: Integer, minQuantity: Integer>
    private HashMap<Integer, Integer> discountList; // <prodID: Integer, percentDis: Integer>

    public BillOfQuantities(HashMap<Integer, Integer> minQuantityForDis, HashMap<Integer, Integer> discountList){
        this.minQuantityForDis = minQuantityForDis;
        this.discountList = discountList;
    }

    public HashMap<Integer, Integer> getDiscountList() {
        return discountList;
    }

    public HashMap<Integer, Integer> getMinQuantityForDis() {
        return minQuantityForDis;
    }
}
