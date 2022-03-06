package BusinessLayer.DeliveryBusinessLayer;

import BusinessLayer.EmployeesBuisnessLayer.Employee;
import BusinessLayer.EmployeesBuisnessLayer.ShiftController;
import BusinessLayer.Supplier.FacadeSupplier;
import DataAccessLayer.DeliveryDataAccessLayer.DTO.DeliveryDTO;
import DataAccessLayer.DeliveryDataAccessLayer.DTO.DriverDTO;
import DataAccessLayer.DeliveryDataAccessLayer.DTO.TaskDTO;
import DataAccessLayer.DeliveryDataAccessLayer.DTO.TruckDTO;
import DataAccessLayer.DeliveryDataAccessLayer.DeliveryDAO;
import DataAccessLayer.DeliveryDataAccessLayer.Mapper;
import serviceObjects.ResponseT;

import javax.security.auth.callback.Callback;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.lang.reflect.Method;

public class DeliveryController {
    private HashMap<String, Delivery> deliveries; // changes it to deliveries
    private String nextID = "A000";
    private DeliveryDAO dataController;
    private Mapper mapper;

    public DeliveryController(){
        deliveries = new HashMap<>();
        dataController = DeliveryDAO.getInstance();
        mapper = Mapper.getInstance();
        if (mapper.getLastTaskID() != null){
            nextID = mapper.getLastDeliveryID();
            getNewDeliveryID();
        }
        initDeliveries();
    }

    private void initDeliveries() {
        ArrayList<DeliveryDTO> deliveryDTOS = mapper.getDeliveries();
        for (DeliveryDTO deliveryDTO : deliveryDTOS){
            this.deliveries.put(deliveryDTO.getId(), new Delivery(deliveryDTO));
        }
    }

    public HashMap<String, Delivery> getDeliveries() {
        return deliveries;
    }

    public String createFullDelivery(String id, String date, String timeOfDeparture, String truckNumber, String driverName, int departureWeight, String modification, Location origin, ArrayList<Task> destinations){
//        String id = getNewDeliveryID();
        Delivery del = new Delivery(id, date, timeOfDeparture, truckNumber, driverName, departureWeight, modification, origin, destinations);
        deliveries.put(id ,del);
        storeDelivery(del);
        mapper.addDelivery(new DeliveryDTO(del));
        return id;
    }

    public void addDriver(Driver dr, String delID){
        deliveries.get(delID).setDriver(dr);
    }

    public void storeDelivery(Delivery toStore){
        DeliveryDTO deliveryDTO = new DeliveryDTO(toStore);
        this.dataController.storeDelivery(deliveryDTO);
    } // send to database todo

    // TODO: check if the change in the DB is good
    // TODO olso: check whats happened in the sys
    public Delivery updateDelivery(Delivery newDel, String OldDelID){
        Delivery toUpdate = deliveries.remove(OldDelID);
        String olderModification = "- newer "+newDel.getID()+" -";
        String newerModification = "- older "+OldDelID+" -";

        olderModification = toUpdate.addModification(olderModification);
        newerModification = newDel.addModification(newerModification);
        dataController.updateDeliveryModif(toUpdate, olderModification);
        dataController.updateDeliveryModif(newDel, newerModification);
//        storeDelivery(toStore);
//        Delivery newDel = cloneDelivery(toStore);
//        return null;
        return newDel;
    }

    public Delivery cloneDelivery(Delivery oldDel){
        //Delivery clone = OldDel.clone()
        // todo copy all data types
        //oldDel.makeOld(clone.getID()); <letter><num num num> 9999 z999
        // return clone
        return null;
    }

    public void storeDelivery(String delID){} // start with th line - Delivery newDel = updateDelivery(delID); newDel.set

    public void updateDeliveryDate(String delID, String newDate){}

    public void updateDeliveryTime(String delID, String newTime){}

    public void updateDeliveryTruckNumber(String delID, String newTrack){}

    public void updateDeliveryDriver(String delID, String newDriverName){}

    public void updateDeliveryDepartureWeight(String delID, int newWeight){}

    public void updateDeliveryOrigin(String delID, Location newOrig){}

    public void insertDeliveryTasks(String deID, ArrayList<Task> newTasks){} //Delivery newDel = updateDelivery(delID); newDel.set

    public void removeDeliveryTasks(String delID, ArrayList<Task> remTasks){}

    public void replaceDeliveryTasks(String delID, ArrayList<String> TaskToChange, ArrayList<Task> newTask){
        /*
        Delivery updated = updateDelivery(String delID);
        Delivery newDel = cloneDelivery(delID);
        newDel.removeTask
        deliveries[delID].remove
         */
    }

    public String getNewDeliveryID(){
        String ret = nextID;
        String[] splitted = {nextID.substring(0,1), nextID.substring(1)};
        if (splitted[1].equals("999")) {
            int index = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(splitted[0]) + 1;
            try {
                nextID = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".charAt(index) + "000";
            }catch (IndexOutOfBoundsException e){
                System.out.println("overloaded system, resetting delivery ids");
            }
        }
        else{
            nextID = splitted[0] + String.format("%1$03d",Integer.parseInt(splitted[1])+1);
        }
        return ret;
    }

