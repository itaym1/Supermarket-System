package BusinessLayer.DeliveryBusinessLayer;

import DataAccessLayer.DeliveryDataAccessLayer.DTO.TruckDTO;

public class Truck {
    private String id;
    private String model;
    private int maxWeight;
    private int truckWeight;

    public void setId(String id) {
        this.id = id;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setMaxWeight(int maxWeight) {
        this.maxWeight = maxWeight;
    }

    public void setTruckWeight(int truckWeight) {
        this.truckWeight = truckWeight;
    }

    public Truck(String id, String model, int maxWeight, int truckWeight){
        this.id = id;
        this.model = model;
        this.maxWeight = maxWeight;
        this.truckWeight = truckWeight;
    }

    public Truck(TruckDTO truckDTO){
        this.id = truckDTO.getId();
        this.model = truckDTO.getModel();
        this.maxWeight = truckDTO.getMaxWeight();
        this.truckWeight = truckDTO.getTruckWeight();
    }

    public String getId(){
        return id;
    }

    public String getModel(){
        return this.model;
    }

    public int getMaxWeight(){
        return maxWeight;
    }

    public int getTruckWeight(){
        return truckWeight;
    }

    public String toString(){
        String ret = "";
        ret += ("id-"+id);
        ret += ("\nmodel-"+model) + ("\nmaxWeight-"+maxWeight) + ("\ntruckWeight-"+truckWeight);
        return ret;
    }

}
