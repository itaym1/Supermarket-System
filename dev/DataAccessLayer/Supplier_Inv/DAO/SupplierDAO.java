package DataAccessLayer.Supplier_Inv.DAO;

import BusinessLayer.Response;
import BusinessLayer.ResponseT;
import BusinessLayer.Supplier.SupplierCard;
import DataAccessLayer.Supplier_Inv.DTO.SupplierDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;


public class SupplierDAO extends DAO {

    public Response insert(Integer ID, String name, String address, String email, Integer bankAcc, String paymentMethod,
                           String infoSupDay, String contacts, boolean pickUp) {

        String supplier = "INSERT INTO Supplier (ID, name, address, email, bankAcc, paymentMethod ,infoSupDay, contacts, pickUp) VALUES (?, ?, ?, ?, ?, ?, ?, ?,?)";

        try {Connection conn = getConn().value;
             PreparedStatement pstmt = conn.prepareStatement(supplier);

            // inserting to Suppliers table
            pstmt.setInt(1, ID);
            pstmt.setString(2, name);
            pstmt.setString(3,address);
            pstmt.setString(4, email);
            pstmt.setInt(5, bankAcc);
            pstmt.setString(6, paymentMethod);
            pstmt.setString(7, infoSupDay);
            pstmt.setString(8, contacts);
            pstmt.setBoolean(9, pickUp);

            pstmt.execute();

        } catch (SQLException e) {
            return new Response(e.getMessage());
        }
        return new Response();
    }


    public Response insert(SupplierCard supplier){
        return insert(supplier.getSupplierID(), supplier.getSupplierName(), supplier.getAddress(), supplier.getEmail(),
                supplier.getBankAcc(), supplier.getPaymentMethod(), supplier.getInfoSupplyDay(),
                supplier.getContacts(), supplier.isPickUp());
    }

    //update string column
    public Response update(String col, int ID, String newVal){
        String sql = String.format("UPDATE Supplier SET %s = ? WHERE ID = ?", col);

        try{
            Connection conn = getConn().value;
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newVal);
            pstmt.setInt(2, ID);

            pstmt.execute();

        }catch(SQLException e){
            return new Response(e.getMessage());
        }

        return new Response();
    }

    //update int column
    public Response update(String col, int ID, int newVal){
        String sql = String.format("UPDATE Supplier SET %s = ? WHERE ID = ?", col);

        try{
            Connection conn = getConn().value;
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, newVal);
            pstmt.setInt(2, ID);

            pstmt.execute();

        }catch(SQLException e){
            return new Response(e.getMessage());
        }

        return new Response();
    }

    //update bool column
    public Response update(String col, int ID, boolean newVal){
        String sql = String.format("UPDATE Supplier SET %s = ? WHERE ID = ?", col);

        try{
            Connection conn = getConn().value;
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setBoolean(1, newVal);
            pstmt.setInt(2, ID);

            pstmt.execute();

        }catch(SQLException e){
            return new Response(e.getMessage());
        }

        return new Response();
    }

    public Response delete(Integer supplierID) {
        String SQL = "DELETE FROM Supplier WHERE ID = ?";
        try {
            ResponseT<Connection> r = getConn();
            if(!r.ErrorOccured()) {
                PreparedStatement ps = r.value.prepareStatement(SQL);
                ps.setInt(1, supplierID);
                ps.execute();
            }
        }catch (SQLException e) {
            return new Response(e.getMessage());
        }
        return new ResponseT(supplierID);
    }

    public ResponseT<List<SupplierDTO>> read() {
        String SQL = "SELECT * FROM Supplier";
        List<SupplierDTO> supplierList = new LinkedList<>();
        try {
            ResponseT<Connection> r = getConn();
            if(!r.ErrorOccured()) {
                PreparedStatement ps = r.value.prepareStatement(SQL);
                ResultSet rs = ps.executeQuery();
                while(rs.next()) {
                    supplierList.add(new SupplierDTO(rs.getInt("ID"), rs.getString("name"),
                            rs.getString("address"), rs.getString("email"),
                            rs.getInt("bankAcc"), rs.getString("paymentMethod"),
                            rs.getString("infoSupDay"), rs.getString("contacts"), rs.getBoolean("pickUp")));
                }
            }
        }catch (Exception e) {
            return new ResponseT("cannot get faulty item");
        }
        return new ResponseT<>(supplierList);
    }

}