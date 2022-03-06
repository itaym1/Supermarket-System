package PresentationLayer.Supplier;

import BusinessLayer.FacadeController;
import BusinessLayer.Response;
import BusinessLayer.ResponseT;
import BusinessLayer.StringWarpper;
import BusinessLayer.Supplier.FacadeSupplier;
import BusinessLayer.Supplier.PeriodicOrder;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;

import static java.lang.System.exit;

/**
 * This is the presentation layer of the system.
 * The communication with the users are from here.
 */

public class IO_Supplier {

    private static IO_Supplier ioSupplier = null;
    public static FacadeSupplier facadeC = FacadeSupplier.getInstance();
    public static Scanner scanner = new Scanner(System.in);
    public static FacadeController facadeController = FacadeController.getInstance();

    private IO_Supplier() {

    }

    public static IO_Supplier getInstance() {
        if (ioSupplier == null)
            ioSupplier = new IO_Supplier();

        return ioSupplier;
    }

    public void init() {
        System.out.println( "1. Main Menu " + '\n' + "2. Exit From System ");
        try{
            int caseNumber = Integer.parseInt(scanner.nextLine());
            switch (caseNumber) {
                case 2:
                    System.out.println('\n'+ "You Are Log Out From The System, Have A Nice Day.\n");
                    exit(0);

                case 1:
                    ioSupplier.mainMenu();
                    break;

                default:
                    System.out.println("Please Choose Only 1-2" + '\n');
                    init();
            }
        }catch(Exception e){
            System.out.println("Invalid Input - Please Enter 1-2 " + '\n');
            init();
        }


    }

    public void mainMenu() {
        try{
            while (true) {
                System.out.println('\n' + "1. Suppliers And Products");
                System.out.println("2. Orders");
                System.out.println("3. Return Back");

                int caseNumber = Integer.parseInt(scanner.nextLine());
                switch (caseNumber) {
                    case 1:
                        suppliersAndProducts();
                        break;
                    case 2:
                        orders();
                        break;
                    case 3:
                        init();
                    default:
                        System.out.println("You Need To Choose Only 1-3");
                }
            }
        }catch (Exception e){
            System.out.println("Invalid Input - Please Enter 1-3 " + '\n');
            mainMenu();
        }

    }

    public void suppliersAndProducts() {

        System.out.println('\n' + "Please Choose One Of The Following Options : ");
        System.out.println("1. Add New Supplier");
        System.out.println("2. Delete Supplier");
        System.out.println("3. Update Supplier");
        System.out.println("4. Create Bill Of Quantity");
        System.out.println("5. Delete Bill Of Quantity");
        System.out.println("6. Edit Bill Of Quantity");
        System.out.println("7. Add Product To Supplier");
        System.out.println("8. Delete Product from Supplier");
        System.out.println("9. Show All Supplier Products");
        System.out.println("10. Show All Suppliers ");
        System.out.println("11. Show Specific Supplier ");
        System.out.println("12. Back To Main Menu ");
        try{
            int caseNumber = Integer.parseInt(scanner.nextLine());

            switch (caseNumber) {
                case 1:
                    createSupplierCard();
                    break;

                case 2:
                    System.out.println('\n' + "Enter Supplier ID You Would Like To Delete: ");
                    int SupplierID = Integer.parseInt(scanner.nextLine());
                    Response response = facadeC.deleteSupCard(SupplierID);
                    if (response.ErrorMessage != null) {
                        System.out.println(response.ErrorMessage);
                        return;
                    }
                    System.out.println("Deleted Successfully" + '\n');
                    break;

                case 3:
                    updateSupplier();
                    break;

                case 4:
                    createBillOfQ();
                    break;

                case 5:
                    System.out.println("Enter Supplier ID You Would Like To Delete His Bill Of Quantity: ");
                    int SuppID = Integer.parseInt(scanner.nextLine());
                    response = facadeC.deleteBillOfQuantity(SuppID);
                    if (response.ErrorMessage != null) {
                        System.out.println(response.ErrorMessage);
                        return;
                    }
                    break;

                case 6:
                    updateBill();
                    break;

                case 7:
                    addProdToSupp();
                    break;

                case 8:
                    System.out.println("Enter Supplier ID You Would Like To Delete His Product: ");
                    int SupplID = Integer.parseInt(scanner.nextLine());
                    System.out.println("Enter Product ID You Would Like To Delete: ");
                    int pid = Integer.parseInt(scanner.nextLine());
                    response = facadeC.removeProductToSupplier(SupplID,pid);
                    if (response.ErrorMessage != null) {
                        System.out.println(response.ErrorMessage);
                        return;
                    }
                    break;

                case 9:
                    System.out.println('\n' + "Enter Supplier ID You Would Like To See His Product: ");
                    int SupID = Integer.parseInt(scanner.nextLine());
                    ResponseT<StringWarpper> res = facadeC.showSupplierProducts(SupID);
                    if (res.ErrorMessage != null) {
                        System.out.println(res.ErrorMessage);
                        return;
                    }
                    String allSuppliersProd = res.value.getStr();
                    System.out.println(allSuppliersProd);
                    break;

                case 10:
                    res = facadeC.showAllSupplier();
                    if (res.ErrorMessage != null) {
                        System.out.println(res.ErrorMessage);
                        return;
                    }
                    System.out.println('\n' + res.value.getStr());
                    break;

                case 11:
                    System.out.println("Enter Supplier ID You Would Like To See His Details: ");
                    int SupId = Integer.parseInt(scanner.nextLine());
                    ResponseT<StringWarpper> resp = facadeC.showSupplierCard(SupId);
                    if (resp.ErrorMessage != null) {
                        System.out.println(resp.ErrorMessage);
                        return;
                    }
                    System.out.println(resp.value.getStr());
                    break;

                case 12:
                    return;

                default:
                    System.out.println("You Need To Choose Only 1-12");
            }
        }catch (Exception e){
            System.out.println("Invalid Input - Please Enter 1-3 " + '\n');
            suppliersAndProducts();
        }

    }

