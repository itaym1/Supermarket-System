package DataAccessLayer.Supplier_Inv.DTO;

import BusinessLayer.Inventory.FaultyItem;

import java.time.LocalDate;
public class FaultyItemDTO {
    private int itemId;
    private LocalDate expDate;
    private int amount;

    public FaultyItemDTO(FaultyItem fi) {
        this.itemId = fi.getItemId();
        this.expDate = fi.getExpDate();
        this.amount = fi.getAmount();
    }

    public FaultyItemDTO(int itemId, LocalDate expDate, int amount) {
        this.itemId = itemId;
        this.expDate = expDate;
        this.amount = amount;
    }

    public int getItemId() {
        return itemId;
    }

    public LocalDate getExpDate() {
        return expDate;
    }

    public int getAmount() {
        return amount;
    }
}
