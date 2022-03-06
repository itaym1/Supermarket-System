package DataAccessLayer.Supplier_Inv.DTO;

import BusinessLayer.Inventory.Category;

public class CategoryDTO {
    private String name;

    public CategoryDTO(Category c) {
        name = c.getName();
    }

    public CategoryDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
