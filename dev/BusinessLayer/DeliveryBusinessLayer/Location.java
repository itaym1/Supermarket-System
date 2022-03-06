package BusinessLayer.DeliveryBusinessLayer;

import DataAccessLayer.DeliveryDataAccessLayer.DTO.LocationDTO;

public class Location {
    private String address;
    private String phoneNumber;
    private String contactName;

    public Location(String address, String phoneNumber, String contactName){
        this.address = address;
        this.contactName = contactName;
        this.phoneNumber = phoneNumber;
    }

    public Location(LocationDTO locationDTO){
        this.address = locationDTO.getAddress();
        this.contactName = locationDTO.getContactName();
        this.phoneNumber = locationDTO.getPhoneNumber();
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "{" +
                "address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", contactName='" + contactName + '\'' +
                '}';
    }
}
