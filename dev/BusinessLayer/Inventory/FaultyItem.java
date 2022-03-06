package BusinessLayer.Inventory;


import java.time.LocalDate;

public class FaultyItem {
    private int itemId;
    private LocalDate expDate;
    private int amount;

    public FaultyItem(int itemId, LocalDate expDate, int amount){
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

    public String toString(){
        String str = "id:\t\t\t\t" + itemId +"\n" +
                "Expiration date: \t" + expDate + "\n" +
                "Amount: \t\t\t" + amount+"\n";
        return str;
    }
}
