package BusinessLayer;

import BusinessLayer.Inventory.FacadeInv;
import BusinessLayer.Supplier.FacadeSupplier;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

/**
 * This is the Facade of the system.
 * This class hold two more facade for each package: suppliers and inventory
 */

public class FacadeController {
    private static FacadeController facadeController = null;
    public FacadeInv facadeInv;
    public FacadeSupplier facadeSupplier;
    public BusinessLayer.DeliveryBusinessLayer.FacadeController facadeDelivery;

    private FacadeController() {
        facadeInv = FacadeInv.getInstance();
        facadeSupplier = FacadeSupplier.getInstance();
        facadeDelivery = BusinessLayer.DeliveryBusinessLayer.FacadeController.getInstance();
    }

    public static FacadeController getInstance() {
        if (facadeController == null)
            facadeController = new FacadeController();

        return facadeController;
    }

    // after each order we send info to match delivery.
    public void sendOrderToDelivery(int orderID, int suppID, boolean pickup) {
        HashMap<Integer,Integer> prodQuantity = facadeSupplier.getOrderController().switchToSuperLeeID(orderID, suppID);
        //Supplier details:
        String address = facadeSupplier.getSupplierController().getSuppliers().get(suppID).getAddress();
        String[] contact = (facadeSupplier.getSupplierController().getSuppliers().get(suppID).getContacts()).split(" ");
        String contactName = contact[0];
        String phoneNumber= contact [1];
        String[] infoSupplyDates = (facadeSupplier.getSupplierController().getSuppliers().get(suppID).getInfoSupplyDay()).split(" ");
        ArrayList<LocalDate> supplierDays = supplyDateFunction(infoSupplyDates);
        facadeDelivery.assignAutoTask(prodQuantity, address, phoneNumber, contactName, "loading", supplierDays);
    }

    private ArrayList<LocalDate> supplyDateFunction(String[] infoSupplyDates){
        ArrayList<LocalDate> list = new ArrayList<>();
        LocalDate todayDate = LocalDate.now();
        int today = LocalDate.now().getDayOfWeek().getValue();
        for(String day : infoSupplyDates){
            int dayVal = DayOfWeek.valueOf(day.toUpperCase(Locale.ROOT)).getValue();
            int diff = Math.abs(today - dayVal);
            if(diff == 0){
                list.add(todayDate.plusDays(7));
            }
            else if (today < dayVal){
                list.add(todayDate.plusDays(diff));
            }
            else if (dayVal < today){
                list.add(todayDate.plusDays(7-diff));
            }
        }
        return list;
    }

    public ResponseT<StringBuilder> ordersByLack(HashMap<Integer, Integer> stkReport){
        try{
            ResponseT<StringBuilder> s = facadeSupplier.ordersByLack(stkReport);

            List<Integer> orders = new ArrayList<>();
            String[] strArr =  s.value.toString().split(" ");
            for(int i=0; i<strArr.length; i++){
                if(strArr[i].equals("OrderID:")){
                    // each order id is added to list of orders
                    orders.add(Integer.parseInt(strArr[i+1]));
                }
            }

            // for each order we sent the specific details to delivery module.
            // SupplierID,orderID,PickUp
            for(Integer order : orders){
                int SupplierID = facadeSupplier.getOrderController().getOrders().get(order).getSupplierID();
                boolean isPickUp = facadeSupplier.getSupplierController().needPickUp(SupplierID);
                sendOrderToDelivery(order,SupplierID,isPickUp);
            }
            return s;

        }catch (Exception e){
            return new ResponseT<>(e.getMessage());
        }
    }

    public void acceptOrder(HashMap<Integer, Integer> items, Map<Integer, Integer> faultyItems) {
        //Map<Integer, Integer> itemsInOrder=facadeDelivery.getOrder();// = facadeDelivery.getUpdatedDelivery(orderId, faultyItems);
        //TODO: get order from delivery after updated the faulty items
        //System.out.println(items.toString());
        for (int key : items.keySet()) {
            facadeInv.addToStorage(key, items.get(key));
        }
        for (int key : faultyItems.keySet()) {
            facadeInv.addFaulty(key,0, faultyItems.get(key));
        }
    }

    public void rejectOrder() {
        //facadeDelivery.rejectDelivery(orderId);
        //TODO: reject delivery (pop order from queue)
    }

    public String getOrderString(int orderId) {
        //facadeDelivery.getOrderString(orderId);
        //TODO: facadeDelivery\facadeSupplier return order string (pop from queue?)
        return "";
    }
}
