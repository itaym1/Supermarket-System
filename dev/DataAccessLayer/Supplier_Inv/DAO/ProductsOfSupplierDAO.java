package DataAccessLayer.Supplier_Inv.DAO;

import BusinessLayer.Response;
import BusinessLayer.ResponseT;
import DataAccessLayer.Supplier_Inv.DTO.ProductsOfSupplierDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class ProductsOfSupplierDAO extends DAO {

    public Response insert(Integer productID, Integer supplierID, String name, String category, double price, int pidSuperLee) {

        String product = "INSERT INTO ProductsOfSupplier (productID, supplierID, name, category, price,pidSuperLee) VALUES (?, ?, ?, ?, ?,?)";

        try {Connection conn = getConn().value;
             PreparedStatement pstmt = conn.prepareStatement(product);

            // inserting to employee table
            pstmt.setInt(1, productID);
            pstmt.setInt(2, supplierID);
            pstmt.setString(3,name);
            pstmt.setString(4, category);
            pstmt.setDouble(5, price);
            pstmt.setInt(6,pidSuperLee);

            pstmt.execute();

        } catch (SQLException e) {
            return new Response(e.getMessage());
        }
        return new Response();
    }


    public Response insert(ProductsOfSupplierDTO productOfSup){
        return insert(productOfSup.getProductID(), productOfSup.getSupplierID(), productOfSup.getName(),
                productOfSup.getCategory(), productOfSup.getPrice(), productOfSup.getPidSuperLee());
    }

    public Response delete(Integer supplierID, Integer productID) {
        String SQL = "DELETE FROM ProductsOfSupplier WHERE productID = ? AND supplierID = ? ";
        try {
            ResponseT<Connection> r = getConn();
            if(!r.ErrorOccured()) {
                PreparedStatement ps = r.value.prepareStatement(SQL);
                ps.setInt(1, productID);
                ps.setInt(2, supplierID);

                ps.execute();

            }
        }catch (SQLException e) {
            return new Response(e.getMessage());
        }
        return new Response();
    }
    //TODO: update functions ????

    public ResponseT<List<ProductsOfSupplierDTO>> read() {
        String SQL = "SELECT * FROM ProductsOfSupplier";
        List<ProductsOfSupplierDTO> posList = new LinkedList<>();
        try {
            ResponseT<Connection> r = getConn();
            if(!r.ErrorOccured()) {
                PreparedStatement ps = r.value.prepareStatement(SQL);
                ResultSet rs = ps.executeQuery();
                while(rs.next()) {
                    posList.add(new ProductsOfSupplierDTO(rs.getInt("productID"),
                            rs.getInt("supplierID"), rs.getString("name"),
                            rs.getString("category"), rs.getDouble("price"), rs.getInt("prodIDSuperLee")));
                }
            }
        }catch (Exception e) {
            return new ResponseT("cannot read sale");
        }
        return new ResponseT<>(posList);
    }
}
