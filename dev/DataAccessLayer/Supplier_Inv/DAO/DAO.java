package DataAccessLayer.Supplier_Inv.DAO;

import BusinessLayer.ResponseT;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class DAO {
    private static final String url = "jdbc:sqlite:dev/Suppliers_Inv/Super-Li.db";
    private static Connection conn;

    public static ResponseT<Connection> getConn() {
        if(conn == null) {
            try {
                Class.forName("org.sqlite.JDBC");
                conn = DriverManager.getConnection(url);
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                return new ResponseT<>(null, e.getMessage());
            }
        }
        return new ResponseT<>(conn);
    }
}