    public String getNextDeliveryID(){
        return nextID;
    }


    public Delivery createNewDelivery(DeliveryDTO newDel, Location origin, ArrayList<Task> destinations){
        if (newDel.getId() == null)
            newDel.setId(getNewDeliveryID());
        Delivery ret = new Delivery(newDel.getId(),newDel.getDate(),newDel.getTimeOfDeparture(),newDel.getTruckNumber(),newDel.getDriverName(),newDel.getDepartureWeight(),newDel.getModification(),origin,destinations);
//        deliveries.put(newDel.getId(), ret); TODO: we need this here? because we say that only in sendDelivery we save
        deliveries.put(ret.getID() ,ret);
        storeDelivery(ret);
        mapper.addDelivery(newDel);
        return ret;
    }

    public void addDate(String delID, String date){
        deliveries.get(delID);
    }

    // TODO: same as the upper todo.. dont think its relevant
    public Delivery getDeliveryById(String id){
        if (!this.deliveries.containsKey(id)){
            throw new InputMismatchException("Delivery dose not exist.");
        }
        return this.deliveries.get(id);
    }

    public void createDelivery(){
        String newId = this.getNewDeliveryID();


    }

    @Override
    public String toString() {
        String ret = "";
        for (Delivery d: deliveries.values()){
            ret += d.toString()+"\n";
        }
        return ret;
//        return "\t" + deliveries +
//                '}';
    }

    public ArrayList<Delivery> getUpdatableDeliveries() {
        ArrayList<Delivery> ret = new ArrayList<>();
        for (Delivery d: deliveries.values()){
            if(d.isUpdatable())
                ret.add(d);
        }
        return ret;
    }

    public LocalDate getCurrentDate(){
        return LocalDate.now();
    }

    public LocalDateTime getCurrentTime(){
        return LocalDateTime.now();
    }

    public Task getTasksFromDelivery(String id, String oldDelId) {
        ArrayList<Task> ta = deliveries.get(oldDelId).getDestinations();
        for (Task t: ta){
            if (t.getId().equals(id))
                return t;
        }
        return null;
    }

    public void sendDelivery(DeliveryDTO deliveryDTO, boolean storeIt) {
        Delivery delivery = this.deliveries.get(deliveryDTO.getId());
        delivery.setDepartureWeight(deliveryDTO.getDepartureWeight());
        if (storeIt) {
            dataController.updateDeliveryDW(deliveryDTO);
            this.deliveries.remove(deliveryDTO.getId());
            mapper.removeDelivery(deliveryDTO);
        }
    }

    public ArrayList<DeliveryDTO> getDeliveriesToSend() {
        ArrayList<DeliveryDTO> ret = new ArrayList<>();
        ArrayList<DeliveryDTO> arr = mapper.getDeliveries();
        for (DeliveryDTO deliveryDTO : arr){
            if (deliveryDTO.getDepartureWeight() <= 0 && !deliveryDTO.getModification().contains("newer")){
                deliveries.put(deliveryDTO.getId(), new Delivery(deliveryDTO));
                ret.add(deliveryDTO);
            }
        }
        return ret;
    }

    public ArrayList<DeliveryDTO> getDeliveriesData() {
        return mapper.getDeliveries();
    }

    public DeliveryDTO getDeliveriesByDate(LocalDate date) {
        ArrayList<DeliveryDTO> ret = new ArrayList<>();
        ArrayList<DeliveryDTO> deliveryDTOS = mapper.getDeliveries();
        for (DeliveryDTO deliveryDTO : deliveryDTOS){
            if (deliveryDTO.getDate().equals(date.format(DateTimeFormatter.ofPattern("d-M-uu"))))
                if  (deliveryDTO.getDepartureWeight() <= 0) {
                    if (!deliveryDTO.getModification().contains("newer"))
                        return deliveryDTO;
                }
        }
        return null;
    }

    // probably we dont need this function
//    public boolean tryToCreateDelivery(DriverDTO driverDTO, TruckDTO truckDTO, LocalTime shift, LocalDate date, TaskDTO taskDTO) {
//
//        BusinessLayer.EmployeesBuisnessLayer.FacadeController efc = BusinessLayer.EmployeesBuisnessLayer.FacadeController.getInstance();
//        LocalTime MShift = LocalTime.parse("10:00");
//        LocalTime EShift = LocalTime.parse("16:00");
//        LocalTime[] possibleShifts = new LocalTime[]{MShift, EShift};
//        for (LocalTime shift : possibleShifts){
//            boolean thereIsStorekeeper = efc.isStorekeeperAssigned(date, shift).getValue();
//            ResponseT<List <Employee>> drivers = getAllAssignedDrivers.invoke(date, shift)//ShiftController.getInstance().getAllAssignedDrivers(date, shift);
//
//        }
//    }

//    public ArrayList<DeliveryDTO> getTasksFromDeliveriesData() {
//        return new ArrayList<DeliveryDTO>(this.dataController.getDeliveries().values());
//    }
}
