package DataAccessLayer.Supplier_Inv.DAO;

import BusinessLayer.Inventory.Item;
import BusinessLayer.Response;
import BusinessLayer.ResponseT;
import DataAccessLayer.Supplier_Inv.DTO.ItemDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

public class ItemDAO extends DAO {

    public ResponseT<List<ItemDTO>> read() {
        String SQL = "SELECT * FROM Item";
        List<ItemDTO> result = new LinkedList<>();
        try {
            ResponseT<Connection> r = getConn();
            if(!r.ErrorOccured()) {
                PreparedStatement ps = r.value.prepareStatement(SQL);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    ItemDTO toAdd = new ItemDTO(rs.getInt("itemId"), rs.getString("name"),
                            rs.getDouble("price"), rs.getDouble("cost"), rs.getInt("shelfNum"), rs.getString("manufacturer"),
                            rs.getInt("shelfQuantity"), rs.getInt("storageQuantity"), rs.getInt("minAlert"));
                    result.add(toAdd);
                }
            }
        }catch (Exception e) {
            return new ResponseT("failed to get items");
        }
        return new ResponseT<List<ItemDTO>>(result);
    }

    public ResponseT<ItemDTO> create(Item item) {
        ItemDTO toInsert = new ItemDTO(item);
        String SQL = "INSERT INTO Item (itemId, name, price, shelfNum, manufacturer, shelfQuantity, storageQuantity, minAlert, cost) VALUES (?,?,?,?,?,?,?,?,?)";
        try {
            ResponseT<Connection> r = getConn();
            if(!r.ErrorOccured()) {
                PreparedStatement ps = r.value.prepareStatement(SQL);
                ps.setInt(1, toInsert.getId());
                ps.setString(2, toInsert.getName());
                ps.setDouble(3, toInsert.getPrice());
                ps.setInt(4, toInsert.getShelfNum());
                ps.setString(5, toInsert.getManufacturer());
                ps.setInt(6, toInsert.getShelfQuantity());
                ps.setInt(7, toInsert.getStorageQuantity());
                ps.setInt(8, toInsert.getMinAlert());
                ps.setDouble(9, toInsert.getCost());
                ps.execute();
            }
        }catch (Exception e) {
            return new ResponseT("cannot add item to db");
        }
        return new ResponseT(toInsert);
    }

    public ResponseT<ItemDTO> update(Item item) {
        ItemDTO toUpdate = new ItemDTO(item);
        String SQL = "UPDATE item SET name = ?, price = ?, shelfNum = ?, manufacturer = ?, shelfQuantity = ?, storageQuantity = ?, cost = ?, minAlert = ? WHERE itemId = ? ";
        try {
            ResponseT<Connection> r = getConn();
            if(!r.ErrorOccured()) {
                PreparedStatement ps = r.value.prepareStatement(SQL);
                ps.setString(1, toUpdate.getName());
                ps.setDouble(2, toUpdate.getPrice());
                ps.setInt(3, toUpdate.getShelfNum());
                ps.setString(4, toUpdate.getManufacturer());
                ps.setInt(5, toUpdate.getShelfQuantity());
                ps.setInt(6, toUpdate.getStorageQuantity());
                ps.setDouble(7 , toUpdate.getCost());
                ps.setInt(8, toUpdate.getMinAlert());
                ps.setInt(9, toUpdate.getId());
                ps.execute();
            }
        }catch (Exception e) {
            return new ResponseT("cannot update item to db");
        }
        return new ResponseT(toUpdate);
    }

    public Response delete(Item item) {
        ItemDTO toDelete = new ItemDTO(item);
        String SQL = "DELETE FROM item WHERE itemId = ?";
        try {
            ResponseT<Connection> r = getConn();
            if(!r.ErrorOccured()) {
                PreparedStatement ps = r.value.prepareStatement(SQL);
                ps.setInt(1, toDelete.getId());
                ps.execute();
            }
        }catch (Exception e) {
            return new Response("cannot delete item from db");
        }
        return new Response();
    }
}
