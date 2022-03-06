package DataAccessLayer.Supplier_Inv.DAO;

import BusinessLayer.Response;
import BusinessLayer.ResponseT;
import DataAccessLayer.Supplier_Inv.DTO.ProductsInOrderDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ProductsInOrderDAO extends DAO {

    public Response insert(Integer orderID, Integer productID, Integer quantity, Integer supplierID) {

        String order = "INSERT INTO ProductsInOrder (orderID, productID, quantity, supplierID) VALUES (?, ?, ?, ?)";

        try {Connection conn = getConn().value;
             PreparedStatement pstmt = conn.prepareStatement(order);

            // inserting to employee table
            pstmt.setInt(1, orderID);
            pstmt.setInt(2, productID);
            pstmt.setInt(3,quantity);
            pstmt.setInt(4, supplierID);

            pstmt.execute();

        } catch (SQLException e) {
            return new Response(e.getMessage());
        }
        return new Response();
    }


    public Response insert(ProductsInOrderDTO productInOrder){
        return insert(productInOrder.getOrderID(), productInOrder.getProductID(), productInOrder.getQuantity(),
                productInOrder.getSupplierID());
    }

    public Response delete(Integer orderID, Integer productID) {
        String SQL = "DELETE FROM ProductsInOrder WHERE orderID = ? AND productID = ?";
        try {
            ResponseT<Connection> r = getConn();
            if(!r.ErrorOccured()) {
                PreparedStatement ps = r.value.prepareStatement(SQL);
                ps.setInt(1, orderID);
                ps.setInt(2, productID);

                ps.execute();
            }
        }catch (SQLException e) {
            return new Response(e.getMessage());
        }
        return new Response();
    }
    //TODO: update functions???

    public ResponseT<HashMap<Integer, Integer>> getProductsFromOrder(int orderID) {
        String SQL = "SELECT productID, quantity FROM ProductsInOrder WHERE orderID = ?";
        HashMap<Integer,Integer> prodQua = new HashMap<>();
        try {
            ResponseT<Connection> r = getConn();
            if(!r.ErrorOccured()) {
                PreparedStatement ps = r.value.prepareStatement(SQL);
                ps.setInt(1, orderID);
                ResultSet rs = ps.executeQuery();
                while(rs.next()) {
                    prodQua.put(rs.getInt("productID"), rs.getInt("quantity"));
                }
            }
        }catch (Exception e) {
            return new ResponseT("cannot read products from order");
        }
        return new ResponseT<>(prodQua);
    }


    public ResponseT<List<ProductsInOrderDTO>> read() {
        String SQL = "SELECT * FROM ProductsInOrder";
        List<ProductsInOrderDTO> pioList = new LinkedList<>();
        try {
            ResponseT<Connection> r = getConn();
            if(!r.ErrorOccured()) {
                PreparedStatement ps = r.value.prepareStatement(SQL);
                ResultSet rs = ps.executeQuery();
                while(rs.next()) {
                    pioList.add(new ProductsInOrderDTO(rs.getInt("orderID"), rs.getInt("productID"),
                            rs.getInt("quantity"), rs.getInt("supplierID")));
                }
            }
        }catch (Exception e) {
            return new ResponseT("cannot read sale");
        }
        return new ResponseT<>(pioList);
    }

    public ResponseT updateQuantity(int orderID, int productID, int quantity) {
        String SQL = "UPDATE ProductsInOrder SET quantity = ? WHERE orderID = ? AND productID = ?";
        try {
            ResponseT<Connection> r = getConn();
            if(!r.ErrorOccured()) {
                PreparedStatement ps = r.value.prepareStatement(SQL);
                ps.setInt(1, quantity);
                ps.setInt(2, orderID);
                ps.setInt(3, productID);

                ps.execute();
            }
        }catch (SQLException e) {
            return new ResponseT<>(e.getMessage());
        }
        return new ResponseT<>(null);
    }

    public Response deleteOrder(int orderID) {
        String SQL = "DELETE FROM ProductsInOrder WHERE orderID = ?";
        try {
            ResponseT<Connection> r = getConn();
            if(!r.ErrorOccured()) {
                PreparedStatement ps = r.value.prepareStatement(SQL);
                ps.setInt(1, orderID);
                ps.execute();
            }
        }catch (SQLException e) {
            return new Response(e.getMessage());
        }
        return new Response();
    }
}
