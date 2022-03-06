package PresentationLayer;
import BusinessLayer.EmployeesBuisnessLayer.FacadeController;
import serviceObjects.ResponseT;
import java.util.Scanner;

public class CLI {
    Scanner scanner;
    CLIController controller;

    public CLI(){
        scanner = new Scanner(System.in);
        controller = new CLIController();
    }

    public void start() {
        String ID;
        int action;

        do {
            LoginWindow();
            ID = scanner.next();
            if(ID.equals("0"))
                break;
            ResponseT<Boolean> r = controller.isEmployee(ID);
            while(r.isErrorOccured() || !r.getValue()) {
                System.out.println("ID not found, please try again");
                ID = scanner.next();
                r = FacadeController.getInstance().isEmployee(ID);
            }
            do {
                MainMenuWindow();
                action = scanner.nextInt();
                if (action == 0)
                    break;
                controller.handleManagement(action, ID);
            }while (true);
        }while (true);
    }

    private void LoginWindow(){
        System.out.println("""
                Hi,
                Welcome to Nitzan's code.
                Also known as "Super Lee",
                But Let's keep it Nitzan's code.
                
                Enter your ID please. quick.
                To exit (why? don't you like my code?) press 0
                """);
    }

    private void MainMenuWindow(){
        System.out.println("""
                1) Supplier Management
                2) Employee Management
                3) Inventory Management
                4) Delivery Management
                
                To exit press 0
                """);
    }

    //    public void start() {
//
//        String ID;
//        int action;
//
//        do {
//            employeeCLI.DisloginMenu();
//            ID = scanner.next();
//            if(ID.equals("0"))
//                break;
//            ResponseT<Boolean> r = FacadeController.getInstance().isDeliveryManager(ID);
//            while(r.isErrorOccured()) {
//                System.out.println("ID not found, please try again");
//                ID = scanner.next();
//                r = FacadeController.getInstance().isDeliveryManager(ID);
//            }
//            if (r.getValue()) {
//                do {
//                    //The User is Delivery manager
//                    deliveryCLI.runWithConsole();
//                    action = scanner.nextInt();
//                    if (action == 0)
//                        break;
////                    scanner.nextLine();
////                    cliController.Mmainmanue(action);
//                } while (true);
//            } else {
//                do {
//                    employeeCLI.start(ID);
//                    action = scanner.nextInt();
//                    if (action == 0)
//                        break;
//                    scanner.nextLine();
//                    employeeCLI.start(ID);
//                } while (true);
//
//            }
//        }while (true);
//    }

}