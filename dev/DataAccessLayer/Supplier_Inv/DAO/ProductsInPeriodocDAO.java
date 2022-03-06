package DataAccessLayer.Supplier_Inv.DAO;

import BusinessLayer.Response;
import BusinessLayer.ResponseT;
import DataAccessLayer.Supplier_Inv.DTO.ProductsInPeriodicDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ProductsInPeriodocDAO extends DAO {

    public Response insert(Integer periodicID, Integer productID, Integer quantity) {

    String order = "INSERT INTO ProductsInPeriodic (PeriodicID, productID, quantity) VALUES (?, ?, ?)";

    try {
        Connection conn = getConn().value;
        PreparedStatement pstmt = conn.prepareStatement(order);

        // inserting to employee table
        pstmt.setInt(1, periodicID);
        pstmt.setInt(2, productID);
        pstmt.setInt(3,quantity);

        pstmt.execute();

    } catch (SQLException e) {
        return new Response(e.getMessage());
    }
    return new Response();
}


//    public Response insert(ProductsInOrderDTO productInOrder){
//        return insert(productInOrder.getOrderID(), productInOrder.getProductID(), productInOrder.getQuantity(),
//                productInOrder.getSupplierID());
//    }

    public Response delete(Integer periodicID) {
        String SQL = "DELETE FROM ProductsInPeriodic WHERE PeriodicID = ? ";
        try {
            ResponseT<Connection> r = getConn();
            if(!r.ErrorOccured()) {
                PreparedStatement ps = r.value.prepareStatement(SQL);
                ps.setInt(1, periodicID);

                ps.execute();
            }
        }catch (SQLException e) {
            return new Response(e.getMessage());
        }
        return new Response();
    }


    public Response deleteProductFromPeriodic(Integer periodicID, Integer productID) {
        String SQL = "DELETE FROM ProductsInPeriodic WHERE PeriodicID = ? AND productID = ? ";
        try {
            ResponseT<Connection> r = getConn();
            if(!r.ErrorOccured()) {
                PreparedStatement ps = r.value.prepareStatement(SQL);
                ps.setInt(1, periodicID);
                ps.setInt(2, productID);

                ps.execute();
            }
        }catch (SQLException e) {
            return new Response(e.getMessage());
        }
        return new Response();
    }
    //TODO: update functions???

    public ResponseT<HashMap<Integer, Integer>> getProductsFromPeriodic(int periodicID) {
        String SQL = "SELECT productID, quantity FROM ProductsInOrder WHERE PeriodicID = ?";
        HashMap<Integer,Integer> prodQua = new HashMap<>();
        try {
            ResponseT<Connection> r = getConn();
            if(!r.ErrorOccured()) {
                PreparedStatement ps = r.value.prepareStatement(SQL);
                ps.setInt(1, periodicID);
                ResultSet rs = ps.executeQuery();
                while(rs.next()) {
                    prodQua.put(rs.getInt("productID"), rs.getInt("quantity"));
                }
            }
        }catch (Exception e) {
            return new ResponseT("cannot read products from periodic order");
        }
        return new ResponseT<>(prodQua);
    }


    public ResponseT<List<ProductsInPeriodicDTO>> read() {
        String SQL = "SELECT * FROM ProductsInOrder";
        List<ProductsInPeriodicDTO> pioList = new LinkedList<>();
        try {
            ResponseT<Connection> r = getConn();
            if(!r.ErrorOccured()) {
                PreparedStatement ps = r.value.prepareStatement(SQL);
                ResultSet rs = ps.executeQuery();
                while(rs.next()) {
                    pioList.add(new ProductsInPeriodicDTO(rs.getInt("orderID"), rs.getInt("productID"),
                            rs.getInt("quantity")));
                }
            }
        }catch (Exception e) {
            return new ResponseT<>("cannot read sale");
        }
        return new ResponseT<>(pioList);
    }

    public Response editQuantityInPeriodic(Integer periodicID, Integer productID, Integer quantity) {
        String SQL = "UPDATE ProductsInPeriodic SET quantity = ? WHERE periodicID = ? AND productID = ?";
        try {
            ResponseT<Connection> r = getConn();
            if(!r.ErrorOccured()) {
                PreparedStatement ps = r.value.prepareStatement(SQL);
                ps.setInt(1, quantity);
                ps.setInt(2, periodicID);
                ps.setInt(3, productID);

                ps.execute();
            }
        }catch (SQLException e) {
            return new Response(e.getMessage());
        }
        return new Response();
    }
}
