package BusinessLayer.DeliveryBusinessLayer;

import DataAccessLayer.DeliveryDataAccessLayer.DTO.DeliveryDTO;
import DataAccessLayer.DeliveryDataAccessLayer.DTO.TaskDTO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Delivery {
    private String id;
    private String date;
    private String timeOfDeparture;
    private String truckNumber;
    private String driverName;
    private int departureWeight;
    private String modification;
    private Location origin;
    private ArrayList<Task> destinations;

    public Delivery(DeliveryDTO deliveryDTO) {
        this.id = deliveryDTO.getId();
        this.date =deliveryDTO.getDate();
        this.timeOfDeparture = deliveryDTO.getTimeOfDeparture();
        this.truckNumber = deliveryDTO.getTruckNumber();
        this.driverName = deliveryDTO.getDriverName();
        this.departureWeight = deliveryDTO.getDepartureWeight();
        this.modification = deliveryDTO.getModification(); // " - old g121 -- new g123 -"
        this.origin = new Location(deliveryDTO.getOrigin());

        this.destinations = convertTaskDTO2Task(deliveryDTO.getDestinations());
    }

    private ArrayList<Task> convertTaskDTO2Task(ArrayList<TaskDTO> arr){
        ArrayList<Task> ret = new ArrayList<>();
        for (TaskDTO taskDTO : arr){
            ret.add(new Task(taskDTO));
        }
        return ret;

    }


    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimeOfDeparture() {
        return timeOfDeparture;
    }

    public void setTimeOfDeparture(String timeOfDeparture) {
        this.timeOfDeparture = timeOfDeparture;
    }

    public String getTruckNumber() {
        return truckNumber;
    }

    public void setTruckNumber(String truckNumber) {
        this.truckNumber = truckNumber;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public int getDepartureWeight() {
        return departureWeight;
    }

    public void setDepartureWeight(int departureWeight) {
        this.departureWeight = departureWeight;
    }

    public String getModification() {
        return modification;
    }

    public String addModification(String modification) {
        this.modification += modification;
        return this.modification;
    }

    public Location getOrigin() {
        return origin;
    }

    public void setOrigin(Location origin) {
        this.origin = origin;
    }

    public ArrayList<Task> getDestinations() {
        return destinations;
    }

    public void setDestinations(ArrayList<Task> destinations) {
        this.destinations = destinations;
    }

    public Delivery(String id, String date, String timeOfDeparture, String truckNumber, String driverName, int departureWeight, String modification, Location origin, ArrayList<Task> destinations){
        this.id = id;
        this.date =date;
        this.timeOfDeparture = timeOfDeparture;
        this.truckNumber = truckNumber;
        this.driverName = driverName;
        this.departureWeight = departureWeight;
        this.modification = modification; // " - old g121 -- new g123 -"
        this.origin = origin;
        this.destinations = destinations;
    }

    public Delivery(){}



    public void setDriver(Driver dr) {
        driverName = dr.getName();
    }

    public Delivery clone(){return null;} //todo  clone.modification = "- old "+this.id+" -"

    public void makeOld(String oldID){
        modification += "- new "+ oldID+ " -";
    }

    @Override
    public String toString() {
        ArrayList<String> destin = new ArrayList<>();
        for (Task t: destinations){
            destin.add("\n"+t.toString("\t\t\t")+"\n\t\t");
        }
        String destinSTR = destin.toString().substring(0,destin.toString().length()-2);
        return  "\t\tid ='" + id + '\'' +
                "\n\t\tdate = '" + date + '\'' +
                "\n\t\ttimeOfDeparture = '" + timeOfDeparture + '\'' +
                "\n\t\ttruckNumber = '" + truckNumber + '\'' +
                "\n\t\tdriverName = '" + driverName + '\'' +
                "\n\t\tdepartureWeight = " + departureWeight +
                "\n\t\tmodification = '" + modification + '\'' +
                "\n\t\torigin = " + origin.getAddress() +
                "\n\t\tdestinations= " + destinSTR;
    }

    public String getID() {
        return id;
    }


    public boolean isUpdatable(){
//        SimpleDateFormat sdf = new SimpleDateFormat("d-M-uu");
//        SimpleDateFormat sdt = new SimpleDateFormat("h:m");
        String[] spl = date.split("-");
        String[] splTIme = timeOfDeparture.split(":");
        int year = Integer.parseInt(spl[2])+2000;
        int month = Integer.parseInt(spl[1]);
        int day = Integer.parseInt(spl[0]);
        int hour = Integer.parseInt(splTIme[0]);
        int min = Integer.parseInt(splTIme[1]);
        LocalDate l1 = LocalDate.of(year,month,day);
        LocalTime l2 = LocalTime.of(hour, min);
        LocalDate current = LocalDate.now();
        LocalTime curT = LocalTime.now();
        if (l1.compareTo(current) == 0) {
            if (l2.compareTo(curT) < 0) {
                return false;
            }
        }
        return l1.compareTo(current) >= 0;
    }
}
