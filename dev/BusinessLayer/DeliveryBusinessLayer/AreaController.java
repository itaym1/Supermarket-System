package BusinessLayer.DeliveryBusinessLayer;

import DataAccessLayer.DeliveryDataAccessLayer.DTO.AreaDTO;
import DataAccessLayer.DeliveryDataAccessLayer.DTO.LocationDTO;
import DataAccessLayer.DeliveryDataAccessLayer.DTO.Response;
import DataAccessLayer.DeliveryDataAccessLayer.AreaDAO;
import DataAccessLayer.DeliveryDataAccessLayer.Mapper;

import java.util.ArrayList;
import java.util.HashMap;

public class AreaController {
    private HashMap<String, Area> controller;
    private AreaDAO dc;
    private Mapper mapper = Mapper.getInstance();

    public AreaController(){
        this.controller = new HashMap<>();
        dc = AreaDAO.getInstance();
    }

    public boolean containsArea(String areaName){
        if (this.controller.containsKey(areaName))
            return true;
        if (mapper.containsArea(areaName))
            return true;
//        AreaDTO areaDTO = mapper.getArea(areaName);
//        if (areaDTO != null) {
////            this.controller.put(areaName, new Area(areaName));
//            return false;
//        }
        return false;
    }

    public void addNewArea(AreaDTO areaDTO){
        controller.put(areaDTO.getAreaName(), new Area(areaDTO.getAreaName()));
        dc.storeArea(areaDTO);
        mapper.addNewArea(areaDTO);
    }

    public void addArea(String areaName, Area area){
        controller.put(areaName, area);
    }

    public Response<Boolean> addLocation(AreaDTO areaDTO, LocationDTO locationDTO){
        if (!containsArea(areaDTO.getAreaName())){
            return new Response<>(false);
        }
        if (mapper.containsLocation(locationDTO.getAddress()))
            return new Response<>(false);
//        controller.get(areaDTO.getAreaName()).addLocation(new Location(locationDTO.getAddress(), locationDTO.getPhoneNumber(), locationDTO.getContactName()));
        dc.storeLocation(areaDTO, locationDTO);
        mapper.addLocation(areaDTO, locationDTO);
        return new Response<>(true);
    }


    @Override
    public String toString() {
        String str = "";
        for (Area a : controller.values()){
            str += "\t"+a.getAreaName()+"\n";
            for (Location l: a.getLocations()){
                str += "\t\t"+l.getAddress()+"\n";
            }
        }
        return str;
    }

    public HashMap<String, ArrayList<LocationDTO>> getLocationsByArea() {
        HashMap<String, ArrayList<LocationDTO>> hash = mapper.getLocationsByArea();
//        HashMap<Area, ArrayList<Location>> ret = new HashMap<>();
//        for(Area a : controller.values()){
//            ret.put(a, a.getLocations());
//        }
        return hash;
    }

    public Location getLocation(String destination) {
//        for (Area a : controller.values()){
//            for (Location l: a.getLocations()){
//                if (l.getAddress().equals(destination)){
//                    return l;
//                }
//            }
//        }
        LocationDTO locationDTO = mapper.getLocation(destination);
        if (locationDTO != null)
            return new Location(locationDTO);
        return  null;
    }

    public ArrayList<AreaDTO> getAreas() {
//        ArrayList<Area> arr = new ArrayList<>();
//        for (AreaDTO area : mapper.getAreas()){
//            arr.add(new Area(area));
//        }

        return mapper.getAreas();
    }

//    public ArrayList<AreaDTO> getAreasData() {
////        ArrayList<AreaDTO> arr = new ArrayList<>();
////        for (AreaDTO area : this.dc.getAreas().values()){
////            arr.add(area);
////        }
////        return arr;
////        return new ArrayList<>(dc.getAreas().values());
//        return null;
//    }
}