    public void orders(){
        System.out.println('\n' + "Please Choose One Of The Following Options : ");
        System.out.println("1. Create New Order");
        System.out.println("2. Delete Exist Order");
        System.out.println("3. Show All Orders From Suppliers");
        System.out.println("4. Show Order By Supplier");
        System.out.println("5. Periodic Orders ");
        System.out.println("6. Back To Main Menu ");
        try{
            int caseNumber = Integer.parseInt(scanner.nextLine());
            switch (caseNumber) {
                case 1:
                    createNewOrder();
                    break;

                case 2:
                    System.out.println('\n' + "Enter Order ID You Would Like To Delete: ");
                    int orderID = Integer.parseInt(scanner.nextLine());
                    Response res = facadeC.removeOrder(orderID);
                    if (res.ErrorMessage != null) {
                        System.out.println(res.ErrorMessage);
                        return;
                    }
                    System.out.println("Deleted Order Successfully");
                    break;

                case 3:
                    ResponseT<StringWarpper> resp = facadeC.showAllOrders();
                    if (resp.ErrorMessage != null) {
                        System.out.println(resp.ErrorMessage);
                        return;
                    }
                    System.out.println(resp.value.getStr());
                    break;

                case 4:
                    System.out.println('\n' + "Enter Supplier ID: ");
                    int supID = Integer.parseInt(scanner.nextLine());
                    if(!checkSupExist(supID)){return;}
                    orderBySupp(supID);
                    break;

                case 5:
                    PeriodicOrders();
                    break;

                case 6:
                    return;

                default:
                    System.out.println("You Need To Choose Only 1-6");
            }
        }catch (Exception e){
            System.out.println("Invalid Input - Please Enter 1-6 " + '\n');
            orders();
        }

    }

    public void PeriodicOrders(){
        System.out.println('\n' + "Please Choose One Of The Following Options : ");
        System.out.println("1. Create New Periodic Order ");
        System.out.println("2. Edit Periodic Order ");
        System.out.println("3. Delete Periodic Order ");
        System.out.println("4. Show All Periodic Orders ");
        System.out.println("5. Return Back ");
        try {
            int caseNumber = Integer.parseInt(scanner.nextLine());
            switch (caseNumber) {
                case 1:
                    createNewPeriodicOrder();
                    break;

                case 2:
                   editPeriodicOrder();
                   break;

                case 3:
                    System.out.println('\n' + "Enter Periodic Order ID You Would Like To Delete: ");
                    int orderID = Integer.parseInt(scanner.nextLine());
                    Response res = facadeC.removePOrder(orderID);
                    if (res.ErrorMessage != null) {
                        System.out.println(res.ErrorMessage);
                        return;
                    }
                    System.out.println("Deleted Order Successfully");
                    break;

                case 4:
                    ResponseT<StringWarpper> resp = facadeC.showAllPOrders();
                    if (resp.ErrorMessage != null) {
                        System.out.println(resp.ErrorMessage);
                        return;
                    }
                    System.out.println(resp.value.toString());
                    break;

                case 5:
                    return;

                default:
                    System.out.println("Invalid Input - Please Enter 1-5 " + '\n');
            }
        }catch (Exception e){
            System.out.println("Invalid Input - Please Enter 1-5 " + '\n');
            orders();
        }
    }

