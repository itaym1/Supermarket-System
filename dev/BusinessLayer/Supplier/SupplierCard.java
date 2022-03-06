package BusinessLayer.Supplier;

import java.util.HashMap;

public class SupplierCard {
    private String supplierName;
    private int supplierID;
    private String address;
    private String email;
    private int bankAcc;
    private String paymentMethod;
    private String contacts;
    private String infoSupplyDay;
    private boolean pickUp;

    public SupplierCard(String supplierName, int supplierID, String address, String email, int bankAcc,
                        String paymentMethod, String contacts, String infoSupplyDay, boolean pickUp){
        this.supplierName = supplierName;
        this.supplierID = supplierID;
        this.address = address;
        this.email = email;
        this.bankAcc = bankAcc;
        this.paymentMethod = paymentMethod;
        this.contacts = contacts;
        this.infoSupplyDay = infoSupplyDay;
        this.pickUp = pickUp;
    }

    public String toString(){
        String pickupS = "";
        if(pickUp)
            pickupS = "Pickup Is Required";
        else pickupS = "Pickup Is Not Required";
        return '\n' + "----- Supplier Info -----  \n" + "Name: " + supplierName + '\n' + "ID: " +supplierID + '\n' + "Address: : " + address + '\n' + "Email: " + email + '\n' +
                "Bank Account: " + bankAcc + '\n' +"Payment Method: " + paymentMethod + '\n' + "Contacts: " + contacts + '\n'
                + "Info Supply Day: " + infoSupplyDay + '\n' +  "pickUp: " + pickupS + '\n';
    }

    public String getSupplierName(){
        return this.supplierName;
    }

    public int getSupplierID(){
        return this.supplierID;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setBankAcc(int bankAcc) {
        this.bankAcc = bankAcc;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setInfoSupplyDay(String infoSupplyDay) {
        this.infoSupplyDay = infoSupplyDay;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setSupplierID(int supplierID) {
        this.supplierID = supplierID;
    }

    public void setPickUp(boolean pickUp) {
        this.pickUp = pickUp;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public int getBankAcc() {
        return bankAcc;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getContacts() {
        return contacts;
    }

    public String getInfoSupplyDay() {
        return infoSupplyDay;
    }

    public boolean isPickUp() {
        return pickUp;
    }
}
