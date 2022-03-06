package DataAccessLayer.EmployeesDataAccessLayer.DAOs;

import serviceObjects.Response;
import serviceObjects.ResponseT;
import DataAccessLayer.EmployeesDataAccessLayer.DTOs.EmployeeDTO;
import DataAccessLayer.EmployeesDataAccessLayer.DTOs.ShiftDTO;
import DataAccessLayer.EmployeesDataAccessLayer.Objects.Mapper;
import DataAccessLayer.EmployeesDataAccessLayer.Objects.ShiftDate;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShiftDAO extends DAO{
    private Mapper mapper;

    public ShiftDAO() {
        mapper = Mapper.getInstance();
    }

    public Response insertShift(LocalDate date, LocalTime start, LocalTime end, boolean closed){
        String sql = """
                INSERT INTO Shifts (Date, Start, End, Closed)
                VALUES
                (?, ?, ?, ?)
                """;

        try(Connection conn = getConn();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, date.toString());
            pstmt.setString(2, start.toString());
            pstmt.setString(3, end.toString());
            pstmt.setInt(4, (closed ? 1 : 0));

            pstmt.executeUpdate();

        }catch(SQLException e){
            return new Response(e.getMessage());
        }
        return new Response();
    }

    public Response insertShift(ShiftDTO shift){
        return insertShift(shift.getDate(), shift.getStart(), shift.getEnd(), shift.isClosed());
    }

    public Response addConstrain(LocalDate date, LocalTime start, LocalTime end, String empID, int preference){
        String sql = """
                INSERT INTO ShiftConstrains (Date, Start, End, EmpID, Preference)
                VALUES
                (?, ?, ?, ?, ?)
                """;

        try(Connection conn = getConn();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, date.toString());
            pstmt.setString(2, start.toString());
            pstmt.setString(3, end.toString());
            pstmt.setString(4, empID);
            pstmt.setInt(5, preference);

            pstmt.executeUpdate();

            return new Response();

        }catch(SQLException e){
            return new Response(e.getMessage());
        }
    }

    public Response addConstrain(ShiftDate shiftDate, String id, int pref){
        return addConstrain(shiftDate.getDate(), shiftDate.getStart(), shiftDate.getEnd(), id, pref);
    }

    public Response assignEmployee(LocalDate date, LocalTime start, LocalTime end, String empID, String role){
        String sql = """
                INSERT INTO ShiftAssignees (Date, Start, End, EmpID, Role)
                VALUES
                (?, ?, ?, ?, ?)
                """;

        try(Connection conn = getConn();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, date.toString());
            pstmt.setString(2, start.toString());
            pstmt.setString(3, end.toString());
            pstmt.setString(4, empID);
            pstmt.setString(5, role);

            pstmt.executeUpdate();

        }catch(SQLException e){
            return new Response(e.getMessage());
        }
        return new Response();
    }

    public Response assignEmployee(ShiftDate shiftDate, String ID, String role){
        return assignEmployee(shiftDate.getDate(), shiftDate.getStart(), shiftDate.getEnd(), ID, role);
    }

    public ResponseT<ShiftDTO> get(ShiftDate shiftDate){
        String shiftSql = """
                SELECT* FROM Shifts
                WHERE Date = ? AND Start = ? AND End = ?
                """;

        String shiftConstrainsSql = """
                SELECT* FROM ShiftConstrains
                WHERE Date = ? AND Start = ? AND End = ?
                """;

        String shiftAssigneesSql = """
                SELECT* FROM ShiftAssignees
                WHERE Date = ? AND Start = ? AND End = ?
                """;

        try(Connection conn = getConn();
            PreparedStatement shiftSqlStmt = conn.prepareStatement(shiftSql);
            PreparedStatement shiftConstrainsSqlStmt = conn.prepareStatement(shiftConstrainsSql);
            PreparedStatement shiftAssigneesSqlStmt = conn.prepareStatement(shiftAssigneesSql)){

            prepareShiftStatement(shiftSqlStmt, shiftDate);
            ResultSet shiftRs = shiftSqlStmt.executeQuery();
            shiftRs.next();
            if (shiftRs.isClosed())
                return new ResponseT<>(null, "Shift not found");
            boolean closed = shiftRs.getInt(4) == 1;

            shiftRs.close();
            // constrains part
            prepareShiftStatement(shiftConstrainsSqlStmt, shiftDate);
            ResultSet consRs = shiftConstrainsSqlStmt.executeQuery();
            ResponseT<Map<EmployeeDTO, Integer>> constrains = getMapConstrains(consRs);
            // assignees part
            prepareShiftStatement(shiftAssigneesSqlStmt, shiftDate);
            ResultSet assRs = shiftAssigneesSqlStmt.executeQuery();
            Map<String, EmployeeDTO> assignedEmployees = new HashMap<>();
            Map<String, List<EmployeeDTO>> rolesMap = new HashMap<>();
            Response r = fillAssigneesAndRoles(assRs, assignedEmployees, rolesMap);
            if (r.isErrorOccured())
                return new ResponseT<>(null, r.getErrorMessage());

            ShiftDTO shift = new ShiftDTO(shiftDate.getDate(), shiftDate.getStart(), shiftDate.getEnd(), closed,
                    constrains.getValue(), assignedEmployees, rolesMap, this);

            return new ResponseT<>(shift);

        }catch(SQLException e){
            e.printStackTrace();
            return new ResponseT<>(null, e.getMessage());
        }
    }

    private Response fillAssigneesAndRoles(ResultSet assRs, Map<String, EmployeeDTO> assignedEmployees,
                                           Map<String, List<EmployeeDTO>> rolesMap) throws SQLException {
        while (assRs.next()){
            String empId = assRs.getString(4);
            String roleName = assRs.getString(5);
            ResponseT<EmployeeDTO> employee = Mapper.getInstance().getEmployee(empId);
            if (employee.isErrorOccured())
                return employee;
            assignedEmployees.put(empId, employee.getValue());
            List<EmployeeDTO> emps = rolesMap.computeIfAbsent(roleName, k -> new ArrayList<>());
            emps.add(employee.getValue());
        }
        return new Response();
    }

    private ResponseT<Map<EmployeeDTO, Integer>> getMapConstrains(ResultSet consRs) throws SQLException {
        Map<EmployeeDTO, Integer> constrains = new HashMap<>();
        while(consRs.next()){
            String empId = consRs.getString(4);
            ResponseT<EmployeeDTO> employee = Mapper.getInstance().getEmployee(empId);
            if(employee.isErrorOccured())
                return new ResponseT<>(null, employee.getErrorMessage());
            constrains.put(employee.getValue(), consRs.getInt(5));
        }
        return new ResponseT<>(constrains);
    }

    private void prepareShiftStatement(PreparedStatement shiftSqlStmt, ShiftDate shiftDate) throws SQLException {
        shiftSqlStmt.setString(1, shiftDate.getDate().toString());
        shiftSqlStmt.setString(2, shiftDate.getStart().toString());
        shiftSqlStmt.setString(3, shiftDate.getEnd().toString());
    }

    public Response deleteAssignee(ShiftDate shiftDate, String ID){
        String sql = """
                DELETE FROM ShiftAssignees
                WHERE Date = ? AND Start = ? AND End = ? AND EmpID = ?
                """;

        try(Connection conn = getConn();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, shiftDate.getDate().toString());
            pstmt.setString(2, shiftDate.getStart().toString());
            pstmt.setString(3, shiftDate.getEnd().toString());
            pstmt.setString(4, ID);

            pstmt.executeUpdate();

        }catch(SQLException e){
            return new Response(e.getMessage());
        }
        return new Response();
    }

    public Response setClose(ShiftDate shiftDate, boolean cond){
        String sql = """
                UPDATE Shifts SET Closed = ?
                WHERE WHERE Date = ? AND Start = ? AND End = ?
                """;

        try(Connection conn = getConn();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(2, shiftDate.getDate().toString());
            pstmt.setString(3, shiftDate.getStart().toString());
            pstmt.setString(4, shiftDate.getEnd().toString());
            pstmt.setInt(4, (cond ? 1 : 0));

            pstmt.executeUpdate();

        }catch(SQLException e){
            return new Response(e.getMessage());
        }
        return new Response();
    }


}