    public void editPeriodicOrder(){
        try{
            System.out.println('\n' + "Enter Periodic Order ID You Would Like To Edit: ");
            int orderID = Integer.parseInt(scanner.nextLine());
            if(!facadeC.checkOrderPExist(orderID) ){
                System.out.println("Order Does Not Exists");
                return;
            }
            if (!facadeC.checkOrderPEditable(orderID)) {
                System.out.println("Cannot edit periodic order 1 day before supply date");
                return;
            }
            System.out.println("1. Add Product To Exists Period Order");
            System.out.println("2. Remove Product From Exists Period Order");
            System.out.println("3. Change Interval Of Supply");
            System.out.println("4. Show All Super-Lee Items");
            System.out.println("5. Edit Quantity Of Product"); //TODO
            System.out.println("6. Return Back");

            int caseNumber = Integer.parseInt(scanner.nextLine());
            Response res;

            switch (caseNumber) {
                case 1:
                    System.out.println('\n' + "Enter Product ID:");
                    int productID = Integer.parseInt(scanner.nextLine());
                    System.out.println('\n' + "Enter Product Quantity:");
                    int quantity = Integer.parseInt(scanner.nextLine());

                    int check = facadeController.facadeInv.QuantityBiggerThenInvMin(quantity,productID);
                    if(check == -1){
                        System.out.println("Item Does Not Exists");
                        return;
                    }
                    if(check != quantity){
                        System.out.println('\n' + "The Quantity Of The Product Must Be Bigger Then: " + check);
                        return;
                    }
                    res = facadeC.addProductToExistPeriodicOrder(orderID,productID,quantity);
                    if (res.ErrorMessage != null) {
                        System.out.println(res.ErrorMessage);
                        return;
                    }
                    break;

                case 2:
                    System.out.println('\n' + "Enter Product ID You Want To Remove:");
                    productID = Integer.parseInt(scanner.nextLine());
                    res = facadeC.removeProdFromPOrder(productID, orderID);
                    if (res.ErrorMessage != null) {
                        System.out.println(res.ErrorMessage);
                        return;
                    }
                    System.out.println("Deleted Product Successfully");
                    break;

                case 3:
                    System.out.println('\n' + "Enter New Interval For The Order:");
                    int interval = Integer.parseInt(scanner.nextLine());
                    res = facadeC.changeInterval(interval, orderID);
                    if (res.ErrorMessage != null) {
                        System.out.println(res.ErrorMessage);
                        return;
                    }
                    System.out.println("Interval Edited Successfully");
                    break;

                case 4:
                    String s = facadeController.facadeInv.showAllItemsInSuper();
                    if (s == null) {
                        System.out.println("No Items Found");
                        return;
                    }
                    else{
                        System.out.println(s);
                    }
                    break;

                case 5:
                    System.out.println('\n' + "Enter Product ID You Want To Edit His Quantity:");
                    productID = Integer.parseInt(scanner.nextLine());
                    System.out.println('\n' + "Enter New Quantity:");
                    int quant = Integer.parseInt(scanner.nextLine());
                    res = facadeC.editQuantityForPOrder(productID, orderID,quant);
                    if (res.ErrorMessage != null) {
                        System.out.println(res.ErrorMessage);
                        return;
                    }
                    System.out.println("Edit Successfully");
                    break;

                case 6:
                    return;

                default:
                    System.out.println("You Need To Choose Only 1-8");
            }
        }catch (Exception e){
            System.out.println("Invalid Input, Please Enter 1-8 Only");
            updateSupplier();
        }

    }

