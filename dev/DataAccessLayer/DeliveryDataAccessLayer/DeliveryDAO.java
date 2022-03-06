package DataAccessLayer.DeliveryDataAccessLayer;

import BusinessLayer.DeliveryBusinessLayer.Delivery;
import DataAccessLayer.DeliveryDataAccessLayer.DTO.DeliveryDTO;

import java.sql.*;

public class DeliveryDAO extends DAO {
    private static DeliveryDAO instance = null;

    private DeliveryDAO() {
        super();
    }

    public static DeliveryDAO getInstance() {
        if (instance == null)
            instance = new DeliveryDAO();
        return instance;
    }

    public void storeDelivery(DeliveryDTO deliveryDTO) {
        String sql = "INSERT INTO Deliveries(id, date, timeOfDeparture, truckNumber, driverName, departureWeight, modification, origin) VALUES(?,?,?,?,?,?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, deliveryDTO.getId());
            pstmt.setString(2, deliveryDTO.getDate()); // TODO: need to check if this OK !
            pstmt.setString(3, deliveryDTO.getTimeOfDeparture()); // TODO: need to check if this OK !
            pstmt.setString(4, deliveryDTO.getTruckNumber());
            pstmt.setString(5, deliveryDTO.getDriverName());
            pstmt.setInt(6, deliveryDTO.getDepartureWeight());
            pstmt.setString(7, deliveryDTO.getModification());
            pstmt.setString(8, deliveryDTO.getOrigin().getAddress());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        // todo - update tasks delivery ids
    }

    public DeliveryDTO getDeliveryByID(String deliveryId) {
        String sql = "SELECT * FROM DELIVERIES WHERE DELIVERIES.ID = (?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, deliveryId);

            ResultSet rs = pstmt.executeQuery();
            String date = rs.getDate(2).toString();
//            return new DeliveryDTO(rs.getString(1))
            // TODO need to add here more details to send back a DeliveryDTO

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;

    }

    public void updateDeliveryDW(DeliveryDTO ddto) {
        String query = """
                UPDATE Deliveries SET departureWeight = ? WHERE id = ?
                """;
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, ddto.getDepartureWeight());
            pstmt.setString(2, ddto.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public void updateDeliveryModif(Delivery toUpdate, String strModify) {
        String query = """
                UPDATE Deliveries SET modification = ? WHERE id = ?
                """;
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, strModify);
            pstmt.setString(2, toUpdate.getID());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    public void insertOrder(){
//
//    }
}