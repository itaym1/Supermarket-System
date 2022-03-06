package DataAccessLayer.Supplier_Inv.DAO;

import BusinessLayer.Response;
import BusinessLayer.ResponseT;
import BusinessLayer.Supplier.Order;
import DataAccessLayer.Supplier_Inv.DTO.OrderDTO;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;

public class OrderDAO extends DAO {


    public ResponseT<OrderDTO> insert(Integer orderID, Integer supplierID, boolean delivered, LocalDate supplyDate, double price) {

        String order = "INSERT INTO Orders (orderID, supplierID, delivered, supplyDate, price) VALUES (?, ?, ?, ?, ?)";
        OrderDTO orderDTO = new OrderDTO(orderID, supplierID, delivered, supplyDate, price);
        try {Connection conn = getConn().value;
             PreparedStatement pstmt = conn.prepareStatement(order);

            // inserting to employee table
            pstmt.setInt(1, orderID);
            pstmt.setInt(2, supplierID);
            pstmt.setBoolean(3, delivered);
            pstmt.setDate(4, Date.valueOf(supplyDate));
            pstmt.setDouble(5,price);


            pstmt.execute();

        } catch (SQLException e) {
            return new ResponseT<>(e.getMessage());
        }
        return new ResponseT<>(orderDTO);
    }


    public ResponseT<OrderDTO> insert(Order order) {
        return insert(order.getOrderID(), order.getSupplierID(), order.isDelivered(), order.getDate(), order.getPrice());
    }

    //SELECT
    public ResponseT<OrderDTO> get(Integer orderID) {
        String orderSQL = String.format("SELECT * FROM Orders WHERE orderID = %s", orderID);

        try {Connection conn = getConn().value;
             Statement ordStmt = conn.createStatement();
             ResultSet ordRs = ordStmt.executeQuery(orderSQL);

            if (ordRs.isClosed())
                return new ResponseT<>(null, String.format("orderID %s not found", orderID));
            OrderDTO order = new OrderDTO(ordRs.getInt("orderID"),
                    ordRs.getInt("supplierID"), ordRs.getBoolean("delivered"),
                    ordRs.getDate("supplyDate").toLocalDate(), ordRs.getFloat("price"));

            return new ResponseT<>(order);

        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseT<>(null, e.getMessage());
        }

    }

    public Response delete(Integer orderID) {
        String SQL = "DELETE FROM Orders WHERE orderID = ?";
        try {
            ResponseT<Connection> r = getConn();
            if (!r.ErrorOccured()) {
                PreparedStatement ps = r.value.prepareStatement(SQL);
                ps.setInt(1, orderID);

                ps.execute();
            }
        } catch (SQLException e) {
            return new Response(e.getMessage());
        }
        return new ResponseT<>(orderID);
    }
    //TODO: update functions ???????

    public ResponseT<HashMap<Integer, OrderDTO>> read() {
        String SQL = "SELECT * FROM Orders";
        HashMap<Integer, OrderDTO> orderList = new HashMap<>();
        try {
            ResponseT<Connection> r = getConn();
            if (!r.ErrorOccured()) {
                PreparedStatement ps = r.value.prepareStatement(SQL);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    orderList.put(rs.getInt("orderID"), new OrderDTO(rs.getInt("orderID"), rs.getInt("supplierID"),
                            rs.getBoolean("delivered"), rs.getDate("supplyDate").toLocalDate(),
                            rs.getDouble("price")));
                }
            }
        } catch (Exception e) {
            return new ResponseT("cannot get orders");
        }
        return new ResponseT<>(orderList);
    }

    public void updatePrice(int orderID, double price) {
        String sql = "UPDATE Orders SET price = ? WHERE orderID = ?";
        try {
            //Connection conn = getConn().value;
            ResponseT<Connection> r = getConn();
            if (!r.ErrorOccured()) {
                PreparedStatement ps = r.value.prepareStatement(sql);
                //PreparedStatement ps = conn.prepareStatement(sql);
                ps.setDouble(1, price);
                ps.setInt(2, orderID);

                ps.execute();
            }
        }
         catch (SQLException e) {
            System.out.println(e.getErrorCode() + " " + e.getMessage());
            //return new Response(e.getMessage());
        }
    }

    public ResponseT<Integer> getNextOrderID() {
        String SQL = "SELECT MAX(orderID) FROM Orders";
        ResponseT<Integer> orderID = new ResponseT<>(0);
        try {
            ResponseT<Connection> r = getConn();
            if (!r.ErrorOccured()) {
                PreparedStatement ps = r.value.prepareStatement(SQL);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    orderID = new ResponseT<>(rs.getInt("MAX(orderID)"));
                }
            }
        } catch (Exception e) {
            return new ResponseT("cannot get orders");
        }
        return orderID;
    }
}
