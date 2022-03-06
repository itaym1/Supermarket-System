package BusinessLayer.Inventory;


import java.time.LocalDate;

public class Discount {
    private LocalDate start;
    private LocalDate end;
    private int discountPr;

    public Discount(LocalDate start, LocalDate end, int discountPr) {
        this.start = start;
        this.end = end;
        this.discountPr = discountPr;
    }

    public LocalDate getStart() {
        return start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public int getDiscountPr() {
        return discountPr;
    }
}
