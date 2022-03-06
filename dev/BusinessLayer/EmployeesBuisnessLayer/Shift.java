package BusinessLayer.EmployeesBuisnessLayer;
import DataAccessLayer.EmployeesDataAccessLayer.DAOs.ShiftDAO;
import DataAccessLayer.EmployeesDataAccessLayer.DTOs.EmployeeDTO;
import DataAccessLayer.EmployeesDataAccessLayer.DTOs.ShiftDTO;
import DataAccessLayer.EmployeesDataAccessLayer.Objects.ShiftDate;
import serviceObjects.Response;
import serviceObjects.ResponseT;

import java.time.LocalDate;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public abstract class Shift {

    protected LocalDate date;
    protected LocalTime start;
    protected LocalTime end;
    private boolean closed;
    private HashMap<Employee, Integer> constrains;
    private HashMap<String, Employee> assignedEmployees;
    private HashMap<String, List<Employee>> assignedRolesEmp; //Yanay's Plaster for getting the specific roles that emps assigned to..
    protected ShiftDTO dto;

    Shift(LocalDate _date, ShiftDAO _dao) {
        closed = false; // **added default to be false by Yanay.
        setStart();
        setEnd();
        date = _date;
        constrains = new HashMap<Employee, Integer>();
        assignedEmployees = new HashMap<String, Employee>();
        assignedRolesEmp = new HashMap<>();
        dto = new ShiftDTO(date, start, end, closed, getConstrainsDTO(), getAssigneesDTO(), getRolesMap(), _dao);
        dto.persist();
    }

    Shift(ShiftDTO dto){
        closed = dto.isClosed();
        setStart();
        setEnd();
        date = dto.getDate();

        constrains = new HashMap<>();
        for (Map.Entry<EmployeeDTO, Integer> e : dto.getConstrains().entrySet())
            constrains.put(new Employee(e.getKey()), e.getValue());

        assignedEmployees = new HashMap<>();
        for(Map.Entry<String, EmployeeDTO> entry : dto.getAssignedEmployees().entrySet())
            assignedEmployees.put(entry.getKey(), new Employee(entry.getValue()));

        assignedRolesEmp = new HashMap<>();
        for(Map.Entry<String, List<EmployeeDTO>> entry : dto.getRolesMap().entrySet())
            assignedRolesEmp.put(entry.getKey(),
                    entry.getValue().stream().map(Employee::new).collect(Collectors.toList()));
        this.dto = dto;
    }

    private Map<EmployeeDTO, Integer> getConstrainsDTO() {
        Map<EmployeeDTO, Integer> constraintsDTO = new HashMap<>();
        for (Map.Entry<Employee, Integer> entry : constrains.entrySet())
            constraintsDTO.put(entry.getKey().getDTO(), entry.getValue());
        return constraintsDTO;
    }

    private Map<String, EmployeeDTO> getAssigneesDTO() {
        Map<String, EmployeeDTO> assigneesDTO = new HashMap<>();
        for (Map.Entry<String, Employee> entry : assignedEmployees.entrySet())
            assigneesDTO.put(entry.getKey(), entry.getValue().getDTO());
        return assigneesDTO;
    }

    private Map<String, List<EmployeeDTO>> getRolesMap() {
        Map<String, List<EmployeeDTO>> rolesMap = new HashMap<>();
        for (Map.Entry<String, List<Employee>> entry : assignedRolesEmp.entrySet()){
            List<EmployeeDTO> dtos = entry.getValue().stream().map(Employee::getDTO).collect(Collectors.toList());
            rolesMap.put(entry.getKey(), dtos);
        }
        return rolesMap;
    }

    public abstract Shift clone();

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStart() {
        return start;
    }

    public abstract void setStart();

    public LocalTime getEnd() {
        return end;
    }

    public abstract void setEnd();


    public boolean isClosed() {
        return closed;
    }

    public Response close() {
        if(!hasManager())
            return new Response("Can not close before a shift manager is assigned to the shift");
        closed = true;
        return dto.setClosed(true);
    }

    protected boolean hasManager(){
        for(Map.Entry<String, Employee> entry: assignedEmployees.entrySet()){
            if(entry.getValue().haveRoleCheck("HR Manager") || entry.getValue().haveRoleCheck("Shift Manager"))
                return true;
        }
        return false;
    }

    public Response open() {
        closed = false;
        dto.setClosed(false);
        return new Response();
    }

    public Response AddConstrain(Employee employee, int con){
        if (isClosed())
            return new Response("Shift already closed");
        if (con > 2 || con < 0)
            return new Response("Invalid preference");
        constrains.put(employee, con);
        return dto.addConstrain(employee.getDTO(), con);
    }

//    public boolean compare(LocalDate date, int StartTime, int EndTime){
//        return (date.equals(this.date) && StartTime== this.start && EndTime == this.end);
//    }

    public boolean compare(LocalDate date, LocalTime StartTime, LocalTime EndTime){
        return (date.equals(this.date) && StartTime.compareTo(start) == 0  && EndTime.compareTo(end) == 0);
    }

    public ResponseT<String> getShiftConstrainsString() {
        StringBuilder res = new StringBuilder();
        for (Employee emp : constrains.keySet()){
            res.append("ID: ").append(emp.getID().getValue()).append(" | Name: ").append(emp.getName().getValue())
                    .append(" | Preference: ").append(this.constrains.get(emp)).append("\n");
        }
        return new ResponseT<String>(res.toString());

    }

    public Response AssignEmployee(Employee e, String role){
        if (isClosed())
            return new Response("Shift already closed");
        assignedEmployees.put(e.getID().getValue(), e);
        List<Employee> employees = assignedRolesEmp.computeIfAbsent(role, k -> new ArrayList<>());
        employees.add(e);
        return dto.AssignEmployee(e.getDTO(), role);
    }

    public ResponseT<List<Employee>> getAllAssignedEmployees(){
        List<Employee> employees = new ArrayList<>();
        for(Map.Entry<String, Employee> entry: assignedEmployees.entrySet())
            employees.add(new Employee(entry.getValue()));
        return new ResponseT<>(employees);
    }

    public ResponseT<List<String>> getAllAssignedRolesForEmployeeInShift(String id){
        List<String> assigRoles = new ArrayList<>();
        for(Map.Entry<String, List<Employee>> entry: assignedRolesEmp.entrySet()){
            if (entry.getValue().stream().anyMatch(e2 -> e2.getID().getValue().equals(id)))
                assigRoles.add(entry.getKey());
        }
        return new ResponseT<>(assigRoles);
    }

    public ResponseT<String> getWhoIWorkWith(Employee employee){
        if (!assignedEmployees.containsKey(employee.getID().getValue()))
            return new ResponseT<>(null, "This employee is not assigned to this shift");
        return new ResponseT<String>(getIdsNames());
    }

    protected String getIdsNames(){
        StringBuilder sb = new StringBuilder();
        sb.append("|\t").append(date.toString()).append('\n');
        sb.append("|\t").append(start.toString()).append('\n');
        sb.append("|\t").append(end.toString()).append("\n\n");
        for(Map.Entry<String,Employee> entry: assignedEmployees.entrySet()) {
            sb.append(entry.getKey()).append('\t').append(entry.getValue().getName().getValue()).append('\n');
        }
        sb.deleteCharAt(sb.length()-1); // remove the last \n
        return sb.toString();
    }

    public ResponseT<String> getEmpPreferences(Employee employee){
        if (!constrains.containsKey(employee))
            return new ResponseT("Employee did not assign into this shift");
        int pref = constrains.get(employee);
        switch (pref){
            case 0:
                return new ResponseT("The preference to this shift is: Can't work on this shift");
            case 1:
                return new ResponseT("The preference to this shift is: Can work on this shift");
            case 2:
                return new ResponseT("The preference to this shift is: Want work on this shift");
            default:
                return new ResponseT(null, "for some reason, we reached the default"); // never happens
        }
    }

    public boolean isAssigned(Employee employee){
        return assignedEmployees.containsKey(employee.getID().getValue());
    }

    public Response removeEmp(Employee employee){
        if (!isAssigned(employee))
            return new Response("Employee is not assigned to this shift");
        assignedEmployees.remove(employee.getID().getValue());
        return dto.removeEmployeeFromShift(new ShiftDate(date, start, end), employee.getID().getValue());
    }


    public ResponseT<List<Employee>> getAllAssignedDrivers(){
        for(Map.Entry<String, List<Employee>> entry: assignedRolesEmp.entrySet()){
            if (entry.getKey().equals("Driver"))
                return new ResponseT<>(entry.getValue());
        }
        return new ResponseT<>(null, "for some reason there is no drivers in this shift");
    }

    @Override
    public String toString() {
        return "Shift{" +
                "date=" + date +
                ", start=" + start +
                ", end=" + end +
                ", closed=" + closed +
                ", \nconstrains=" + constrains +
                ", \nassignedEmployees=" + assignedEmployees +
                ", \nassignedRolesEmp=" + assignedRolesEmp +
                '}';
    }
}