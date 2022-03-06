package DataAccessLayer.Supplier_Inv.DAO;

import BusinessLayer.Inventory.Discount;
import BusinessLayer.Response;
import BusinessLayer.ResponseT;
import DataAccessLayer.Supplier_Inv.DTO.DiscountDTO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

public class DiscountDAO extends DAO{
    private String table;

    public DiscountDAO(String table) {
        this.table = table;
    }

    public ResponseT<List<DiscountDTO>> read() {
        String SQL = "SELECT * FROM " + table;
        List<DiscountDTO> result = new LinkedList<>();
        try {
            ResponseT<Connection> r = getConn();
            if(!r.ErrorOccured()) {
                PreparedStatement ps = r.value.prepareStatement(SQL);
                ResultSet rs = ps.executeQuery();
                while(rs.next()) {
                    result.add(new DiscountDTO(rs.getDate("start").toLocalDate(), rs.getDate("end").toLocalDate(), rs.getInt("discountPr"),rs.getInt("itemId")));
                }
            } else {
                return new ResponseT("failed to get discount");
            }
        }catch (Exception e) {
            return new ResponseT("failed to get discount");
        }
        return new ResponseT<>(result);
    }

    public ResponseT<DiscountDTO> create(Discount dis, int itemId) {
        DiscountDTO toInsert = new DiscountDTO(dis, itemId);
        String SQL = "INSERT INTO " + table + " (itemId, start, end, discountPr) VALUES (?,?,?,?)";
        try {
            ResponseT<Connection> r = getConn();
            if(!r.ErrorOccured()) {
                PreparedStatement ps = r.value.prepareStatement(SQL);
                ps.setInt(1, toInsert.getItemId());
                ps.setDate(2 , Date.valueOf(toInsert.getStart()));
                ps.setDate(3 , Date.valueOf(toInsert.getEnd()));
                ps.setInt(4, toInsert.getDiscountPr());
                ps.execute();
            }
        }catch (Exception e) {
            return new ResponseT("cannot add discount to db");
        }
        return new ResponseT(toInsert);
    }

    public Response update(Discount dis, int itemId) {
        DiscountDTO toUpdate = new DiscountDTO(dis, itemId);
        String SQL = "UPDATE " + table + " SET discountPr = ? WHERE itemId = ? AND start = ? AND end = ?";
        try {
            ResponseT<Connection> r = getConn();
            if(!r.ErrorOccured()) {
                PreparedStatement ps = r.value.prepareStatement(SQL);
                ps.setInt(1, toUpdate.getDiscountPr());
                ps.setInt(2, toUpdate.getItemId());
                ps.setDate(3, Date.valueOf(toUpdate.getStart()));
                ps.setDate(4, Date.valueOf(toUpdate.getEnd()));
                ps.execute();
            }
        }catch (Exception e) {
            return new Response("cannot update discount to db");
        }
        return new Response();
    }

    public Response delete(Discount dis, int itemId) {
        DiscountDTO toDelete = new DiscountDTO(dis, itemId);
        String SQL = "DELETE FROM  " + table + "  WHERE itemId = ? AND start = ? AND end = ?";
        try {
            ResponseT<Connection> r = getConn();
            if(!r.ErrorOccured()) {
                PreparedStatement ps = r.value.prepareStatement(SQL);
                ps.setInt(1, toDelete.getItemId());
                ps.setDate(2, Date.valueOf(toDelete.getStart()));
                ps.setDate(3, Date.valueOf(toDelete.getEnd()));
                if(!ps.execute()) {
                    return new Response("cannot delete discount from db");
                }
            }
        }catch (Exception e) {
            return new Response("cannot delete discont from db");
        }
        return new Response();
    }
}
