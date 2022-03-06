package BusinessLayer.DeliveryBusinessLayer;


import java.util.ArrayList;

public class Area{
    private ArrayList<Location> locations;
    private String areaName; // we didnt put this data type in the diagram - but its necessary

    public Area(String areaName){
        this.locations = new ArrayList<>();
        this.areaName = areaName;
    }

//    public Area(AreaDTO areaDTO){
//        this.areaName = areaDTO.getAreaName();
//        this.locations = areaDTO.getLocations();
//    }

    public String getAreaName() {
        return areaName;
    }

    public void addLocation(Location location){
        this.locations.add(location);
    }

    @Override
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

    public ArrayList<Location> getLocations() {
        return locations;
    }

    public String getName() {
        return areaName;
    }
}
