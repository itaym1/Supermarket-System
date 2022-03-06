package DataAccessLayer.Supplier_Inv.DTO;

public class CategoryItemsDTO {

    private String catName;
    private int itemID;

    public CategoryItemsDTO(String catName, int itemID) {
        this.catName = catName;
        this.itemID = itemID;
    }

    public String getCatName() {
        return catName;
    }

    public int getItemID() {
        return itemID;
    }
}
