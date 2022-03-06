package DataAccessLayer.Supplier_Inv.DTO;

import java.time.LocalDate;

public class PeriodicOrderDTO {

    private Integer orderID;
    private LocalDate supplyDate;
    private Integer intervals;


    public PeriodicOrderDTO(Integer orderID, LocalDate supplyDate, Integer intervals) {
        this.orderID = orderID;
        this.supplyDate = supplyDate;
        this.intervals = intervals;

    }

    public Integer getOrderID() {
        return orderID;
    }

    public LocalDate getSupplyDate() {
        return supplyDate;
    }

    public Integer getIntervals() {
        return intervals;
    }


    @Override
    public String toString() {
        return "PeriodicOrderDTO{" +
                "orderID: '" + this.orderID + '\'' +
                ", supplyDate: '" + this.supplyDate + '\'' +
                ", intervals: '" + this.intervals + '\'' +
                '}';
    }
}
