package BusinessLayer.DeliveryBusinessLayer;

import DataAccessLayer.DeliveryDataAccessLayer.DTO.TaskDTO;

import java.util.HashMap;

public class Task {
    private String id;
    private HashMap<String, Integer>listOfProduct;
    private String loadingOrUnloading;
    Location destination;

    public Task(String id, HashMap<String, Integer> listOfProduct, String loadingOrUnloading, Location destination){
        this.id = id;
        this.listOfProduct = listOfProduct;
        this.loadingOrUnloading = loadingOrUnloading;
        this.destination = destination;
    }

    public Task(TaskDTO taskDTO) {
        this.id = taskDTO.getId();
        this.listOfProduct = taskDTO.getListOfProduct();
        this.loadingOrUnloading = taskDTO.getLoadingOrUnloading();
        this.destination = new Location(taskDTO.getDestination());
    }

    public Location getDestination() {
        return destination;
    }

    public String getId() {
        return id;
    }

    public String getLoadingOrUnloading() {
        return loadingOrUnloading;
    }

    public HashMap<String, Integer> getListOfProduct() {
        return listOfProduct;
    }

    public String toString(){
        return "id - "+this.id+"\nlist of products - "+this.listOfProduct+"\nmission - "+this.loadingOrUnloading+"\ndestination - "+destination.getAddress()+" | "+destination.getContactName()+" | "+destination.getPhoneNumber();
    }

    public String toString(String tabs){
        return tabs+"id - "+this.id+"\n"+tabs+"list of products - "+this.listOfProduct+"\n"+tabs+"mission - "+this.loadingOrUnloading+"\n"+tabs+"destination - "+destination.getAddress()+" | "+destination.getContactName()+" | "+destination.getPhoneNumber();
    }
}