    public void createNewPeriodicOrder(){
        try{
            LocalDate date;
            int interval;
            System.out.println('\n' + "Enter The First Date Of Which The Periodic Order Will Be: ");
            System.out.println('\n' + "Enter Number Of Year:");
            int year = Integer.parseInt(scanner.nextLine());
            System.out.println('\n' + "Enter Number Of Month:");
            int month = Integer.parseInt(scanner.nextLine());
            System.out.println('\n' + "Enter Number Of Day:");
            int day = Integer.parseInt(scanner.nextLine());
            if((year < 2021)| (12<month | month<0)| (day>31 | day<0)){
                System.out.println("Date Is Not Valid");
                return;
            }
            date = LocalDate.of(year,month,day);

            System.out.println('\n' + "Enter The Number Of Days Between Each Periodic Order: ");

            interval = Integer.parseInt(scanner.nextLine());

            ResponseT<Integer> res = facadeC.createPeriodicOrder(interval,date);
            if (res.ErrorMessage != null) {
                System.out.println(res.ErrorMessage);
                return;
            }
            int orderID = res.value;
            boolean finishOrder = false;

            while (!finishOrder) {
                System.out.println("1. Add Product To Periodic Order");
                System.out.println("2. Show All Super-Lee Products");
                System.out.println("3. Finish Order");

                int caseNumber = Integer.parseInt(scanner.nextLine());

                switch (caseNumber) {
                    case 1:
                        System.out.println('\n' + "Enter Product ID:");
                        int productID = Integer.parseInt(scanner.nextLine());
                        System.out.println('\n' + "Enter Product Quantity:");
                        int quantity = Integer.parseInt(scanner.nextLine());

                        int check = facadeController.facadeInv.QuantityBiggerThenInvMin(quantity,productID);
                        if(check == -1){
                            System.out.println("Item Does Not Exists");
                            facadeC.removePeriodicOrder(orderID);
                            return;
                        }
                        if(check != quantity){
                            System.out.println('\n' + "The Quantity Of The Product Must Be Bigger Then: " + check);
                            facadeC.removePeriodicOrder(orderID);
                            return;
                        }
                        Response response = facadeC.addProductToPeriodicOrder(orderID, date, interval,productID,quantity);
                        if (response.ErrorMessage != null) {
                            System.out.println(response.ErrorMessage);
                            return;
                        }
                        break;

                    case 2:
                        String s = facadeController.facadeInv.showAllItemsInSuper();
                        if (s == null) {
                            System.out.println("No Items Found");
                            return;
                        }
                        else{
                            System.out.println(s);
                        }
                        break;

                    case 3:
                        finishOrder = true;
                        if(facadeC.isEmptyPOrder(orderID)){
                            System.out.println('\n' + "No Products In This Order, This Order Will Be Deleted");
                            response = facadeC.removeOrder(orderID);
                            response = facadeC.removePeriodicOrder(orderID);
                            if (response.ErrorMessage != null) {
                                System.out.println(response.ErrorMessage);
                            }
                            return;
                        }
                        System.out.println("Order Finished Successfully");
                        return;
                    default:
                        System.out.println("You Need To Choose Only 1-3");

                }
            }
        }catch (Exception e){
            System.out.println("Invalid Input, Please Try Again");
            createNewPeriodicOrder();
        }
    }

    /** This function create from periodic order 1 to n orders from the suppliers in the system **/
    /** If The Create Of The Order Failed The Function Return The OrderID Otherwise Return -1 **/
    public int createOrdersFromPOrders(int pOrderID){
        ResponseT<HashMap<Integer, Integer>> response = facadeC.getProductOfporder(pOrderID);
        if (response.ErrorMessage != null) {
            System.out.println(response.ErrorMessage);
            return pOrderID;
        }
        HashMap<Integer, Integer> prods = response.value;
        //ordersToMake --> First Integer represent the supplierID, HashMap represent the products & quantity
        // that we need to order from this supplier
        HashMap<Integer,HashMap<Integer, Integer>> ordersToMake = facadeC.findCheapestSupplier(prods);
        // create order with supplier = supID, and supplier.values() = products
        for(Integer supplier : ordersToMake.keySet()){
            //create new order
            ResponseT<Integer> res = facadeC.createOrder(supplier);
            if (res.ErrorMessage != null) {
                System.out.println(res.ErrorMessage);
                return pOrderID;
            }
            int orderID = res.value;
            // add each product to the order
            HashMap<Integer, Integer> supProds = ordersToMake.get(supplier);
            for(Integer itemID : supProds.keySet()){
                Response r = facadeC.addProductToOrder(supplier,orderID,itemID,supProds.get(itemID));
                if (r.ErrorMessage != null) {
                    System.out.println(r.ErrorMessage);
                    return pOrderID;
                }
            }
            // calculate final price for order
            Response resp = facadeC.finalPriceForOrder(orderID, supplier);
            if (resp.ErrorMessage != null) {
                System.out.println(resp.ErrorMessage);
                return pOrderID;
            }

            boolean pickup = facadeC.needPickUp(supplier);
            facadeController.sendOrderToDelivery(orderID,supplier,pickup);

        }
        return -1;
    }

