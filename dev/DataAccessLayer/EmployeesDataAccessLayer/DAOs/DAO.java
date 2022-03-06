package DataAccessLayer.EmployeesDataAccessLayer.DAOs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class DAO {
    private final String url = "jdbc:sqlite:dev/DataBase/SuperLeeDB.db";

    protected Connection getConn() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(url);
    }

}
