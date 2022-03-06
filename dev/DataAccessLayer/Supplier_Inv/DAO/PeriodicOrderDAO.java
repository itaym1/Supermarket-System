package DataAccessLayer.Supplier_Inv.DAO;

import BusinessLayer.Response;
import BusinessLayer.ResponseT;
import DataAccessLayer.Supplier_Inv.DTO.PeriodicOrderDTO;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;

public class PeriodicOrderDAO extends DAO{

    public Response insert(Integer orderID, LocalDate supplyDate, Integer intervals) {

        String order = "INSERT INTO PeriodicOrder(orderID, supplyDate, intervals) VALUES (?, ?, ?)";

        try {Connection conn = getConn().value;
             PreparedStatement pstmt = conn.prepareStatement(order);

            // inserting to employee table
            pstmt.setInt(1, orderID);
            pstmt.setDate(2, Date.valueOf(supplyDate));
            pstmt.setInt(3,intervals);

            pstmt.execute();

        } catch (SQLException e) {
            return new Response(e.getMessage());
        }
        return new Response();
    }


    public Response insert(PeriodicOrderDTO periodic){
        return insert(periodic.getOrderID(), periodic.getSupplyDate(), periodic.getIntervals());
    }

    public Response delete(Integer periodicID) {
        String SQL = "DELETE FROM PeriodicOrder WHERE orderID = ?";
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

    public Response updateInterval(Integer periodicID, Integer interval) {
        String SQL = "UPDATE PeriodicOrder SET intervals = ? WHERE orderID = ?";
        try {
            ResponseT<Connection> r = getConn();
            if(!r.ErrorOccured()) {
                PreparedStatement ps = r.value.prepareStatement(SQL);
                ps.setInt(1, interval);
                ps.setInt(2, periodicID);

                ps.execute();
            }
        }catch (SQLException e) {
            return new Response(e.getMessage());
        }
        return new Response();
    }



    public Response deleteProduct(Integer periodicID, Integer productID) {
        String SQL = "DELETE FROM PeriodicOrder WHERE orderID = ? AND productID = ?";
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

    public ResponseT<HashMap<Integer,PeriodicOrderDTO>> read() {
        String SQL = "SELECT * FROM PeriodicOrder";
        HashMap<Integer,PeriodicOrderDTO> poList = new HashMap<>();
        try {
            ResponseT<Connection> r = getConn();
            if(!r.ErrorOccured()) {
                PreparedStatement ps = r.value.prepareStatement(SQL);
                ResultSet rs = ps.executeQuery();
                while(rs.next()) {
                    poList.put(rs.getInt("orderID"),new PeriodicOrderDTO(rs.getInt("orderId"), rs.getDate("supplyDate").toLocalDate(),
                            rs.getInt("intervals")));
                }
            }
        }catch (Exception e) {
            return new ResponseT("cannot read sale");
        }
        return new ResponseT<>(poList);
    }


    public ResponseT<HashMap<Integer, Integer>> getProductsFromPeriod(int periodID) {
        String SQL = "SELECT ProductID, quantity FROM ProductsInPeriodic WHERE PeriodicID = ?";
        HashMap<Integer,Integer> prodQua = new HashMap<>();
        try {
            ResponseT<Connection> r = getConn();
            if(!r.ErrorOccured()) {
                PreparedStatement ps = r.value.prepareStatement(SQL);
                ps.setInt(1, periodID);
                ResultSet rs = ps.executeQuery();
                while(rs.next()) {
                    prodQua.put(rs.getInt("ProductID"), rs.getInt("quantity"));
                }
            }
        }catch (Exception e) {
            return new ResponseT<>("cannot read products from order");
        }
        return new ResponseT<>(prodQua);
    }
    //add product TODO: maybe delete this function
//    public Response addProduct(PeriodicOrder period, Integer productID, Integer quantity) {
//        String supplier = "INSERT INTO PeriodicOrder (orderID, supplyDate, intervals, productID, quantity) VALUES (?, ?, ?, ?, ?)";
//
//        try (Connection conn = getConn().value;
//             PreparedStatement pstmt = conn.prepareStatement(supplier);) {
//
//            // inserting to employee table
//            pstmt.setInt(1, period.getpOrderID());
//            pstmt.setDate(2, Date.valueOf(period.getDateOfSupply()));
//            pstmt.setInt(3,period.getInterval());
//            pstmt.setInt(4, productID);
//            pstmt.setInt(5, quantity);
//
//            pstmt.executeUpdate();
//
//        } catch (SQLException e) {
//            return new Response(e.getMessage());
//        }
//        return new Response();
//    }
}
