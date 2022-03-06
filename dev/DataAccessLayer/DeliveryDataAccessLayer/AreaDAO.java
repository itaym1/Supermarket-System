package DataAccessLayer.DeliveryDataAccessLayer;

import DataAccessLayer.DeliveryDataAccessLayer.DTO.AreaDTO;
import DataAccessLayer.DeliveryDataAccessLayer.DTO.LocationDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AreaDAO extends DAO{
    private static AreaDAO instance = null;

    private AreaDAO(){
        super();
    }
    public static AreaDAO getInstance() {
        if (instance == null)
            instance = new AreaDAO();
        return instance;
    }

    public void storeArea(AreaDTO areaDTO){
        String sql = "INSERT INTO Areas(areaName) VALUES(?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, areaDTO.getAreaName());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    // we need to add 2 more fields in it (delID, taskID).. so what im thinking is to add them now with -1 and when we... nevermined
    public void storeLocation(AreaDTO areaDTO, LocationDTO locationDTO){
        String sql = "INSERT INTO Locations(areaName, address, phoneNumber, contactName) VALUES(?,?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, areaDTO.getAreaName());
            pstmt.setString(2, locationDTO.getAddress());
            pstmt.setString(3, locationDTO.getPhoneNumber());
            pstmt.setString(4, locationDTO.getContactName());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }

        }
    }

//    public ArrayList<AreaDTO> getAreas(){
//
//    }

}
