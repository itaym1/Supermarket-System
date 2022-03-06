package DataAccessLayer.Supplier_Inv.DTO;

public class subCategoriesDTO {
    private String fatherCategory;
    private String childCategory;

    public subCategoriesDTO(String fatherCategory, String childCategory) {
        this.fatherCategory = fatherCategory;
        this.childCategory = childCategory;
    }

    public String getFatherCategory() {
        return fatherCategory;
    }

    public String getChildCategory() {
        return childCategory;
    }
}