    public void createSupplierCard() {
        try{
            System.out.println('\n' + "Enter Supplier ID: ");
            int SupplierID = Integer.parseInt(scanner.nextLine());

            Response res = facadeC.checkSuppNotExist(SupplierID);
            if( res.ErrorMessage != null){
                System.out.println(res.ErrorMessage);
                return;
            }

            System.out.println("Enter Supplier Name: ");
            String SupplierName = scanner.nextLine();

            System.out.println("Enter Address: ");
            String Address = scanner.nextLine();

            System.out.println("Enter E-mail: ");
            String mail = scanner.nextLine();

            System.out.println("Enter Bank Account Number: ");
            int bankAcc = Integer.parseInt(scanner.nextLine());

            System.out.println("Enter Payment Method: ");
            String payment = scanner.nextLine();

            System.out.println("Enter Contact Details: ");
            System.out.println("Enter Private Name: ");
            String pname = scanner.nextLine();
            System.out.println("Enter Contact Phone Number:");
            int phoneNumber = Integer.parseInt(scanner.nextLine());
            String contacts = pname + " " + phoneNumber;

            System.out.println("Enter Days of Supply Separated By Spaces: ");
            String infoSupplyDay = scanner.nextLine().toUpperCase(Locale.ROOT);

            System.out.println("The Supplier Need Pickup?, (Y/N): ");
            System.out.println("1. Yes");
            System.out.println("2. No");
            boolean pickUp = false;
            int caseNumber = Integer.parseInt(scanner.nextLine());
            switch (caseNumber) {
                case 1:
                    pickUp = true;
                    break;
                case 2:
                    break;
                default:
                    System.out.println("You Need To Choose Only 1-2");
            }
            res = facadeC.createSupCard(SupplierName,SupplierID,Address,mail,bankAcc,payment,contacts,infoSupplyDay,pickUp);
            if( res.ErrorMessage != null)
                System.out.println(res.ErrorMessage);
        }catch (Exception e){
            System.out.println("Invalid Input, Please Try Again" + '\n');
            createSupplierCard();
        }
    }

    public void updateSupplier(){
        try{
            System.out.println('\n' + "Enter Supplier ID You Would Like To Update: ");
            int SupplierID = Integer.parseInt(scanner.nextLine());

            if(!checkSupExist(SupplierID)){return;}

            System.out.println("1. Edit Supplier Name");
            System.out.println("2. Edit Supplier Address");
            System.out.println("3. Edit Supplier Email");
            System.out.println("4. Edit Supplier Bank Account");
            System.out.println("5. Edit Supplier Payment Method");
            System.out.println("6. Edit Supplier Contacts ");
            System.out.println("7. Edit Info Supply Day");
            System.out.println("8. Edit Supplier Pick Up");

            int caseNumber = Integer.parseInt(scanner.nextLine());
            Response res;

            switch (caseNumber) {
                case 1:
                    System.out.println("Enter New Name: ");
                    String name = scanner.nextLine();
                    res = facadeC.EditSupplierName(SupplierID, name);
                    if(res.ErrorMessage != null){
                        System.out.println(res.ErrorMessage);
                    }
                    break;

                case 2:
                    System.out.println("Enter New Address: ");
                    String address = scanner.nextLine();
                    res = facadeC.EditAddress(SupplierID, address);
                    if(res.ErrorMessage != null){
                        System.out.println(res.ErrorMessage);
                    }
                    break;

                case 3:
                    System.out.println("Enter New Email: ");
                    String mail = scanner.nextLine();
                    res = facadeC.EditEmail(SupplierID, mail);
                    if(res.ErrorMessage != null){
                        System.out.println(res.ErrorMessage);
                    }
                    break;

                case 4:
                    System.out.println("Enter New Bank Account: ");
                    int bank = Integer.parseInt(scanner.nextLine());
                    res = facadeC.EditBankAccount(SupplierID, bank);
                    if(res.ErrorMessage != null){
                        System.out.println(res.ErrorMessage);
                    }
                    break;

                case 5:
                    System.out.println("Enter New Payment Method: ");
                    String pay = scanner.nextLine();
                    res = facadeC.EditPaymentMethod(SupplierID, pay);
                    if(res.ErrorMessage != null){
                        System.out.println(res.ErrorMessage);
                    }
                    break;

                case 6:
                    System.out.println("Enter New Contacts: ");
                    String contacts = scanner.nextLine();
                    res  = facadeC.EditContact(SupplierID, contacts);
                    if(res.ErrorMessage != null){
                        System.out.println(res.ErrorMessage);
                    }
                    break;

                case 7:
                    System.out.println("Enter New Info Supply Day: ");
                    String supp = scanner.nextLine();
                    res = facadeC.EditInfoSupDay(SupplierID, supp);
                    if(res.ErrorMessage != null){
                        System.out.println(res.ErrorMessage);
                    }
                    break;

                case 8:
                    System.out.println("The Supplier Need Pickup?, (Y/N): ");
                    System.out.println("1. Yes");
                    System.out.println("2. No");
                    boolean pickUp = false;
                    int caseNumber2 = Integer.parseInt(scanner.nextLine());
                    switch (caseNumber2) {
                        case 1:
                            pickUp = true;
                            break;
                        case 2:
                            break;
                        default:
                            System.out.println("You Need To Choose Only 1-2");
                    }
                    res = facadeC.EditPickup(SupplierID, pickUp);
                    if(res.ErrorMessage != null){
                        System.out.println(res.ErrorMessage);
                    }
                    break;
                default:
                    System.out.println("You Need To Choose Only 1-8");
            }
        }catch (Exception e){
            System.out.println("Invalid Input, Please Enter 1-8 Only");
            updateSupplier();
        }

    }

