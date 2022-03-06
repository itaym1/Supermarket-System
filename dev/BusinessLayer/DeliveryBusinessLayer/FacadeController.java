package BusinessLayer.DeliveryBusinessLayer;

import BusinessLayer.EmployeesBuisnessLayer.Employee;
import DataAccessLayer.DeliveryDataAccessLayer.DTO.*;
import BusinessLayer.EmployeesBuisnessLayer.ShiftController;
import serviceObjects.ResponseT;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.stream.Stream;

public class FacadeController {
    DeliveryController dec;
    DriverController drc;
    AreaController arc;
    TaskController tac;
    TruckController trc;
    BusinessLayer.EmployeesBuisnessLayer.FacadeController efc;
    String origin;

    static FacadeController instance;

    public FacadeController(){
        dec = new DeliveryController();
        drc = new DriverController();
        arc = new AreaController();
        tac = new TaskController();
        trc = new TruckController();
        efc = BusinessLayer.EmployeesBuisnessLayer.FacadeController.getInstance();
        origin = "Ben-Gurion 1, Beer Sheva";
    }

    public static FacadeController getInstance() {
        if (instance == null)
            instance = new FacadeController();
        return instance;
    }

    public Response<String> toStringResponse() {
        return new Response<>("\n\n-----  System Current State:  -----\n"+
                "\nDeliveries=\n" + dec.getDeliveriesToSend() +
                "\nDrivers=\n" + drc.toString("\t") + // TODO: need to add this driver from other module
                "\nAreas=\n" + arc.getAreas() +
                "\nFree Tasks=\n" + tac.getTasks() +
//                "\nTrucks=\n" + trc.toString("\t")+"\n");
                "\nTrucks=\n" + trc.getTrucks());
    }

    @Override
    public String toString() {
        return "Deliveries=" + dec +
                "\nDrivers=" + drc +
                "\nAreas=" + arc +
                "\nTasks=" + tac +
                "\nTrucks=" + trc +
                '}';
    }

    // - Area -
    public void addNewArea(AreaDTO areaDTO){
        this.arc.addNewArea(areaDTO);

    }

    public boolean containsArea(String areaName){
        return this.arc.containsArea(areaName);
    }

    public Response<Boolean> addLocation(AreaDTO areaDTO, LocationDTO locationDTO){
        return arc.addLocation(areaDTO, locationDTO);
    }

    public ArrayList<AreaDTO> getAreas() {
        return arc.getAreas();
    }

    public LocationDTO getLocationByAddress(Response<String> address){
        return new LocationDTO(arc.getLocation(address.getData()));
    }

    // - Task -
    public String addTask(HashMap<String, Integer> listOfProduct, String loadingOrUnloading,
                        String Destination){
        Location destination = arc.getLocation(Destination);
        return this.tac.addTask(listOfProduct, loadingOrUnloading, destination);
    }

    public TaskDTO addTask(TaskDTO t){
        if (t.getId() == null){
            t.setId(addTask(t.getListOfProduct(),t.getLoadingOrUnloading(),t.getDestination().getAddress()));
            return t;
        }
        return null;

    }

    // - Truck -
    public void addTruck(TruckDTO truckDTO){
        this.trc.addTruck(truckDTO);
    }

    public boolean containsTruck(String id){
        return trc.containsTruck(id);
    }

    public ArrayList<TruckDTO> getTrucks(){
        return trc.getTrucks();
    }

    // - Driver -
    public boolean containsDriver(String name){
        return this.drc.containsDriver(name);
    }

    // - Delivery -
    public DeliveryDTO getDeliveryById(String id){
        return new DeliveryDTO(this.dec.getDeliveryById(id));
    }

    public void sendDelivery(DeliveryDTO deliveryDTO, Response<Boolean> storeIt){
        this.dec.sendDelivery(deliveryDTO, storeIt.getData());
        // store to appendingTask(taskId)
        this.tac.storeAppendingTasks(deliveryDTO.getDestinations());
        //  TODO: check if work
    }

