package DataAccessLayer.DeliveryDataAccessLayer.DTO;

import BusinessLayer.DeliveryBusinessLayer.Area;
import BusinessLayer.DeliveryBusinessLayer.Location;

import java.util.ArrayList;

public class AreaDTO {
    private ArrayList<LocationDTO> locations;
    private String areaName; // we didnt put this data type in the diagram - but its necessary

    public AreaDTO(Area a){
        locations = new ArrayList<>();
        for (Location l : a.getLocations()){
            locations.add(new LocationDTO(l));
        }
        this.areaName = a.getAreaName();
    }

    public ArrayList<LocationDTO> getLocations() {
        return locations;
    }

    public void addLocation(LocationDTO location) {
        this.locations.add(location);
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }



    public AreaDTO(String areaName){
        this.locations = new ArrayList<>();
        this.areaName = areaName;
    }

    public AreaDTO(String areaName, ArrayList<LocationDTO> locations){
        this.areaName = areaName;
        this.locations = locations;
    }
    public String toString() {
        String str = "";
        str += locations.toString() + ", ";

//        for (int i = 0; i < locations.size(); i++){
//            System.out.println(locations);
//            str += locations.toString() + ", ";
//        }
//        if (str.equals(""))
//            return str;
        str = str.substring(0, str.length() - 2);

        return areaName + ": " + str;
    }

    public boolean equals(Object other){
        if (other instanceof AreaDTO){
            AreaDTO otherArea = (AreaDTO) other;
            return otherArea.getAreaName().equals(getAreaName());
        }
        return false;
    }
}