    public void createBillOfQ() {
        try{
            System.out.println("Enter Supplier ID: ");
            int SupplierID = Integer.parseInt(scanner.nextLine());

            if (!checkSupExist(SupplierID)) {
                return;
            }
            Response res = facadeC.checkBillExist(SupplierID);
            if (res.ErrorMessage != null) {
                System.out.println(res.ErrorMessage);
                return;
            }

            boolean exit = false;
            HashMap<Integer, Integer> minQuantityForDis = new HashMap<>();
            HashMap<Integer, Integer> discountList = new HashMap<>();

            while (!exit) {
                System.out.println("Enter Product ID Which You Want To Make Discount: ");
                int ProdID = Integer.parseInt(scanner.nextLine());
                res = facadeC.checkProductExist(SupplierID, ProdID);
                if (res.ErrorMessage != null) {
                    System.out.println(res.ErrorMessage);
                    return;
                }
                // if the user already insert this product to the supplier's bill of quantity
                if (minQuantityForDis.get(ProdID) != null) {
                    System.out.println("This Product Already Has A Discount");
                    return;
                }

                System.out.println("Enter The Minimum Amount Of Ordering This Product For The Discount: ");
                int minDis = Integer.parseInt(scanner.nextLine());
                System.out.println("Enter The Discount In Percentage, 1-100: ");
                int percentage = Integer.parseInt(scanner.nextLine());
                if (percentage < 1 || percentage > 100) {
                    System.out.println("Invalid Discount");
                    return;
                }

                minQuantityForDis.put(ProdID, minDis);
                discountList.put(ProdID, percentage);
                facadeC.addBilltoDB(SupplierID, ProdID, minDis, percentage);
                System.out.println("Do You Want To Add Another Product? " + '\n' + "1. Yes" + '\n' + "2. No");
                int caseNumber = Integer.parseInt(scanner.nextLine());
                switch (caseNumber) {
                    case 1:
                        continue;

                    case 2:
                        exit = true;
                        break;

                    default:
                        System.out.println("You Need To Choose Only 1 Or 2");
                }
            }
            res = facadeC.addBillOfQuantity(SupplierID, minQuantityForDis, discountList);
            if (res.ErrorMessage != null) {
                System.out.println(res.ErrorMessage);
                return;
            }
        }catch (Exception e){
            System.out.println("Invalid Input, Please Try Again" + '\n');
            createBillOfQ();
        }
    }

