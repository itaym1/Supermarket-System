package BusinessLayer.DeliveryBusinessLayer;
public class Driver {
    private int licenseType;
    private Employee employee;

    public Driver(int licenseType){
        this.licenseType = licenseType;
    }

    public Driver(Employee e){
        this.employee = e;
    }

    public Driver(tmpEmployee e){
        this.employee = e;
        this.licenseType = e.getLicenceType();
    }

    public void setEmployee(Employee e){
        employee = e;
    }

    public String getName(){
        return employee.getName();
    }


    @Override
    public String toString() {
        return "Driver{" +
                "licenseType=" + licenseType +
                ", employee=" + employee +
                '}';
    }

    public int getLicenceType() {
        return licenseType;
    }
}

