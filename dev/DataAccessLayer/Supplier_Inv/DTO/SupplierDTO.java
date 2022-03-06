package DataAccessLayer.Supplier_Inv.DTO;

public class SupplierDTO {

        private Integer ID;
        private String name;
        private String address;
        private String email;
        private Integer bankAcc;
        private String paymentMethod;
        private String infoSupDay;
        private String contacts;
        private  boolean pickUp;


        public SupplierDTO(Integer ID, String name, String address, String email, Integer bankAcc, String paymentMethod,
                           String infoSupDay, String contacts, boolean pickUp){
            this.ID = ID;
            this.name = name;
            this.address = address;
            this.email = email;
            this.bankAcc = bankAcc;
            this.paymentMethod = paymentMethod;
            this.infoSupDay = infoSupDay;
            this.contacts = contacts;
            this.pickUp = pickUp;
        }

    public Integer getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public Integer getBankAcc() {
        return bankAcc;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getInfoSupDay() {
        return infoSupDay;
    }

    public String getContacts() {
        return contacts;
    }

    public boolean isPickUp() {
        return pickUp;
    }

    @Override
        public String toString() {
            return "supplierDTO{" +
                    "ID: '" + this.ID + '\'' +
                    ", name: '" + this.name + '\'' +
                    ", addres: '" + this.address + '\'' +
                    ", email: " + this.email +
                    ", bankAcc: " + this.bankAcc +
                    ", paymentMethod: " + this.paymentMethod +
                    ", infoSupDay: " + this.infoSupDay +
                    ", contacts: " + this.contacts +
                    ", pickUp: " + this.pickUp +
                    '}';
        }
    }

