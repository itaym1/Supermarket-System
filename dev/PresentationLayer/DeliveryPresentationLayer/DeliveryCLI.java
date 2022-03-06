package PresentationLayer.DeliveryPresentationLayer;

import BusinessLayer.DeliveryBusinessLayer.DeliveryController;
import BusinessLayer.DeliveryBusinessLayer.FacadeController;
import DataAccessLayer.DeliveryDataAccessLayer.DTO.*;
import serviceObjects.ResponseT;

import java.time.LocalDate;
import java.util.*;
import java.time.format.*;

public class DeliveryCLI {
    private static DeliveryCLI deliveryCLI = null;
    FacadeController fc;

    public DeliveryCLI() {
        fc = FacadeController.getInstance();
    }

    public static DeliveryCLI getInstance(){
        if (deliveryCLI == null)
            deliveryCLI = new DeliveryCLI();
        return deliveryCLI;
    }

    public void runWithConsole() {
        Scanner in = new Scanner(System.in);
        System.out.println("welcome to Delivery Module!");
        System.out.println("for exit the simulation type 'exit' as input at any point");
        System.out.println("press <Enter> to continue!");
        in.nextLine();
        String s = "";
        boolean isFirstIteration = true;
        while (!s.equals("exit")) {
            if (isFirstIteration) {
                System.out.println("now the system will show you its current state:\n press <Enter> to continue");
                in.nextLine();
                printSystemCurrentState();
                System.out.println("Press <Enter> to see what action can you do on the system");
                in.nextLine();
            }
            else{
                System.out.println(this.fc.toStringResponse().getData());
            }
            isFirstIteration = false;
            System.out.println("chose action:\n1 add new delivery\n2 update existing delivery\n3 create new appending task" +
                    "\n4 add Truck to the sys\n5 add Area to the sys\n6 add location to the sys\n7 send delivery\n8 display documentation");
            s = in.nextLine().strip();
            chooseAction(s); // Todo: its tachles nees to be after the while, to the case that press immediate exit
        }

    }

    private void printSystemCurrentState() {
        // getting String current state from the facade.
        System.out.println(this.fc.toStringResponse().getData());
    }

    private void chooseAction(String s) {
        switch (s) {
            case ("1"): {
                this.createNewDelivery();
                break;
            }
            case ("2"): {
                this.updateDelivery();
                break;
            }
            case ("3"): {
                this.addNewTask();
                break;
            }
            case ("4"): {
                this.addNewTrack();
                break;
            }
//            case ("5"): {
//                this.addNewDriver();
//                break;
//            }
            case ("5"): {
                this.addNewArea();
                break;
            }
            case ("6"): {
                this.addNewLocation();
                break;
            }
            case ("7"): {
                this.sendDelivery();
                break;
            }
            case ("8"): {
                this.displayDoc();
                break;
            }
        }
    }

    private void displayDoc() {
        Scanner in = new Scanner(System.in);
        String choose = "";
        while (true) {
            do {
                System.out.println("Which data do you want to present:");
                System.out.println("1) Areas\n2) Drivers\n3) Trucks\n4) Tasks\n5) Deliveries");
                choose = in.nextLine().strip();
            }while (!isLegalChoice(5,choose));
            chooseData(choose);
            System.out.println("What would you like to do:");
            System.out.println("1) back to menu");
            System.out.println("2) choose another data to present");
            String inp = "" ;
            inp = in.nextLine().strip();
            while (!(isLegalChoice(2, inp) || inp.equals("exit"))) {
                System.out.println("choose 1, 2 or type 'exit'");
                inp = in.nextLine().strip();
            }
            if (inp.equals("2")) {
                continue;
            }else break;
        }
    }

    private void chooseData(String choose){
        switch (choose) {
            case ("1"): {
                System.out.println(this.fc.getAreasData());
                break;
            }
            case ("2"): {
                System.out.println(this.fc.getDriversData());
                break;
            }
            case ("3"): {
                System.out.println(this.fc.getTrucks());
                break;
            }
            case ("4"): {
                System.out.println(this.fc.getTasks());
                break;
            }
            case ("5"): {
                System.out.println(this.fc.getDeliveriesData());
                break;
            }
        }
    }

