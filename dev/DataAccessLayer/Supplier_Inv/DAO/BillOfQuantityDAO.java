package DataAccessLayer.Supplier_Inv.DAO;

import BusinessLayer.Response;
import BusinessLayer.ResponseT;
import DataAccessLayer.Supplier_Inv.DTO.BillOfQuantityDTO;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class BillOfQuantityDAO extends DAO {


    public Response insert(Integer supplierID, Integer productID, Integer minQuantity, Integer percentDiscount) {

        String billOfQuantity = "INSERT INTO BillOfQuantity (supplierID, productID, minQuantity, precentDiscount) VALUES (?, ?, ?, ?)";

        try  {
            Connection conn = getConn().value;
            PreparedStatement pstmt = conn.prepareStatement(billOfQuantity);
            // inserting to employee table
            pstmt.setInt(1, supplierID);
            pstmt.setInt(2, productID);
            pstmt.setInt(3, minQuantity);
            pstmt.setInt(4,percentDiscount);

            pstmt.execute();

        } catch (SQLException e) {
            return new Response(e.getMessage());
        }
        return new Response();
    }

    public Response insert(BillOfQuantityDTO boq){
        return insert(boq.getSupplierID(),boq.getProductID(), boq.getMinQuantity(), boq.getPercentDiscount());
    }


    //SELECT
    public ResponseT<BillOfQuantityDTO> get(Integer supplierID){
        String billSQL = String.format("SELECT* FROM BillOfQuantity WHERE supplierID = %s", supplierID);

        try{
            Connection conn = getConn().value; //TODO
            Statement billStmt = conn.createStatement();
            ResultSet billRs = billStmt.executeQuery(billSQL);//
            if(billRs.isClosed())
                return new ResponseT<>(null, String.format("supplierID %s not found", supplierID));
            BillOfQuantityDTO bill = new BillOfQuantityDTO(billRs.getInt("supplierID"),
                    billRs.getInt("productID"), billRs.getInt("minQuantity"),
                    billRs.getInt("precentDiscount"));

            return new ResponseT<>(bill);

        }catch(SQLException e){
            e.printStackTrace();
            return new ResponseT<>(null, e.getMessage());
        }

    }

    public Response delete(Integer supplierID) {
        String SQL = "DELETE FROM BillOfQuantity WHERE supplierID = ?";
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

    public Response deleteProduct(Integer supplierID, Integer prodID) {
        String SQL = "DELETE FROM BillOfQuantity WHERE supplierID = ? AND productID = ?";
        try {
            ResponseT<Connection> r = getConn();
            if(!r.ErrorOccured()) {
                PreparedStatement ps = r.value.prepareStatement(SQL);
                ps.setInt(1, supplierID);
                ps.setInt(2, prodID);

                ps.execute();
            }
        }catch (SQLException e) {
            return new Response(e.getMessage());
        }
        return new ResponseT(supplierID);
    }


    public Response updateMinQuantity(Integer supplierID, Integer productID, Integer newMinQ) {
        String SQL = "UPDATE BillOfQuantity SET minQuantity = ? WHERE supplierID = ? AND productID = ?";
        try {
            ResponseT<Connection> r = getConn();
            if(!r.ErrorOccured()) {
                PreparedStatement ps = r.value.prepareStatement(SQL);
                ps.setInt(1, newMinQ);
                ps.setInt(2, supplierID);
                ps.setInt(3, productID);

                ps.execute();
            }
        }catch (SQLException e) {
            return new Response(e.getMessage());
        }
        return new Response();
    }

    public Response updatePercentDiscount(Integer supplierID, Integer productID, Integer newPercent) {
        String SQL = "UPDATE BillOfQuantity SET precentDiscount = ? WHERE supplierID = ? AND productID = ?";
        try {
            ResponseT<Connection> r = getConn();
            if(!r.ErrorOccured()) {
                PreparedStatement ps = r.value.prepareStatement(SQL);
                ps.setInt(1, newPercent);
                ps.setInt(2, supplierID);
                ps.setInt(3, productID);

                ps.execute();
            }
        }catch (SQLException e) {
            return new Response(e.getMessage());
        }
        return new Response();
    }

    public ResponseT<List<BillOfQuantityDTO>> read() {
        String SQL = "SELECT * FROM BillOfQuantity";
        List<BillOfQuantityDTO> boqList = new LinkedList<>();
        try {
            ResponseT<Connection> r = getConn();
            if(!r.ErrorOccured()) {
                PreparedStatement ps = r.value.prepareStatement(SQL);
                ResultSet rs = ps.executeQuery();
                while(rs.next()) {
                    boqList.add(new BillOfQuantityDTO(rs.getInt("supplierID"), rs.getInt("productID"),
                            rs.getInt("minQuantity"), rs.getInt("precentDiscount")));
                }
            }
        }catch (Exception e) {
            return new ResponseT("cannot read sale");
        }
        return new ResponseT<>(boqList);
    }
}
