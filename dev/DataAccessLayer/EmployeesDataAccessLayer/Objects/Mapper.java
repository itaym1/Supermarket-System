package DataAccessLayer.EmployeesDataAccessLayer.Objects;

import serviceObjects.ResponseT;
import DataAccessLayer.EmployeesDataAccessLayer.DAOs.EmployeeDAO;
import DataAccessLayer.EmployeesDataAccessLayer.DAOs.ShiftDAO;
import DataAccessLayer.EmployeesDataAccessLayer.DTOs.EmployeeDTO;
import DataAccessLayer.EmployeesDataAccessLayer.DTOs.ShiftDTO;

import java.util.Map;
import java.util.WeakHashMap;

public class Mapper {
    /**
     * This class holds the identity maps of Employees and Shifts.
     * Any access to the data base goes through here.
     */
    // employee1 = get("206");
    // emp2 = get("206");
    // emp1 == emp2
    private EmployeeDAO employeeDAO;
    private ShiftDAO shiftDAO;
    private Map<ShiftDate, ShiftDTO> shifts;
    private Map<String, EmployeeDTO> employees;

    private static class MapperHolder {
            private static Mapper instance = new Mapper();
        }

    private Mapper() {
        employeeDAO = new EmployeeDAO();
        shiftDAO = new ShiftDAO();
        shifts = new WeakHashMap<>();
        employees = new WeakHashMap<>();
    }

    public static Mapper getInstance(){
        return MapperHolder.instance;
    }

    public ResponseT<EmployeeDTO> getEmployee(String ID) {
        if (employees.containsKey(ID))
            return new ResponseT<>(employees.get(ID));
        ResponseT<EmployeeDTO> employee = employeeDAO.get(ID);
        if (!employee.isErrorOccured())
            employees.put(ID, employee.getValue());
        return employee;
    }

    public ResponseT<ShiftDTO> getShift(ShiftDate shiftDate){
        if (shifts.containsKey(shiftDate))
            return new ResponseT<>(shifts.get(shiftDate));
        ResponseT<ShiftDTO> shift = shiftDAO.get(shiftDate);
        if (!shift.isErrorOccured())
            shifts.put(shiftDate, shift.getValue());
        return shift;
    }




}
