package DataAccessLayer.DeliveryDataAccessLayer;

import DataAccessLayer.DeliveryDataAccessLayer.DTO.Response;
import DataAccessLayer.DeliveryDataAccessLayer.DTO.TaskDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TaskDAO extends DAO {
    private static TaskDAO instance = null;

    private TaskDAO(){
        super();

    }

    public static TaskDAO getInstance(){
        if (instance == null)
            instance = new TaskDAO();
        return instance;
    }

    /**
    because we need here deliveryID , according to the ERD , so if there is no delivery connected to the this task -
    we will send null and put in the DB -1
    and if there is delivery connected to the this task - we will use the update func to change to the current delID
    */
    public void storeTask(TaskDTO taskDTO, Response<String> deliveryID){
        String sql = "INSERT INTO Tasks(taskID, DeliveryID, loadingOrUnloading, destination) VALUES(?,?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, taskDTO.getId());
            pstmt.setString(2, deliveryID.getData());
            pstmt.setString(3, taskDTO.getLoadingOrUnloading());
            pstmt.setString(4, taskDTO.getDestination().getAddress());
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
        storeProducts(taskDTO);
    }

    public void storeProducts(TaskDTO taskDTO){
        HashMap<String, Integer> productsMap = taskDTO.getListOfProductCopy();
        Iterator it = productsMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            String sql = "INSERT INTO Products(taskID, productName, quantity) VALUES(?,?,?)";

            try (Connection conn = this.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, taskDTO.getId());
                pstmt.setString(2, (String) pair.getKey()); // TODO: need to check if this is OK, also use casting
                pstmt.setInt(3, (Integer) pair.getValue());
                pstmt.executeUpdate();
                it.remove();
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
        }
    }


    public void updateTask(TaskDTO oldTaskDTO, Response<String> delID){
        String quary = "UPDATE Tasks SET deliveryID = ? WHERE taskID = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(quary)) {
            pstmt.setString(1, delID.getData());
            pstmt.setString(2, oldTaskDTO.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public void storeAppendingTasks(ArrayList<TaskDTO> taskDTOS) {
        String sql = "INSERT INTO AppendingTasks(taskID) VALUES(?)";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (TaskDTO taskDTO : taskDTOS){
                pstmt.setString(1, taskDTO.getId());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
}
