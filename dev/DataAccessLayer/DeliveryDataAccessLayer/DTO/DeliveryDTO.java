package DataAccessLayer.DeliveryDataAccessLayer.DTO;

import BusinessLayer.DeliveryBusinessLayer.Delivery;
import BusinessLayer.DeliveryBusinessLayer.Task;

import java.util.ArrayList;

public class DeliveryDTO {
    private String id;
    private String date;
    private String timeOfDeparture;
    private String truckNumber;
    private String driverName;
    private int departureWeight;
    private String modification;
    private LocationDTO origin;
    private ArrayList<TaskDTO> destinations;


     public DeliveryDTO(String id, String date, String timeOfDeparture, String truckNumber, String driverName, int departureWeight, String modification, LocationDTO origin, ArrayList<TaskDTO> destinations) {
        this.id = id;
        this.date = date;
        this.timeOfDeparture = timeOfDeparture;
        this.truckNumber = truckNumber;
        this.driverName = driverName;
        this.departureWeight = departureWeight;
        this.modification = modification; // " - old g121 -- new g123 -"
        this.origin = origin;
        this.destinations = destinations;
    }

    public DeliveryDTO(Delivery d){
        this.id = d.getID();
        this.date = d.getDate();
        this.timeOfDeparture = d.getTimeOfDeparture();
        this.truckNumber = d.getTruckNumber();
        this.driverName = d.getDriverName();
        this.departureWeight = d.getDepartureWeight();
        this.modification = d.getModification();
        this.origin = new LocationDTO(d.getOrigin());
        this.destinations = new ArrayList<>();
        for (Task t: d.getDestinations())
            destinations.add(new TaskDTO(t));
    }

    public DeliveryDTO(String date, String timeOfDeparture, String truckNumber, String driverName, int departureWeightInt, String modification, LocationDTO originLocation, ArrayList<TaskDTO> arrTask) {
        this.id = null;
        this.date = date;
        this.timeOfDeparture = timeOfDeparture;
        this.truckNumber = truckNumber;
        this.driverName = driverName;
        this.departureWeight = departureWeightInt;
        this.modification = modification; // " - old g121 -- new g123 -"
        this.origin = originLocation;
        this.destinations = arrTask;
     }

    public String getId() {
        return id;
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

    public void setModification(String modification) {
        this.modification = modification;
    }

    public LocationDTO getOrigin() {
        return origin;
    }

    public void setOrigin(LocationDTO origin) {
        this.origin = origin;
    }

    public ArrayList<TaskDTO> getDestinations() {
        return destinations;
    }

    public void setDestinations(ArrayList<TaskDTO> destinations) {
        this.destinations = destinations;
    }

    @Override
    public String toString() {
//         String departureWeight = "";
//         if (this.departureWeight == 0)
//             departureWeight = " set before departure";
//         else
//             departureWeight = String.valueOf(this.departureWeight);
//        return  "\t\tid ='" + id + '\'' +
//                "\n\t\tdate = '" + date + '\'' +
//                "\n\t\ttimeOfDeparture = '" + timeOfDeparture + '\'' +
//                "\n\t\ttruckNumber = '" + truckNumber + '\'' +
//                "\n\t\tdriverName = '" + driverName + '\'' +
//                "\n\t\tdepartureWeight = " + departureWeight +
//                "\n\t\tmodification = '" + modification + '\'' +
//                "\n\t\torigin = " + origin +
//                "\n\t\tdestinations= " + destinations;

        ArrayList<String> destin = new ArrayList<>();
        for (TaskDTO t: destinations){
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


    public boolean equals(Object other){
        if (other instanceof DeliveryDTO){
            DeliveryDTO oth = (DeliveryDTO) other;
            return oth.getId().equals(getId());
        }
        return false;
    }
}
