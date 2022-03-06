package DataAccessLayer.Supplier_Inv.DAO;

import BusinessLayer.Inventory.FaultyItem;
import BusinessLayer.Response;
import BusinessLayer.ResponseT;
import DataAccessLayer.Supplier_Inv.DTO.FaultyItemDTO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

public class FaultyItemDAO extends DAO{
    public FaultyItemDAO() {

    }

    public ResponseT<List<FaultyItemDTO>> read() {
        String SQL = "SELECT * FROM faultyItem";
        List<FaultyItemDTO> faultyList = new LinkedList<>();
        try {
            ResponseT<Connection> r = getConn();
            if(!r.ErrorOccured()) {
                PreparedStatement ps = r.value.prepareStatement(SQL);
                ResultSet rs = ps.executeQuery();
                while(rs.next()) {
                    faultyList.add(new FaultyItemDTO(rs.getInt("itemID"), rs.getDate("expDate").toLocalDate(), rs.getInt("amount")));
                }
            }
        }catch (Exception e) {
            return new ResponseT("cannot get faulty item");
        }
        return new ResponseT<>(faultyList);
    }

    public ResponseT<FaultyItemDTO> create(FaultyItem fi) {
        FaultyItemDTO toInsert = new FaultyItemDTO(fi);
        String SQL = "INSERT INTO faultyItem (itemId, expDate, amount) VALUES (?,?,?)";
        try {
            ResponseT<Connection> r = getConn();
            if(!r.ErrorOccured()) {
                PreparedStatement ps = r.value.prepareStatement(SQL);
                ps.setInt(1, toInsert.getItemId());
                ps.setDate(2 , Date.valueOf(toInsert.getExpDate()));
                ps.setInt(3 , toInsert.getAmount());
                ps.execute();
            }
        }catch (Exception e) {
            return new ResponseT("cannot add faulty item to db");
        }
        return new ResponseT(toInsert);
    }

    public Response update(FaultyItem fi) {
        FaultyItemDTO toUpdate = new FaultyItemDTO(fi);
        String SQL = "UPDATE faultyItem SET amount = ? WHERE itemId = ? AND expDate = ?";
        try {
            ResponseT<Connection> r = getConn();
            if(!r.ErrorOccured()) {
                PreparedStatement ps = r.value.prepareStatement(SQL);
                ps.setInt(1, toUpdate.getAmount());
                ps.setInt(2, toUpdate.getItemId());
                ps.setDate(3, Date.valueOf(toUpdate.getExpDate()));
                ps.execute();
            }
        }catch (Exception e) {
            return new Response("cannot update faulty item to db");
        }
        return new Response();
    }

    public Response delete(FaultyItem fi) {
        FaultyItemDTO toDelete = new FaultyItemDTO(fi);
        String SQL = "DELETE FROM faultyItem WHERE itemId = ? AND expDate = ?";
        try {
            ResponseT<Connection> r = getConn();
            if(!r.ErrorOccured()) {
                PreparedStatement ps = r.value.prepareStatement(SQL);
                ps.setInt(1, toDelete.getItemId());
                ps.setDate(2, Date.valueOf(toDelete.getExpDate()));
                if(!ps.execute()) {
                    return new Response("cannot delete faulty item from db");
                }
            }
        }catch (Exception e) {
            return new Response("cannot delete faulty item from db");
        }
        return new Response();
    }

}
