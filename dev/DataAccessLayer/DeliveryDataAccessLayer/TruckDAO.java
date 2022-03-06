package DataAccessLayer.DeliveryDataAccessLayer;

import DataAccessLayer.DeliveryDataAccessLayer.DTO.TruckDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TruckDAO extends DAO {
    private static TruckDAO instance = null;
    private TruckDAO(){
        super();

    }

    public static TruckDAO getInstance() {
        if (instance == null)
            instance = new TruckDAO();
        return instance;
    }

    public void storeTruck(TruckDTO truckDTO){
        String sql = "INSERT INTO Trucks(id, model, maxWeight, truckWeight) VALUES(?,?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, truckDTO.getId());
            pstmt.setString(2, truckDTO.getModel());
            pstmt.setInt(3, truckDTO.getMaxWeight());
            pstmt.setInt(4, truckDTO.getTruckWeight());
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

}