    private void sendDelivery() {
        Scanner in = new Scanner(System.in);
        DeliveryDTO deliveryDTO = this.chooseDelivery(in, true);
        if (deliveryDTO == null)
            return;
        System.out.println(deliveryDTO + "\n");
        String response = this.insertDepartureWeight(in, deliveryDTO);
        if (response.split(" ").length == 2) {
            System.out.println("The delivery can not be send! the delivery is over weight :(\n" +
                    "Please update this delivery: " + deliveryDTO.getId() + " if you wish to send.");
            System.out.println("press <Enter> to continue");
            in.nextLine();
            deliveryDTO.setDepartureWeight(Integer.parseInt(response.split(" ")[0]));
            this.fc.sendDelivery(deliveryDTO, new Response<>(false));
            return;
        }
        deliveryDTO.setDepartureWeight(Integer.parseInt(response));
        System.out.println("Send Delivery? y/n");
        System.out.println(deliveryDTO + "\n");
        String inp = in.nextLine();
        if (inp.equals("y")){
            this.fc.sendDelivery(deliveryDTO,new Response<>(true));
        }
        return;


    }

    private void updateDelivery() {
//        System.out.println("Choose delivery to update");
        Scanner in = new Scanner(System.in);
        DeliveryDTO chosen = chooseDelivery(in, true);
        if (chosen == null)
            return;
        String delID = chosen.getId();
        if (chosen.equals("")){
            System.out.println("there are no updatable deliveries in the system, press <Enter> to continue");
            in.nextLine();
            return;
        }
        chooseFieldToUpdate(in, delID);
    }

    private void chooseFieldToUpdate(Scanner in, String oldChosenId) {
        String inp = "";
        DeliveryDTO delDTO = fc.getDeliveryById(oldChosenId);
        while (!(inp.equals("7") || inp.equals("exit"))) {
            System.out.println("Delivery " + delDTO.getId() +
                    "\nchoose which field you would like to update:");
            System.out.println(" 1) date - " + delDTO.getDate());
            System.out.println(" 2) time of departure - " + delDTO.getTimeOfDeparture());
            System.out.println(" 3) truck - " + delDTO.getTruckNumber());
            System.out.println(" 4) driver - " + delDTO.getDriverName());
//            System.out.println(" 4) departure weight - " + delDTO.getDepartureWeight());
            System.out.println(" 5) origin - " + delDTO.getOrigin());
            String destPrint = "";
            for(TaskDTO tdto:delDTO.getDestinations())
                destPrint += "\n" +tdto.toString("\t\t");
            System.out.println(" 6) destinations and tasks - " + destPrint);
            System.out.println(" 7) continue");
            inp = in.nextLine();
            while (!isLegalChoice(7, inp) && !inp.equals("exit")) {
                inp = in.nextLine();
            }
            switch (inp) {
                case ("1"): {
                    delDTO.setDate(insertDate(in).getData());
                    break;
                }
                case ("2"): {
                    delDTO.setTimeOfDeparture(insertTimeOfDeparture(in, delDTO.getDate(), delDTO.getDestinations()).getData());
                    break;
                }
                case ("3"): {
                    delDTO.setTruckNumber(chooseTruck(in).getId());
                    break;
                }
                case ("4"): {
                    DriverDTO driver = chooseDriver(in, fc.getTruckByDelivery(delDTO), delDTO.getDate(), delDTO.getTimeOfDeparture());
                    if (driver == null)
                        break;
                    delDTO.setDriverName(driver.getEmployeeName());
                    break;
                }
//                case ("5"): {
//                    delDTO.setDepartureWeight(Integer.parseInt(insertDepartureWeight(in)));
//                    break;
//                }
                case ("5"): {
                    delDTO.setOrigin(chooseLocation(in));
                    break;
                }
                case ("6"): {
                    delDTO.setDestinations(handleUpdateDeliveryTasks(in, delDTO));
//                    delDTO.setDestinations(insertTasksToDelivery(in));
                    break;
                }
            }
        }
        if (inp.equals("exit"))
            return;
        DeliveryDTO ret = fc.updateDelivery(delDTO, oldChosenId); // delDTO is the old one but with the updates
        System.out.println("the new delivery id is: "+ ret.getId());
        System.out.println("click <Enter> to continue");
        in.nextLine();
    }

