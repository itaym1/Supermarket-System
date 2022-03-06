package DataAccessLayer.DeliveryDataAccessLayer.DTO;

import BusinessLayer.DeliveryBusinessLayer.Truck;

public class TruckDTO {
    private String id;
    private String model;
    private int maxWeight;
    private int truckWeight;

    public TruckDTO(String id, String model, int maxWeight, int truckWeight){
        this.id = id;
        this.model = model;
        this.maxWeight = maxWeight;
        this.truckWeight = truckWeight;
    }

    public TruckDTO(Truck truck){
        this.id = truck.getId();
        this.model = truck.getModel();
        this.maxWeight = truck.getMaxWeight();
        this.truckWeight = truck.getTruckWeight();
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

    @Override
    public String toString() {
        String ret = "";
        ret += ("id-"+id);
        ret += ("\nmodel-"+model) + ("\nmaxWeight-"+maxWeight) + ("\ntruckWeight-"+truckWeight);
        return ret;
    }

    public boolean equals(Object other){
        if (other instanceof TruckDTO){
            TruckDTO oth = (TruckDTO) other;
            return oth.getId().equals(getId());
        }
        return false;
    }
}
