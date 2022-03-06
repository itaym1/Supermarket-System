package BusinessLayer.DeliveryBusinessLayer;

public class tmpEmployee implements Employee {
    private String name;
    private int license;

    public tmpEmployee(String name, int license){
        this.name = name;
        this.license = license;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getLicenceType(){
        return license;
    }

    public String toString(){
        return this.name+" "+this.license;
    }
}
