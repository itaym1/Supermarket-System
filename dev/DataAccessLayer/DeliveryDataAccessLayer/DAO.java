package DataAccessLayer.DeliveryDataAccessLayer;
//package net.sqlitetutorial;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class DAO {
    protected Connection conn = null;
    public DAO(){

    }



//    public void connect() {
//        Connection conn = null;
//        try {
//            // db parameters
//            String url = "jdbc:sqlite:" + System.getProperty("user.dir") + "super-li.db";
//            // create a connection to the database
//            conn = DriverManager.getConnection(url);
//
//            System.out.println("Connection to SQLite has been established.");
//
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        } finally {
//            try {
//                if (conn != null) {
//                    conn.close();
//                }
//            } catch (SQLException ex) {
//                System.out.println(ex.getMessage());
//            }
//        }
//    }

    public Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:" + System.getProperty("user.dir") + "/dev/DataBase/SuperLeeDB.db";
//        String url = "jdbc:sqlite:ADSS_Group_J/dev/DataBase/SuperLeeDB.db";

//        String url = "jdbc:sqlite:C:/Users/refae/BGU/Courses/Semester_B/Software_systems_analysis_and_design/SSAD_Project/ADSS_Group_J/dev/Delivery/DataAccessLayer/super-li.db";
//        System.out.println(System.getProperty("user.dir"));
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

//    protected Connection connect() throws SQLException {
//        String url = "jdbc:sqlite:dev/DataBase/SuperLeeDB.db";
//        try {
//            Class.forName("org.sqlite.JDBC");
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        return DriverManager.getConnection(url);
//    }


}