    public HashMap<Integer, Integer> getOrder(){
        return this.tac.getOrder();
    }



//    public ArrayList<tmpEmployee> getAllDriverEmployees(){
//        ArrayList<tmpEmployee> ret = new ArrayList<>();
//        tmpEmployee emp1 = new tmpEmployee("yanay the sunny",15000);
//        tmpEmployee emp2 = new tmpEmployee("nitzan the lary", 20000);
//        tmpEmployee emp3 = new tmpEmployee("shaul the shauly", 15000);
//        tmpEmployee emp4 = new tmpEmployee("david the davidy", 20000);
//        ret.add(emp1);
//        ret.add(emp2);
//        ret.add(emp3);
//        ret.add(emp4);
//        return ret;
//    }


    public void tempAddDriver(ArrayList<tmpEmployee> arr){
        this.drc.tmpAddDriver(arr);
    }


    public HashMap<String, ArrayList<LocationDTO>> getLocationsByAreas() {
        HashMap<String, ArrayList<LocationDTO>> ret = arc.getLocationsByArea();
        return ret;
    }


    public DeliveryDTO createFullDelivery(DeliveryDTO del){
        ArrayList<Task> tasks = new ArrayList<>();
        String delID = dec.getNewDeliveryID();
        for (TaskDTO t: del.getDestinations())
            tasks.add(tac.getAndRemoveTaskById(t.getId(), delID));
        del.setId(delID);
        dec.createFullDelivery(delID, del.getDate(),del.getTimeOfDeparture(),del.getTruckNumber(),del.getDriverName(),del.getDepartureWeight(),del.getModification(), arc.getLocation(del.getOrigin().getAddress()),tasks);
        return del;
    }

    public ArrayList<TaskDTO> getTasks() {
        ArrayList<TaskDTO> ret = new ArrayList<>();
        for (Task t : tac.getTasks()) {
            ret.add(new TaskDTO(t));
        }
        return ret;
    }

    public ArrayList<DeliveryDTO> getUpdatableDeliveries() {
        ArrayList<DeliveryDTO> ret = new ArrayList<>();
        for (Delivery d:dec.getUpdatableDeliveries())
            ret.add(new DeliveryDTO(d));
        return ret;
    }

    public DeliveryDTO updateDelivery(DeliveryDTO newDel, String oldDelId) {
        String nextID = dec.getNextDeliveryID();
        ArrayList<Task> tasks = new ArrayList<>();
        for(TaskDTO td: newDel.getDestinations()){
            Task t = dec.getTasksFromDelivery(td.getId(), oldDelId);
            if (t==null)
                t = tac.getAndRemoveTaskById(td.getId(), nextID);
            else{
                tac.updateTaskDelID(t, nextID);
            }
            tasks.add(t);
        }
        Location orig = arc.getLocation(newDel.getOrigin().getAddress());
        newDel.setId(null);
        Delivery newD = dec.createNewDelivery(newDel, orig, tasks);

        return new DeliveryDTO(dec.updateDelivery(newD, oldDelId));
    }

    public Response<String> isLegalDepartureWeight(String input, DeliveryDTO deliveryDTO) {
        if (trc.getTruckByID(deliveryDTO.getTruckNumber()).getMaxWeight() <  Integer.parseInt(input))
            return new Response(input + " Exceeded");
        return new Response(input);
    }

    public ArrayList<AreaDTO> getAreasData() {
        return this.arc.getAreas();
    }

    public ArrayList<DriverDTO> getDriversData() {
        return this.drc.getDriversData(); // todo - integration!
    }

    public LocalDate parseToLocalDate(String date){
//        String[] d = date.split("-");
//        return LocalDate.of(Integer.parseInt(d[2]) + 2000, Integer.parseInt(d[1]), Integer.parseInt(d[0]));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-M-uu");
        return LocalDate.parse(date, formatter);
    }