    private ArrayList<TaskDTO> handleUpdateDeliveryTasks(Scanner in, DeliveryDTO delDTO) {
        System.out.println(" 1) remove task from the list\n 2) add task to the list");
        String inp = in.nextLine();
        while (!(isLegalChoice(2,inp) || inp.equals("exit"))){
            System.out.println("choose 1, 2 or type 'exit'");
            inp = in.nextLine();
            }
        ArrayList<TaskDTO> ar = null;
        if (inp.equals("2")){
            ar = insertTasksToDelivery(in, delDTO.getDestinations());
            ar.addAll(delDTO.getDestinations());
//            delDTO.setDestinations(ar);
        }
        if (inp.equals("1")){
            ar = removeTasksFromDelivery(in, delDTO);
        }
        return ar;
    }

    private ArrayList<TaskDTO> removeTasksFromDelivery(Scanner in, DeliveryDTO delDTO) {
        ArrayList<TaskDTO> allTasks = delDTO.getDestinations();
        String inp = "";
        System.out.println("attention !\nevery task you removed will be completely deleted ! ! !");
        do {
            if (allTasks.size() == 0) {
                System.out.println("there are no updatable deliveries in the system\npress <Enter> to continue");
                in.nextLine();
                return allTasks;
            }
            do {
                System.out.println("choose a task to remove: ");
                System.out.println(" 0) continue");
                for (int i = 1; i <= allTasks.size(); i++) {
                    System.out.println(" "+i + ") " + allTasks.get(i - 1));
                }
                inp = in.nextLine();
            } while (!isLegalChoice(allTasks.size(), inp) && !inp.equals("exit") && !inp.equals("0"));
            if (!inp.equals("0"))
                allTasks.remove(Integer.parseInt(inp) - 1);
        }while (!inp.equals("0") && !inp.equals("exit"));
//        inp = in.nextLine();
//        while (!"yn".contains(inp)){inp = in.nextLine();}
//        if (inp.equals("n"))
//            return delDTO.getDestinations();
        return allTasks;
    }

    private DeliveryDTO chooseDelivery(Scanner in, boolean allDeliveries) {
        String inp = "";
        ArrayList<DeliveryDTO> deliveries;
        if (!allDeliveries)
            deliveries = fc.getUpdatableDeliveries();
        else
            deliveries = fc.getAllAppendingDeliveries();
        if (deliveries.size() == 0){
            System.out.println("there are no appending deliveries in the system\npress <Enter> to continue");
            in.nextLine();
            return null;
        }
        do {
            System.out.println("choose a delivery: ");
            for (int i = 1; i <= deliveries.size(); i++) {
                System.out.println(i + ") " + deliveries.get(i - 1).getId() + " "  + deliveries.get(i - 1).getDate());
            }
            inp = in.nextLine();
        } while (!isLegalChoice(deliveries.size(), inp) && !inp.equals("exit"));
        if (inp.equals("exit"))
            return null;
        return deliveries.get(Integer.parseInt(inp)-1);
    }

    private void addNewDriver() {
        System.out.println("this pitcher isn't available in the current non-integrated module's version\npress <Enter> to continue");
        new Scanner(System.in).nextLine();
    }