    public void updateBill(){
        try{
            System.out.println('\n' + "Enter Supplier ID You Would Like To Update His Bill Of Quantity: ");
            int SupplierID = Integer.parseInt(scanner.nextLine());

            Response res = facadeC.checkBillNotExist(SupplierID);
            if (res.ErrorMessage != null) {
                System.out.println(res.ErrorMessage);
                return;
            }

            System.out.println("1. Edit Minimum Quantity For Product");
            System.out.println("2. Edit Discount For Product");
            System.out.println("3. Add New Product To Bill Of Quantity");
            System.out.println("4. Delete Product From Bill Of Quantity");

            int caseNumber = Integer.parseInt(scanner.nextLine());

            switch (caseNumber) {
                case 1:
                    System.out.println("Enter Product ID: ");
                    int pid = Integer.parseInt(scanner.nextLine());
                    res = facadeC.checkProductInBillOfQ(SupplierID, pid);
                    if (res.ErrorMessage != null) {
                        System.out.println(res.ErrorMessage);
                        return;
                    }
                    System.out.println("Enter New Minimum Quantity: ");
                    int min = Integer.parseInt(scanner.nextLine());
                    res = facadeC.editMinQuantity(SupplierID, pid, min);
                    if (res.ErrorMessage != null) {
                        System.out.println(res.ErrorMessage);
                        return;
                    }
                    break;

                case 2:
                    System.out.println("Enter Product ID: ");
                    pid = Integer.parseInt(scanner.nextLine());
                    res = facadeC.checkProductInBillOfQ(SupplierID, pid);
                    if (res.ErrorMessage != null) {
                        System.out.println(res.ErrorMessage);
                        return;
                    }
                    System.out.println("Enter New Discount: ");
                    int discount = Integer.parseInt(scanner.nextLine());
                    if(discount<1 || discount> 100){
                        System.out.println("Invalid Discount");
                        return;
                    }
                    res = facadeC.editDiscount(SupplierID, pid, discount);
                    if (res.ErrorMessage != null) {
                        System.out.println(res.ErrorMessage);
                        return;
                    }
                    break;

                case 3:
                    System.out.println("Enter Product ID: ");
                    pid = Integer.parseInt(scanner.nextLine());
                    res = facadeC.checkProductInBillOfQ(SupplierID, pid);
                    if (res.ErrorMessage != null) {
                        System.out.println(res.ErrorMessage);
                        return;
                    }
                    // if the user already insert this product to the supplier's bill of quantity
                    res = facadeC.checkProductInBillOfQ(SupplierID,pid);
                    if (res.ErrorMessage != null) {
                        System.out.println(res.ErrorMessage);
                        return;
                    }
                    System.out.println("Enter New Minimum Quantity:");
                    int minQ = Integer.parseInt(scanner.nextLine());
                    System.out.println("Enter New Discount, 1-100: ");
                    int disc = Integer.parseInt(scanner.nextLine());
                    if(disc<1 || disc> 100){
                        System.out.println("Invalid Discount");
                        return;
                    }
                    res = facadeC.addProdToBill(SupplierID, pid, minQ, disc);
                    if (res.ErrorMessage != null) {
                        System.out.println(res.ErrorMessage);
                        return;
                    }
                    break;

                case 4:
                    System.out.println("Enter Product ID: ");
                    pid = Integer.parseInt(scanner.nextLine());
                    res = facadeC.removeProdFromBill(SupplierID, pid);
                    if (res.ErrorMessage != null) {
                        System.out.println(res.ErrorMessage);
                        return;
                    }
                    break;

                default:
                    System.out.println("You Need To Choose Only 1-4");

            }
        }catch (Exception e){
            System.out.println("Invalid Input, Please Enter 1-4 Only");
            updateBill();
        }

    }

    public void addProdToSupp(){
        try{
            System.out.println("Enter Supplier ID: ");
            int SupplierID = Integer.parseInt(scanner.nextLine());

            if(!checkSupExist(SupplierID)){return;}

            System.out.println("Enter Supplier Product ID: ");
            int pid = Integer.parseInt(scanner.nextLine());

            System.out.println("Enter Super-Lee Product ID: ");
            int pidSuperLee = Integer.parseInt(scanner.nextLine());

            System.out.println("Enter Product Name : ");
            String name = scanner.nextLine();

            System.out.println("Enter Product Category : ");
            String category = scanner.nextLine();

            System.out.println("Enter Product Price : ");
            double price = Double.parseDouble(scanner.nextLine());
            if(price<=0){
                System.out.println("Price is Invalid");
                return;
            }

            Response res = facadeC.addProductToSupplier(SupplierID,pid,name,category,price,pidSuperLee);
            if (res.ErrorMessage != null) {
                System.out.println(res.ErrorMessage);
                return;
            }
        }catch (Exception e) {
            System.out.println("Invalid Input, Please Try Again");
            addProdToSupp();
        }
    }

