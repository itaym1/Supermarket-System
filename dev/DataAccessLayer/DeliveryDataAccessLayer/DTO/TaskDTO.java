package DataAccessLayer.DeliveryDataAccessLayer.DTO;

import BusinessLayer.DeliveryBusinessLayer.Task;

import java.util.HashMap;

public class TaskDTO {
    private String id;
    private HashMap<String, Integer> listOfProduct;
    private String loadingOrUnloading;
    LocationDTO destination;

    public TaskDTO(String id, HashMap<String, Integer> listOfProduct, String loadingOrUnloading, LocationDTO destination) {
        this.id = id;
        this.listOfProduct = listOfProduct;
        this.loadingOrUnloading = loadingOrUnloading;
        this.destination = destination;
    }

    public TaskDTO() {
    }

    public TaskDTO(HashMap<String, Integer> listOfProduct, String loadingOrUnloading, LocationDTO destination) {
        this.id = null;
        this.listOfProduct = listOfProduct;
        this.loadingOrUnloading = loadingOrUnloading;
        this.destination = destination;
    }

    public TaskDTO(Task task) {
        this.id = task.getId();
        this.listOfProduct = task.getListOfProduct();
        this.loadingOrUnloading = task.getLoadingOrUnloading();
        this.destination = new LocationDTO(task.getDestination());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public HashMap<String, Integer> getListOfProduct() {
        return listOfProduct;
    }

    public HashMap<String, Integer> getListOfProductCopy(){
        HashMap<String, Integer> copy = new HashMap<>(listOfProduct);
        return copy;
    }

    public void setListOfProduct(HashMap<String, Integer> listOfProduct) {
        this.listOfProduct = listOfProduct;
    }

    public String getLoadingOrUnloading() {
        return loadingOrUnloading;
    }

    public void setLoadingOrUnloading(String loadingOrUnloading) {
        this.loadingOrUnloading = loadingOrUnloading;
    }

    public LocationDTO getDestination() {
        return destination;
    }

    public void setDestination(LocationDTO destination) {
        this.destination = destination;
    }

    @Override
    public String toString() {
        return "id - " + this.id + "\nlist of products - " + this.listOfProduct + "\nmission - " + this.loadingOrUnloading + "\ndestination - " + destination.getAddress() + " | " + destination.getContactName() + " | " + destination.getPhoneNumber();
    }

    public String toString(String tabs) {
        return tabs + "id - " + this.id + "\n" + tabs + "list of products - " + this.listOfProduct + "\n" + tabs + "mission - " + this.loadingOrUnloading + "\n" + tabs + "destination - " + destination.getAddress() + " | " + destination.getContactName() + " | " + destination.getPhoneNumber();
    }

    public boolean equals(Object other){
        if (other instanceof TaskDTO){
            TaskDTO oth = (TaskDTO) other;
            return oth.getId().equals(getId());
        }
        return false;
    }
}
