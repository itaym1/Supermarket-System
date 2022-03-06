package PresentationLayer;


import BusinessLayer.EmployeesBuisnessLayer.FacadeController;
import PresentationLayer.DeliveryPresentationLayer.DeliveryCLI;
import PresentationLayer.EmployeesPresentationLayer.EmployeeCLI;
import PresentationLayer.Inventory.INV_IO;
import PresentationLayer.Supplier.IO_Supplier;
import serviceObjects.ResponseT;

public class CLIController {
    FacadeController facade;
    EmployeeCLI employeeCLI;
    DeliveryCLI deliveryCLI;
    IO_Supplier supplierCLI;
    INV_IO inventoryCLI;

    public CLIController() {
        facade = FacadeController.getInstance();
        employeeCLI = EmployeeCLI.getInstance();
        deliveryCLI = DeliveryCLI.getInstance();
        supplierCLI = IO_Supplier.getInstance();
        inventoryCLI = INV_IO.getInstance();
    }

    public void handleManagement(int action, String userID){
        switch (action){
            case 1:
                if(hasRole(userID, "Storekeeper"))
                    supplierCLI.init();
                else System.out.println("You are not permitted entering Supplier Management");
                break;
            case 2:
                employeeCLI.start(userID);
                break;
            case 3:
                if(hasRole(userID, "Storekeeper"))
                    inventoryCLI.start();
                else System.out.println("You are not permitted entering Inventory Management");
                break;
            case 4:
                if(hasRole(userID, "Delivery Manager"))
                    deliveryCLI.runWithConsole();
                else System.out.println("You are not permitted entering Delivery Management");
                break;
            default:
                System.out.println("Incorrect typing");
                break;
        }
    }

    public boolean hasRole(String id, String role){
        ResponseT<Boolean> r = facade.hasRole(id, role);
        if(r.isErrorOccured()) {
            System.out.println(r.getErrorMessage());
            return false;
        }
        return r.getValue();
    }

    public ResponseT<Boolean> isEmployee(String id) {
        return facade.isEmployee(id);
    }
}