    public LocalTime parseToLocalTime(String timeOfDeparture){
        String[] t = timeOfDeparture.split(":");
        return LocalTime.of(Integer.parseInt(t[0]), Integer.parseInt(t[1]), 0);
    }

    public ArrayList<DriverDTO> getDriversToTruckAndTime(TruckDTO ride, String date, String timeOfDeparture) {
        ArrayList<DriverDTO> ret = new ArrayList<>();
        LocalDate localDate = parseToLocalDate(date);
        LocalTime localTime = parseToLocalTime(timeOfDeparture);
        ResponseT<List <Employee>> drivers = ShiftController.getInstance().getAllAssignedDrivers(localDate, localTime);
        if (drivers.getValue() == null)
            return ret;
        for (BusinessLayer.EmployeesBuisnessLayer.Employee driver : drivers.getValue()){
            if (driver.getLicenceType().getValue() >= ride.getTruckWeight())
                ret.add(new DriverDTO(driver.getLicenceType().getValue(),driver.getName().getValue()));
        }
        return ret;
    }



    public TruckDTO getTruckByDelivery(DeliveryDTO ddto) {
        return trc. getTruckByID(ddto.getTruckNumber());
    }

    public ArrayList<DeliveryDTO> getAllAppendingDeliveries() {
        ArrayList<DeliveryDTO> ret = new ArrayList<>();
        for (Delivery d:dec.getDeliveries().values())
            ret.add(new DeliveryDTO(d));
        return ret;
    }

    public ArrayList<DriverDTO> getAllDrivers() {
        ArrayList<DriverDTO> arr = new ArrayList<>();
        for (Driver driver : drc.getDrivers()){
            arr.add(new DriverDTO(driver));
        }
        return arr;
    }

    public ArrayList<DeliveryDTO> getDeliveriesData() {
        return dec.getDeliveriesData();
    }

    public ResponseT<Boolean> isLegalDepartureTime(String timeOfDeparture, String date) {
        BusinessLayer.EmployeesBuisnessLayer.FacadeController efc = BusinessLayer.EmployeesBuisnessLayer.FacadeController.getInstance();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-M-uu");
        LocalDate ld = LocalDate.parse(date, formatter);
        LocalTime lt = LocalTime.parse(timeOfDeparture);
        return efc.isStorekeeperAssigned(ld,lt);
    }

    public Response<Boolean> checkIfStoreKeeperNeeded(ArrayList<TaskDTO> tasks) {
        for (TaskDTO t : tasks){
            if (t.getLoadingOrUnloading().equals("loading"))
                    return new Response<>(true);
        }
        return new Response<>(false);
    }

