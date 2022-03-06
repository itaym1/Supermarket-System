import BusinessLayer.DeliveryBusinessLayer.FacadeController;
import PresentationLayer.CLI;
import PresentationLayer.DeliveryPresentationLayer.DeliveryCLI;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


public class MainDelEmp {
    public static void main(String[] args){
        CLI cli = new CLI();
        cli.start();
//        DeliveryCLI cli = new DeliveryCLI();
//        cli.runWithConsole();
//        FacadeController.getInstance().getOrder();
//        HashMap<Integer, Integer> intIntLstOfProducts = new HashMap<>();
//        intIntLstOfProducts.put(123,123);
//        intIntLstOfProducts.put(456,456);
//        intIntLstOfProducts.put(789,789);
//        Map<String , Integer> lstOfProducts =
//                intIntLstOfProducts.entrySet().stream().collect(Collectors.toMap(
//                        entry -> Integer.toString(entry.getKey()),
//                        entry -> entry.getValue())
//                );
//        System.out.println(lstOfProducts.get(123));
    }
}
