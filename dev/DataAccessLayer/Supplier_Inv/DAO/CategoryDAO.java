package DataAccessLayer.Supplier_Inv.DAO;

import BusinessLayer.Inventory.Category;
import BusinessLayer.Response;
import BusinessLayer.ResponseT;
import DataAccessLayer.Supplier_Inv.DTO.CategoryDTO;
import DataAccessLayer.Supplier_Inv.DTO.CategoryItemsDTO;
import DataAccessLayer.Supplier_Inv.DTO.subCategoriesDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class CategoryDAO extends DAO{

    public CategoryDAO() {
    }

    public ResponseT<CategoryDTO> create(Category c) {
        CategoryDTO toInsert = new CategoryDTO(c);
        String SQL = "INSERT INTO Category (name) VALUES (?)";
        try {
            ResponseT<Connection> r = getConn();
            if (!r.ErrorOccured()) {
                PreparedStatement ps = r.value.prepareStatement(SQL);
                ps.setString(1, toInsert.getName());
                ps.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseT("Could not add category to DB");
        }
        return new ResponseT<>(toInsert);
    }

    public Response createSubCategory(Category c, String fatherName) {
        subCategoriesDTO toInsert = new subCategoriesDTO(fatherName, c.getName());
        String SQL = "INSERT INTO subCategories (fatherCategory, childCategory) VALUES (?, ?)";
        try {
            ResponseT<Connection> r = getConn();
            if (!r.ErrorOccured()) {
                PreparedStatement ps = r.value.prepareStatement(SQL);
                ps.setString(1, toInsert.getFatherCategory());
                ps.setString(2, toInsert.getChildCategory());
                ps.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new Response("Could not add sub category to DB");
        }
        return new ResponseT<>(toInsert);
    }

    public Response createCategoryItems(Category c, int itemID) {
        CategoryItemsDTO toInsert = new CategoryItemsDTO(c.getName(), itemID);
        String SQL = "INSERT INTO CategoryItems (catName, itemID) VALUES (?, ?)";
        try {
            ResponseT<Connection> r = getConn();
            if (!r.ErrorOccured()) {
                PreparedStatement ps = r.value.prepareStatement(SQL);
                ps.setString(1, toInsert.getCatName());
                ps.setInt(2, toInsert.getItemID());
                ps.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new Response("Could not add category-item to DB");
        }
        return new ResponseT<>(toInsert);
    }

    public Response delete(Category c) {
        CategoryDTO toDelete = new CategoryDTO(c);
        String SQL = "DELETE FROM Category WHERE catName = ?";
        try {
            ResponseT<Connection> r = getConn();
            if(!r.ErrorOccured()) {
                PreparedStatement ps = r.value.prepareStatement(SQL);
                ps.setString(1, toDelete.getName());
                if(!ps.execute()) {
                    return new Response("cannot delete category from db");
                }
            }
        }catch (Exception e) {
            return new Response("cannot delete category from db");
        }
        return new Response();
    }

    public Response deleteSubCategory(Category c, String fatherName) {
        subCategoriesDTO toDelete = new subCategoriesDTO(c.getName(), fatherName);
        String SQL = "DELETE FROM subCategories WHERE childCategory = ? AND fatherCategory = ?";
        try {
            ResponseT<Connection> r = getConn();
            if(!r.ErrorOccured()) {
                PreparedStatement ps = r.value.prepareStatement(SQL);
                ps.setString(1, toDelete.getChildCategory());
                ps.setString(2, toDelete.getFatherCategory());

                if(!ps.execute()) {
                    return new Response("cannot delete sub-category from db");
                }
            }
        }catch (Exception e) {
            return new Response("cannot delete sub-category from db");
        }
        return new Response();
    }

    public Response deleteCategoryItems(Category c, int itemID) {
        CategoryItemsDTO toDelete = new CategoryItemsDTO(c.getName(), itemID);
        String SQL = "DELETE FROM CategoryItems WHERE catName = ? AND itemID = ?";
        try {
            ResponseT<Connection> r = getConn();
            if(!r.ErrorOccured()) {
                PreparedStatement ps = r.value.prepareStatement(SQL);
                ps.setString(1, toDelete.getCatName());
                ps.setInt(2, toDelete.getItemID());
                ps.execute();
            }
        }catch (Exception e) {
            return new Response("cannot delete category-item from db");
        }
        return new Response();
    }

    public ResponseT<List<CategoryDTO>> read() {
        String SQL = "SELECT * FROM Category";
        List<CategoryDTO> catList = new LinkedList<>();
        try {
            ResponseT<Connection> r = getConn();
            if(!r.ErrorOccured()) {
                PreparedStatement ps = r.value.prepareStatement(SQL);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    catList.add((new CategoryDTO(rs.getString("name"))));
                }
            }
        }catch (Exception e) {
            return new ResponseT("cannot read category");
        }
        return new ResponseT<>(catList);
    }

    public ResponseT<List<subCategoriesDTO>> readSubCategory() {
        String SQL = "SELECT * FROM subCategories";
        List<subCategoriesDTO> subCatList = new LinkedList<>();
        try {
            ResponseT<Connection> r = getConn();
            if(!r.ErrorOccured()) {
                PreparedStatement ps = r.value.prepareStatement(SQL);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    subCatList.add((new subCategoriesDTO(rs.getString("fatherCategory"), rs.getString("childCategory"))));
                }
            }
        }catch (Exception e) {
            return new ResponseT("cannot read sub-category");
        }
        return new ResponseT<>(subCatList);
    }

    public ResponseT<List<CategoryItemsDTO>> readCategoryItems() {
        String SQL = "SELECT * FROM CategoryItems";
        List<CategoryItemsDTO> catItemsList = new LinkedList<>();
        try {
            ResponseT<Connection> r = getConn();
            if(!r.ErrorOccured()) {
                PreparedStatement ps = r.value.prepareStatement(SQL);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    catItemsList.add(new CategoryItemsDTO(rs.getString("catName"), rs.getInt("itemID")));
                }
            }
        }catch (Exception e) {
            return new ResponseT("cannot read category-item");
        }
        return new ResponseT<>(catItemsList);
    }
}