    /**
     * module supplier call this method for create auto periodic order
     * @param intIntLstOfProducts - <makat, quantity, ?cost?, ?another makat?, ect..>
     * @param sendToAddress - <address>
     * @param sendToPhoneNumber - <phone>
     * @param sendToContactName - <name>
     * @param loadingOrUnloading - loading if we get products from supplier, unloading if we drop shipment  (Farjun's explanation)
     * @param daysOfSupplying - list of LocalDate
     * @return
     */
    public Response<Boolean> assignAutoTask(HashMap<Integer, Integer> intIntLstOfProducts, String sendToAddress, String sendToPhoneNumber, String sendToContactName, String loadingOrUnloading, ArrayList<LocalDate> daysOfSupplying){
        Location location = arc.getLocation(sendToAddress); // todo: check what in the destinationDetails
        if (location == null){
            LocationDTO newLoc = new LocationDTO(sendToAddress, sendToPhoneNumber, sendToContactName);
            ArrayList<AreaDTO> allAreas = arc.getAreas();
            AreaDTO targetArea = null;
            if (allAreas == null || allAreas.isEmpty()){
                AreaDTO south = new AreaDTO("South");
                arc.addNewArea(south);
                targetArea = arc.getAreas().get(0);
            }else{
                targetArea = allAreas.get(0);
            }
            arc.addLocation(targetArea, newLoc);
            location = arc.getLocation(sendToAddress);
        }
        Map<String , Integer> lstOfProducts =
                intIntLstOfProducts.entrySet().stream().collect(Collectors.toMap(
                        entry -> Integer.toString(entry.getKey()),
                        entry -> entry.getValue())
                );
//        HashMap<String , Integer> ourLst = convertLop2Str(lstOfProducts);
        String taskId = tac.addTask((HashMap<String, Integer>) lstOfProducts, loadingOrUnloading, location);
        for (LocalDate date : daysOfSupplying){
            DeliveryDTO deliveryDTO = dec.getDeliveriesByDate(date);
            // if there is a delivery that send in the same day
            if (deliveryDTO != null){
                ArrayList<TaskDTO> arr = deliveryDTO.getDestinations();
                arr.add(new TaskDTO(tac.getTaskById(taskId)));
                deliveryDTO.setDestinations(deliveryDTO.getDestinations());
                updateDelivery(deliveryDTO, deliveryDTO.getId());
                return new Response(true);
            }
            // if not we create new delivery for it
            Object[] constraints = checkConstraintsDelivery(date);
            if (constraints != null){
                Location origin = arc.getLocation(this.origin);
                ArrayList<Task> arrTask = new ArrayList<>(Collections.singletonList(tac.getTaskById(taskId)));

                Delivery delivery = dec.createNewDelivery(new DeliveryDTO(date.toString(), ((LocalTime) constraints[2]).format(DateTimeFormatter.ofPattern("d-M-uu")),
                        ((TruckDTO) constraints[1]).getId(), ((DriverDTO) constraints[0]).getEmployeeName(),
                        0, "",new LocationDTO(origin) ,
                        new ArrayList<TaskDTO>(Collections.singletonList(new TaskDTO(tac.getTaskById(taskId))))),
                        origin, arrTask
                        );
//                for(Task task : arrTask)
//                    tac.getAndRemoveTaskById(taskId)
                tac.getAndRemoveTaskById(taskId, delivery.getID());
                return new Response<>(true);
            }

        }
        efc.addNotification("0","cannot assign task " + taskId);
        return new Response<>(false);
    }


    public Object[] checkConstraintsDelivery(LocalDate date) {
        LocalTime MShift = LocalTime.parse("10:00");
        LocalTime EShift = LocalTime.parse("16:00");
        LocalTime[] possibleShifts = new LocalTime[]{MShift, EShift};
        for (LocalTime shift : possibleShifts){
            boolean thereIsStorekeeper = efc.isStorekeeperAssigned(date, shift).getValue();
            if (!thereIsStorekeeper)
                continue;
            ArrayList<TruckDTO> truckLst = getTrucks();
            for (TruckDTO truckDTO : truckLst){
                ArrayList<DriverDTO> driverDTOS = getAllDrivers(date, shift, truckDTO);
                if (!driverDTOS.isEmpty()){
                    Object[] arr = new Object[3];
                    arr[0] = driverDTOS.get(0);
                    arr[1] = truckDTO;
                    arr[2] = shift;
                    return arr;
                }

            }

        }
        return null;
    }

    public ArrayList<DriverDTO> getAllDrivers(LocalDate date, LocalTime shift, TruckDTO ride){
        ArrayList<DriverDTO> ret = new ArrayList<>();
        ResponseT<List <Employee>> drivers = ShiftController.getInstance().getAllAssignedDrivers(date, shift);

        for (BusinessLayer.EmployeesBuisnessLayer.Employee driver : drivers.getValue()){
            if (driver.getLicenceType().getValue() >= ride.getTruckWeight())
                ret.add(new DriverDTO(driver.getLicenceType().getValue(),driver.getName().getValue()));
        }
        return ret;
    }
}
