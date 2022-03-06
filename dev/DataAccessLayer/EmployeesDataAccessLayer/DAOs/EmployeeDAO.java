package DataAccessLayer.EmployeesDataAccessLayer.DAOs;

import serviceObjects.Response;
import serviceObjects.ResponseT;
import DataAccessLayer.EmployeesDataAccessLayer.DTOs.EmployeeDTO;
import DataAccessLayer.EmployeesDataAccessLayer.DTOs.RoleDTO;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO extends DAO{

    public Response insert(EmployeeDTO employee){
        String sql = """
                INSERT INTO Employees (EmpID, Name, BankAccount, Salary, SickDays, StudyFund ,DaysOff, DateOfHire)
                VALUES
                (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try(Connection conn = getConn();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            // inserting to employee table
            pstmt.setString(1, employee.getID());
            pstmt.setString(2, employee.getName());
            pstmt.setString(3,employee.getBankAccount());
            pstmt.setInt(4,employee.getSalary());
            pstmt.setInt(5,employee.getSickDays());
            pstmt.setInt(6,employee.getAdvancedStudyFund());
            pstmt.setInt(7,employee.getDaysOff());
            pstmt.setString(8,employee.getDateOfHire().toString());
            pstmt.executeUpdate();

            // inserting the roles:
            for(RoleDTO role: employee.getRoles())
                addRole(employee.getID(), role.getName(), role.getLicense(), conn);

        }catch(SQLException e){
            return new Response(e.getMessage());
        }
        return new Response();
    }

    public Response update(EmployeeDTO employeeDTO){
        String sql = """
                UPDATE Employees
                SET Name = ?,
                    BankAccount = ?,
                    Salary = ?,
                    SickDays = ?,
                    StudyFund = ?,
                    DaysOff = ?
                WHERE EmpID = ?
                """;

        try(Connection conn = getConn();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, employeeDTO.getName());
            pstmt.setString(2, employeeDTO.getBankAccount());
            pstmt.setInt(3, employeeDTO.getSalary());
            pstmt.setInt(4, employeeDTO.getSickDays());
            pstmt.setInt(5, employeeDTO.getAdvancedStudyFund());
            pstmt.setInt(6, employeeDTO.getDaysOff());
            pstmt.setString(7, employeeDTO.getID());

            pstmt.executeUpdate();

        }catch(SQLException e){
            return new Response(e.getMessage());
        }

        return new Response();
    }

    private void addRole(String ID, String role, Integer driverLicence, Connection conn) throws SQLException {
        String sql = """
                INSERT INTO RolesEmployees (EmpID, RoleName, DriverLicence)
                VALUES
                (?, ?, ?)
                """;

        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, ID);
        pstmt.setString(2, role);
        if (driverLicence != null)
            pstmt.setString(3, String.valueOf(driverLicence));
        else pstmt.setString(3, null);
        pstmt.executeUpdate();
    }

    public Response addRole(String ID, String role, Integer driverLicence){
        try(Connection conn = getConn()){
            addRole(ID, role, driverLicence, conn);

        }catch(SQLException e){
            return new Response(e.getMessage());
        }
        return new Response();
    }

    public Response addRole(String ID, String role){
        return addRole(ID, role, null);
    }

    public Response addRole(String ID, RoleDTO role){
        return addRole(ID, role.getName(), role.getLicense());
    }

    public ResponseT<EmployeeDTO> get(String id){
        String empSql = String.format("""
                SELECT* FROM Employees
                WHERE EmpID = %s
                """, id);

        String rolesSql = String.format("""
                SELECT RoleName, DriverLicence
                FROM RolesEmployees
                WHERE EmpID = %s
                """, id);

        try(Connection conn = getConn();
            Statement empStmt = conn.createStatement();
            Statement rolesStmt = conn.createStatement();
            ResultSet empRs = empStmt.executeQuery(empSql);
            ResultSet rolesRs = rolesStmt.executeQuery(rolesSql)){

            List<RoleDTO> roles = new ArrayList<>();
            while(rolesRs.next())
                roles.add(new RoleDTO(rolesRs.getString(1), rolesRs.getInt(2)));

            empRs.next();
            if(empRs.isClosed())
                return new ResponseT<>(null, String.format("ID %s not found", id));
            LocalDate date =  LocalDate.parse(empRs.getString("DateOfHire"));
            EmployeeDTO employee = new EmployeeDTO(empRs.getString("Name"), id,
                    empRs.getString("BankAccount"), empRs.getInt("Salary"),
                    empRs.getInt("SickDays"), empRs.getInt("StudyFund"),
                    empRs.getInt("DaysOff"), date, roles, this);

            return new ResponseT<>(employee);

        }catch(SQLException e){
            e.printStackTrace();
            return new ResponseT<>(null, e.getMessage());
        }

    }

    public Response addNotification(String id, String msg) {
        String sql = """
                INSERT INTO Notifications (EmpID, Message)
                VALUES
                (?, ?)
                """;
        try (Connection conn = getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql))
        {

            pstmt.setString(1, id);
            pstmt.setString(2, msg);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return new Response(e.getMessage());
        }
        return new Response();
    }

    public ResponseT<List<String>> getNotifications(String id){
        String sql = """
                SELECT Message
                FROM Notifications
                WHERE EmpID == ?
                """;

        try (Connection conn = getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            List<String> msgs = new ArrayList<>();
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                msgs.add(rs.getString(1));
            }
            return new ResponseT<>(msgs);

        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseT(null, e.getMessage());
        }
    }

}