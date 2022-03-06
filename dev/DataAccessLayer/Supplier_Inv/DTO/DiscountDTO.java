package DataAccessLayer.Supplier_Inv.DTO;


import BusinessLayer.Inventory.Discount;

import java.time.LocalDate;
public class DiscountDTO {
    private LocalDate start;
    private LocalDate end;
    private int discountPr;
    private int itemId;

    public DiscountDTO(Discount dis, int itemId) {
        this.start = dis.getStart();
        this.end = dis.getEnd();
        this.discountPr = dis.getDiscountPr();
        this.itemId = itemId;
    }

    public DiscountDTO(LocalDate start, LocalDate end, int discountPr, int itemId) {
        this.start = start;
        this.end = end;
        this.discountPr = discountPr;
        this.itemId = itemId;
    }

    public LocalDate getStart() {
        return start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public int getItemId() { return itemId; }

    public int getDiscountPr() {
        return discountPr;
    }
}