    public void createNewOrder(){
        try{
            System.out.println('\n' + "Enter Supplier ID Which You Would Like To Take Order From: ");
            int SupplierID = Integer.parseInt(scanner.nextLine());
            if(!checkSupExist(SupplierID)){return;}
            ResponseT<Integer> res = facadeC.createOrder(SupplierID);
            if (res.ErrorMessage != null) {
                System.out.println(res.ErrorMessage);
                return;
            }
            int orderID = res.value;
            boolean finishOrder = false;

            while (!finishOrder) {
                System.out.println("1. Add Product");
                System.out.println("2. Remove Product");
                System.out.println("3. Update Product Quantity");
                System.out.println("4. Show Products Of The Supplier");
                System.out.println("5. Finish Order");

                int caseNumber = Integer.parseInt(scanner.nextLine());

                switch (caseNumber) {
                    case 1:
                        System.out.println('\n' + "Enter Product ID:");
                        int productID = Integer.parseInt(scanner.nextLine());
                        Response response = facadeC.checkProductExist(SupplierID, productID);
                        if (response.ErrorMessage != null) {
                            System.out.println(response.ErrorMessage);
                            return;
                        }
                        System.out.println('\n' + "Enter Product Quantity:");
                        int quantity = Integer.parseInt(scanner.nextLine());
                        response = facadeC.addProductToOrder(SupplierID,orderID,productID,quantity);
                        if (response.ErrorMessage != null) {
                            System.out.println(response.ErrorMessage);
                            return;
                        }
                        break;

                    case 2:
                        System.out.println('\n' + "Enter Product ID You Would Like To Remove:");
                        productID = Integer.parseInt(scanner.nextLine());
                        response = facadeC.removeFromOrder(productID,orderID);
                        if (response.ErrorMessage != null) {
                            System.out.println(response.ErrorMessage);
                            return;
                        }
                        break;

                    case 3:
                        System.out.println('\n' + "Enter Product ID:");
                        productID = Integer.parseInt(scanner.nextLine());
                        response = facadeC.checkProductExist(SupplierID, productID);
                        if (response.ErrorMessage != null) {
                            System.out.println(res.ErrorMessage);
                            return;
                        }
                        response = facadeC.productInOrder(orderID,productID);
                        if (response.ErrorMessage != null) {
                            System.out.println(response.ErrorMessage);
                            return;
                        }
                        System.out.println('\n' + "Enter New Product Quantity:");
                        quantity = Integer.parseInt(scanner.nextLine());
                        response = facadeC.updateProdQuantity(orderID,productID,quantity);
                        if (response.ErrorMessage != null) {
                            System.out.println(response.ErrorMessage);
                            return;
                        }
                        break;

                    case 4:
                        ResponseT<StringWarpper> resp = facadeC.showSupplierProducts(SupplierID);
                        if (resp.ErrorMessage != null) {
                            System.out.println(resp.ErrorMessage);
                            return;
                        }
                        System.out.println(resp.value.getStr());
                        break;

                    case 5:
                        finishOrder = true;
                        if(facadeC.isEmptyOrder(orderID)){
                            System.out.println('\n' + "No Products In This Order, This Order Will Be Deleted");
                            response = facadeC.removeOrder(orderID);
                            if (response.ErrorMessage != null) {
                                System.out.println(response.ErrorMessage);
                                return;
                            }
                        }
                        else{
                            response = facadeC.finalPriceForOrder(orderID, SupplierID);
                            if (response.ErrorMessage != null) {
                                System.out.println(response.ErrorMessage);
                                return;
                            }
                            resp = facadeC.showOrder(orderID);
                            if (resp.ErrorMessage != null) {
                                System.out.println(resp.ErrorMessage);
                                return;
                            }
                            System.out.println('\n' + "Order Summary: " + resp.value);
                        }
                        break;
                    default:
                        System.out.println("You Need To Choose Only 1-5");

                }
            }
        }catch (Exception e){
            System.out.println("Invalid Input, Please Try Again");
            createNewOrder();
        }


    }

    public void orderBySupp(int supID){
        try{
            System.out.println('\n' + "Please Choose One Of The Following Options : ");
            System.out.println("1. Show All From Supplier");
            System.out.println("2. Show Specific Order From Supplier");

            int caseNumber = Integer.parseInt(scanner.nextLine());
            switch (caseNumber) {
                case 1:
                    ResponseT<StringWarpper> res = facadeC.showOrdersBySupplier(supID);
                    if(res.ErrorMessage != null){
                        System.out.println(res.ErrorMessage);
                        return;
                    }
                    System.out.println(res.value.getStr());
                    break;

                case 2:
                    System.out.println("Enter Order ID You Would Like To Show: ");
                    int orderID = Integer.parseInt(scanner.nextLine());
                    res = facadeC.showOrder(orderID);
                    if(res.ErrorMessage != null){
                        System.out.println(res.ErrorMessage);
                        return;
                    }
                    System.out.println(res.value);
                    break;

            }
        }catch (Exception e){
            System.out.println("Invalid Input, Please Enter 1-2");
            orderBySupp(supID);
        }

    }

    private boolean checkSupExist(int SupplierID){
        Response res = facadeC.checkSuppExist(SupplierID);
        if(res.ErrorMessage != null){
            System.out.println(res.ErrorMessage);
            return false;
        }
        return true;
    }

    public void checkForApproachingPOrders() {
        try{
            String stringForFail = "The System Tried To Sent Periodic Order, Due To Failure " + '\n' +
                    "The Next Orders Didn't Sent, Please Handle Those Order Later: "+ '\n';
            int faildOrders = 0;
            HashMap<Integer, PeriodicOrder> periodicOrders  = facadeC.checkForApproachingPOrders();
            for(PeriodicOrder po : periodicOrders.values()){
                int orderID = po.getpOrderID();
                int failure = createOrdersFromPOrders(orderID);
                if(failure != -1){
                    faildOrders++;
                    stringForFail += "OrderID: " + failure + '\n';
                }
            }
            stringForFail += '\n' + "Total " + faildOrders + "Orders Failed";
            if(faildOrders != 0){
                System.out.println(stringForFail);
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }


    }
}