    public boolean isLegalDate(String date) {
        try {
            // ResolverStyle.STRICT for 30, 31 days checking, and also leap year.
            LocalDate.parse(date,
                    DateTimeFormatter.ofPattern("d-M-uu")
                            .withResolverStyle(ResolverStyle.STRICT)
            );
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private Response<String> insertDate(Scanner in) {
        String date = "";
        do {
            System.out.println("Insert date: dd-mm-yy");
            date = in.nextLine();
        } while (!isLegalDate(date) && !date.equals("exit"));
        return new Response<>(date);
    }

    public Response<String> insertTimeOfDeparture(Scanner in, String date, ArrayList<TaskDTO> tasks) {
        String timeOfDeparture = "";
        boolean f_legal = true;
        do {
            System.out.println("Insert time of departure: hh:mm");
            timeOfDeparture = in.nextLine();
            f_legal = isLegalTime(timeOfDeparture);
            if (!f_legal)
                continue;
            if (fc.checkIfStoreKeeperNeeded(tasks).getData()) {
                ResponseT<Boolean> res = fc.isLegalDepartureTime(timeOfDeparture, date);
                if (res.isErrorOccured()) {
                    System.out.println("error during check for storekeeper appearance:");
                    System.out.println(res.getErrorMessage());
                    f_legal = false;
                }
                else
                    f_legal = res.getValue();
                if (!f_legal)
                    System.out.println("there isn't any store keeper assigned on given date and time: " + date + " " + timeOfDeparture);
            }
        } while (!f_legal && !timeOfDeparture.equals("exit"));
        return new Response<>(timeOfDeparture);
    }

    private TruckDTO chooseTruck(Scanner in) {
        String inp = "";
        ArrayList<TruckDTO> truckLst = fc.getTrucks();
        do {
            System.out.println("choose a truck for the delivery: ");
            for (int i = 1; i <= truckLst.size(); i++) {
                System.out.println(i + ") " + truckLst.get(i - 1));
            }
            inp = in.nextLine();
        } while (!isLegalChoice(truckLst.size(), inp) && !inp.equals("exit"));
        if (inp.equals("exit"))
            return null;
        return truckLst.get(Integer.parseInt(inp)-1);
    }

    private boolean isLegalChoice(int size, String input) {
        int p = -1;
        try {
            p = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return false;
        }
        if (p <= 0 || p > size)
            return false;
        return true;
    }

    public void createNewDelivery() {
        Scanner in = new Scanner(System.in);

        LocationDTO originLocation = chooseLocation(in);
        if (originLocation == null)
            return;

        ArrayList<TaskDTO> arrTask = insertTasksToDelivery(in, new ArrayList<>());
        if (arrTask == null)
            return;

        String date = insertDate(in).getData();
        if (date.equals("exit"))
            return;

        String timeOfDeparture = insertTimeOfDeparture(in, date, arrTask).getData(); // also checking new logic - existance of storage manager:
        if (timeOfDeparture.equals("exit"))
            return;


        TruckDTO trdto = chooseTruck(in);
        if (trdto == null)
            return;
        String truck = trdto.getId();

        DriverDTO drdto = chooseDriver(in, trdto, date, timeOfDeparture);
        if (drdto == null)
            return;
        String driverName = drdto.getEmployeeName();

//        String departureWeight = insertDepartureWeight(in);
//        if (departureWeight.equals("exit"))
//            return;
//        int departureWeightInt = Integer.parseInt(departureWeight);

//      moved choose tasks to above due to employee module integration logic

        // summery:
        DeliveryDTO creation = new DeliveryDTO(date, timeOfDeparture, truck.split(" ")[0], driverName, 0, "", originLocation, arrTask);
        System.out.println(creation);
        String approve = "";
        System.out.println("Create the delivery? y/n");
        while (!(approve.equals("y") || approve.equals("n")))
            approve = in.nextLine();
        if (approve.equals("y")) {
            fc.createFullDelivery(creation);
        }
    }


    private ArrayList<TaskDTO> insertTasksToDelivery(Scanner in, ArrayList<TaskDTO> alreadyIn) {
        String op = "";
        ArrayList<TaskDTO> arrTask = new ArrayList<>();
        ArrayList<TaskDTO> allTasks = new ArrayList<>();
        for (TaskDTO t1 : fc.getTasks()){
            boolean isInAllTasks = false;
            for (TaskDTO t2 : alreadyIn){
                if (t2.getId() == t1.getId()) {
                    isInAllTasks = true;
                    break;
                }
            }
            if (!isInAllTasks){
                allTasks.add(t1);
            }
        }
        while (!(op.equals("exit") || op.equals("3"))){
            System.out.println("Insert Task\n 1) create new task\n 2) choose existed task\n 3) continue");
            op = in.nextLine();
            if (op.equals("1")) {
                TaskDTO t = addNewTask();
                if (t!=null)
                    arrTask.add(t);
            }else if (op.equals("2")) {
                if (fc.getTasks().size() == 0) {
                    System.out.println("no appending tasks in the system. please insert before choosing this option");
                    op = "not good";
                    continue;
                }
                TaskDTO chosen = chooseTask(in, allTasks);
                if (chosen != null) {
                    arrTask.add(chosen);
                    allTasks.remove(chosen);
                }
            }
        }
        if (op.equals("exit"))
            return null;
        return arrTask;
    }

    private TaskDTO addNewTask(){
        ArrayList<String> taskSTR = this.userTaskCreator();
        if (taskSTR == null)
            return null;
        TaskDTO taskDTO = new TaskDTO();
        HashMap<String, Integer> hashOfProduct = this.str2Hash(taskSTR.get(0));
        taskDTO.setListOfProduct(hashOfProduct);
        String loadingOrUnloading = taskSTR.get(1);
        taskDTO.setLoadingOrUnloading(loadingOrUnloading);
        Response<String> addresss = new Response<>(taskSTR.get(2));
        LocationDTO Destination = fc.getLocationByAddress(addresss);
        TaskDTO task = new TaskDTO(hashOfProduct,loadingOrUnloading,Destination);
        String id = this.fc.addTask(task).getId();
        task.setId(id);
        return task;
    }

    private TaskDTO chooseTask(Scanner in, ArrayList<TaskDTO> tasksLst) {
        String inp = "";
        do {
            if(tasksLst.size() == 0){
                System.out.println("no tasks available, insert new task");
                inp = "exit";
                continue;
            }
            System.out.println("choose a task for the delivery: ");
            for (int i = 1; i <= tasksLst.size(); i++) {
                System.out.println(i + ") " + tasksLst.get(i - 1));
            }
            inp = in.nextLine();
        } while (!isLegalChoice(tasksLst.size(), inp) && !inp.equals("exit"));
        if (inp.equals("exit"))
            return null;
        return  tasksLst.get(Integer.parseInt(inp)-1);
    }


    public HashMap<String, Integer> str2Hash(String strOfProduct){
        String[] keyValuePairs = strOfProduct.split(",");              //split the string to create key-value pairs
        HashMap<String,Integer> map = new HashMap<>();

        for(String pair : keyValuePairs)                        //iterate over the pairs
        {
            String[] entry = pair.split(":");                   //split the pairs to get key and value

            map.put(entry[0].trim(), Integer.parseInt(entry[1].trim()));          //add them to the hashmap and trim whitespaces, parse to int
        }
        return map;
    }


    private LocationDTO chooseLocation(Scanner in) {
        String inp = "";
        String[] arrayInput;
        HashMap<String, ArrayList<LocationDTO>> locationsByAreas = fc.getLocationsByAreas();
        HashMap<String, String> joinNumberToArea = new HashMap<>();
        boolean legal = false;
        do {
            System.out.println("choose the origin location for the delivery: <area number> <location number>");
            int i = 0;
            for (String a : locationsByAreas.keySet()) {
                ArrayList<LocationDTO> locations = locationsByAreas.get(a);
                System.out.println(++i + ") " + a);
                joinNumberToArea.put(Integer.toString(i), a);
                for (int j = 1; j <= locations.size(); j++) {
                    System.out.println("\t" + j + ") " + locations.get(j - 1));
                }
            }
            inp = in.nextLine();
            arrayInput = inp.split(" ");
            if (arrayInput.length == 2) {
                if (isLegalChoice(locationsByAreas.size(), arrayInput[0])) {
                    if (isLegalChoice(locationsByAreas.get(joinNumberToArea.get(arrayInput[0])).size(), arrayInput[1]))
                        legal = true;
                }
            }
        } while (!legal);
        return locationsByAreas.get(joinNumberToArea.get(arrayInput[0])).get(Integer.valueOf(arrayInput[1]) - 1);
    }


    private boolean isLegalFloat(String input) {
        try {
            Long.valueOf(input);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private String insertDepartureWeight(Scanner in, DeliveryDTO deliveryDTO) {
        String input = "";
        do {
            System.out.println("Insert legal departure weight:");
            input = in.nextLine();
        } while (!isLegalFloat(input));
        String response = this.fc.isLegalDepartureWeight(input, deliveryDTO).getData();
        return response;
    }

    private DriverDTO chooseDriver(Scanner in, TruckDTO ride, String date, String timeOfDeparture) {
        String inp = "";
        ArrayList<DriverDTO> driversLst = fc.getDriversToTruckAndTime(ride, date, timeOfDeparture);
        do {
            if (driversLst.size() == 0){
                System.out.println("there are no available drivers assigned to shift on the given \ntime and date" +
                        " that can drive the chosen truck\npress <Enter> to exit the delivery creator.");
                inp = "exit";
                in.nextLine();
                continue;
            }
            System.out.println("choose a driver for the delivery: ");
            for (int i = 1; i <= driversLst.size(); i++) {
                System.out.println(i + ") " + driversLst.get(i - 1).getEmployeeName()+" "+driversLst.get(i - 1).getLicenseType());
            }
            inp = in.nextLine();
        } while (!isLegalChoice(driversLst.size(), inp) && !inp.equals("exit"));
        if (inp.equals("exit"))
            return null;
        return driversLst.get(Integer.parseInt(inp) - 1);
    }

    private boolean isLegalTime(String timeOfDeparture) {
        String[] spl = timeOfDeparture.split(":");
        if (spl.length != 2)
            return false;
        if (!isLegalFloat(spl[0]) || !isLegalFloat(spl[1]))
            return false;
        int hour = Integer.parseInt(spl[0]);
        int minutes = Integer.parseInt(spl[1]);
        if (hour > 24 || hour < 0 || minutes > 59 || minutes < 0)
            return false;
        return true;
    }

    public ArrayList<String> addNewLocationHelper() {
        ArrayList<String> arr = new ArrayList<>();
        Scanner in = new Scanner(System.in);
        System.out.println("Insert new location:");
        System.out.println("address:");
        String address = in.nextLine();
        if (address.equals("exit"))
            return null;
        arr.add(address);
        String phoneNum = "";
        while(!isLegalFloat(phoneNum) || phoneNum.length() != 10) {
            System.out.println("phone number: <10 digits>");
            phoneNum = in.nextLine();
            if (phoneNum.equals("exit"))
                break;
        }
        arr.add(phoneNum);
        if (phoneNum.equals("exit"))
            return null;
        System.out.println("contact name:");
        String contactName = in.nextLine();
        if (contactName.equals("exit"))
            return null;
        arr.add(contactName);
        return arr;
    }

    public void addNewLocation(){
        Scanner in = new Scanner(System.in);
        ArrayList<AreaDTO> areas;
        String inp = "";
        String areaName = "";
        ArrayList<String> arr = this.addNewLocationHelper();
        if (arr == null)
            return;
        do {
            System.out.println("Choose an area name for the location:");
            areas = this.fc.getAreas();
            for (int i = 1; i <= areas.size(); i++){
                System.out.println(i + ") " + areas.get(i - 1));
            }
            inp = in.nextLine();
        } while (!isLegalChoice(areas.size(), inp) && !inp.equals("exit"));
        if (inp.equals("exit"))
            return;
        LocationDTO locationDTO = new LocationDTO(arr.get(0), arr.get(1), arr.get(2));
        if (!this.fc.addLocation(areas.get(Integer.parseInt(inp) - 1), locationDTO).getData())
            System.out.println("wrong input! try again");
    }

    public ArrayList<String> userTaskCreator() {
        ArrayList<String> arr = new ArrayList<>();
        Scanner in = new Scanner(System.in);
        System.out.println("Insert list of product:");
        System.out.println("Please wright in the EXACT format:");
        System.out.println("<Product>:<Quantity>");
        System.out.println("For example: Banana:40,bread:30 ...");
        String productStr = "";
        boolean flag = true;
        do{
            if (!flag)
                System.out.println("<Product>:<Quantity>");
            flag = true;
            productStr = in.nextLine().strip();
            String[] allProducts = productStr.split(",");
            for(String p : allProducts){
                String[] spl = p.split(":");
                if (spl.length != 2) {
                    flag = false;
                    continue;
                }
                if(!isLegalFloat(spl[1])){
                    flag = false;
                    continue;
                }
            }
        } while (!(flag || productStr.equals("exit")));
        if (productStr.equals("exit"))
            return null;
        arr.add(productStr);
        System.out.println("For loading press 1");
        System.out.println("For unloading press 2");
        String op = in.nextLine();
        while (!(op.equals("1") || op.equals("2") || op.equals("exit"))) {
            System.out.println("Please choose one of the option 1, 2 or exit");
            op = in.nextLine();
        }

        String loadOrUnload = "";
        switch (op) {
            case ("1"): {
                loadOrUnload = "loading";
                break;
            }
            case ("2"): {
                loadOrUnload = "unloading";
                break;
            }
        }
        arr.add(loadOrUnload);


//        System.out.println("choose location:");
//        ArrayList<String> originLocation = chooseLocation(in);
//        arr.add(originLocation.get(0));
//        arr.add(originLocation.get(1));
//        arr.add(originLocation.get(2));
        String destination = chooseLocation(in).getAddress();
        arr.add(destination);

    // arr = [list of product, un\loading, location.address, location.phone number, location.contact name]
        return arr;
    }

    public void addNewTrack() {
        Scanner in = new Scanner(System.in);
        String truckNumber = "";
        do {
            System.out.println("insert truck number: xx-xxx-xx || xxx-xx-xxx");
            truckNumber = in.nextLine();
        } while (!(isLegalTruck(truckNumber) || truckNumber.equals("exit")));
        if (truckNumber.equals("exit"))
            return;

        String truckModel = "";
        do {
            System.out.println("insert truck model:");
            truckModel = in.nextLine();
        } while (!(truckModel.length()  != 0 || truckModel.equals("exit")));
        if (truckModel.equals("exit"))
            return;

        int maxWeight = 0;
        String input = "";
        do {
            System.out.println("insert truck's max weight: kg");
            input = in.nextLine();
        } while (!(isLegalFloat(input) || input.equals("exit")));
        if (input.equals("exit"))
            return;
        maxWeight = Integer.parseInt(input);

        int truckWeight = 0;
        do {
            System.out.println("insert truck's weight");
            input = in.nextLine();
        } while (!(isLegalFloat(input) || input.equals("exit")));
        if (input.equals("exit"))
            return;
        truckWeight = Integer.parseInt(input);
        TruckDTO truckDTO = new TruckDTO(truckNumber, truckModel, maxWeight, truckWeight);
        fc.addTruck(truckDTO);
    }

    private boolean isLegalTruck(String input) {
        input = input.replace("-", "");
        if (input.length() != 7 && input.length() != 8)
            return false;
        if (!isLegalFloat(input))
            return false;
        if (fc.containsTruck(input))
            return false;
        return true;
    }

    public FacadeController getFacade() {
        return fc;
    }

    public void addNewArea(){
        Scanner in = new Scanner(System.in);
        String areaName = "";
        do {
            System.out.println("Insert area name:");
            areaName = in.nextLine();
        } while (this.fc.containsArea(areaName) && !areaName.equals("exit"));
        if (areaName.equals("exit"))
            return;
        AreaDTO areaDTO = new AreaDTO(areaName);
        this.fc.addNewArea(areaDTO);
    }


}
