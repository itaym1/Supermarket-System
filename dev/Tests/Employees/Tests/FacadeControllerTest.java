package Tests.Employees.Tests;

import BusinessLayer.EmployeesBuisnessLayer.*;
import DataAccessLayer.EmployeesDataAccessLayer.DAOs.EmployeeDAO;
import DataAccessLayer.EmployeesDataAccessLayer.DAOs.ShiftDAO;
import DataAccessLayer.EmployeesDataAccessLayer.DTOs.EmployeeDTO;
import DataAccessLayer.EmployeesDataAccessLayer.DTOs.RoleDTO;
import DataAccessLayer.EmployeesDataAccessLayer.DTOs.ShiftDTO;
import DataAccessLayer.EmployeesDataAccessLayer.Objects.Mapper;
import DataAccessLayer.EmployeesDataAccessLayer.Objects.ShiftDate;
import org.junit.jupiter.api.Test;
import serviceObjects.Response;
import serviceObjects.ResponseT;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FacadeControllerTest {
    FacadeController facade;
    EmployeeController employeeController;
    ShiftController shiftController;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        facade = FacadeController.getInstance();
        //facade.initData();
        employeeController = EmployeeController.getInstance();
        shiftController = ShiftController.getInstance();
    }

//    @Test
//    void addEmployee() {
//        facade.addEmployee("313150013", "111111111", "Eyal", "12345", 1000,
//                30, 30, 30, "cashier", LocalDate.now());
//        employeeController = EmployeeController.getInstance();
//        ResponseT<Employee> rE = employeeController.getEmployee("111111111");
//        assertTrue(rE.isErrorOccured());
//        facade.addEmployee("205952971", "111111111", "Eyal", "12345", 1000,
//                30, 30, 30, "cashier", LocalDate.now());
//        rE = employeeController.getEmployee("111111111");
//        assertFalse(rE.isErrorOccured());
//
//
//        facade.addEmployee("205952971", "987654321", "Eyal", "12345", 1000,
//                30, 30, 30, "cashier", LocalDate.now());
//        rE = employeeController.getEmployee("987654321");
//        assertFalse(rE.isErrorOccured());
//    }

    @Test
    void updateEmpName() {
        facade.updateEmpName("205952971", "312174295", "new");
        ResponseT<Employee> rE = employeeController.getEmployee("312174295");
        assertFalse(rE.isErrorOccured());
        assertFalse(rE.getValue().getName().isErrorOccured());
        assertTrue(rE.getValue().getName().getValue().equals("new"));
    }

    @Test
    void updateEmpBankAccount() {
        facade.updateEmpBankAccount("205952971", "312174295", "new");
        ResponseT<Employee> rE = employeeController.getEmployee("312174295");
        assertFalse(rE.isErrorOccured());
        assertFalse(rE.getValue().getBankAccount().isErrorOccured());
        assertTrue(rE.getValue().getBankAccount().getValue().equals("new"));
    }

    @Test
    void updateEmpSalary() {
        facade.updateEmpSalary("205952971", "312174295", 5000);
        ResponseT<Employee> rE = employeeController.getEmployee("312174295");
        assertFalse(rE.isErrorOccured());
        assertFalse(rE.getValue().getSalary().isErrorOccured());
        assertTrue(rE.getValue().getSalary().getValue() == 5000);
    }

    @Test
    void updateEmpSickDays() {
        facade.updateEmpSickDays("205952971", "312174295", 5000);
        ResponseT<Employee> rE = employeeController.getEmployee("312174295");
        assertFalse(rE.isErrorOccured());
        assertTrue(rE.getValue().getTerms().getSickDays() == 5000);
    }

    @Test
    void updateEmpStudyFund() {
        facade.updateEmpStudyFund("205952971", "312174295", 5000);
        ResponseT<Employee> rE = employeeController.getEmployee("312174295");
        assertFalse(rE.isErrorOccured());
        assertTrue(rE.getValue().getTerms().getAdvancedStudyFund() == 5000);
    }

    @Test
    void updateEmpDaysOff() {
        facade.updateEmpDaysOff("205952971", "312174295", 5000);
        ResponseT<Employee> rE = employeeController.getEmployee("312174295");
        assertFalse(rE.isErrorOccured());
        assertTrue(rE.getValue().getTerms().getDaysOff() == 5000);
    }



    /**
     * We check here getMyPreferences and getEmployeesConstrainsForShift in addition.
     */
    @Test
    void putConstrain() {
        LocalDate date = LocalDate.now();
        LocalTime start = LocalTime.of(6,0);
        LocalTime end = LocalTime.of(14,0);
        facade.putConstrain("111111111", LocalDate.now(), start, end, 0);
        facade.getEmployeesConstrainsForShift("111111111", date, start, end);
        ResponseT<String> rS = facade.getMyPreferences("111111111", date, start, end);
        assertFalse(rS.isErrorOccured());
        assertTrue(rS.getValue().equals("The preference to this shift is: Can't work on this shift"));
        ResponseT<String> res = facade.getEmployeesConstrainsForShift("205952971", date, start, end);
        assertFalse(res.isErrorOccured());
        assertTrue(res.getValue().equals("ID: 111111111 | Name: Eyal | Preference: 0\n"));

    }

//    @Test
//    void getFutureShifts() {
//
//    }

    /**
     * We check here getWhoIWorkWith, getAssignedEmpForShift in addition.
     */
    @Test
    void assignEmpToShift() {
        LocalDate date = LocalDate.now();
        LocalTime start = LocalTime.of(6,0);
        LocalTime end = LocalTime.of(14,0);
        Response r = facade.assignEmpToShift("205952971", "312174295", date, start, end, "General Manager");
        Response r2 = facade.assignEmpToShift("205952971", "123456789", date, start, end, "Cashier");
        assertFalse(r.isErrorOccured() || r2.isErrorOccured());
        ResponseT<List<Employee>> res = shiftController.getAssignedEmps(date, start, end);
        assertFalse(res.isErrorOccured());
        assertTrue(res.getValue().get(0).getID().getValue().equals("312174295"));
        ResponseT<String> r3 = facade.getWhoIWorkWith("312174295", date, start, end);
        assertFalse(r3.isErrorOccured());

        ResponseT<List<Employee>> employees = facade.getAssignedEmpForShift(date, start, end);
        assertFalse(employees.isErrorOccured());
        assertTrue(employees.getValue().size() == 2);
        System.out.println(employees.getValue().get(0).getRoles().getValue().get(0));

        // assigning to a shift at the same day with another shift
        start = end;
        end = LocalTime.of(22, 0);
        r = facade.assignEmpToShift("205952971", "123456789",date, start, end, "Cashier");
        r2 = facade.assignEmpToShift("205952971", "123456789",date, start, end, "Cashier");
        assertTrue(r2.isErrorOccured());
    }


    @Test
    void DAL_Emp_insert() {
        // init EmployeeDTO:
        List<RoleDTO> roles = new ArrayList<>();
        roles.add(new RoleDTO("HR Manager"));
        roles.add(new RoleDTO("Driver", 123456));
        EmployeeDAO dao = new EmployeeDAO();
        EmployeeDTO e = new EmployeeDTO("Nitzan", "205952971", "12345", 1000, 30,
                500, 30, LocalDate.now(), roles, dao);
        // insert the DTO to the database:
        Response r = dao.insert(e);
        failureHelper(r);
    }
    // we test here mapper.getEmployee() in addition
    @Test
    void DAL_Emp_update() {

        ResponseT<EmployeeDTO> emp = Mapper.getInstance().getEmployee("111111111");
        assertTrue(emp.isErrorOccured());

        emp = Mapper.getInstance().getEmployee("205952971");
        failureHelper(emp);
        ResponseT<EmployeeDTO> emp2 = Mapper.getInstance().getEmployee("205952971");
        assertEquals(emp2.getValue(), emp.getValue());

        Response r = emp.getValue().setSalary(2000);
        failureHelper(r);

        r = emp.getValue().addRole(new RoleDTO("testOhterRole", 4321));
        failureHelper(r);
    }
    @Test
    void DAL_Emp_get(){
        ResponseT<EmployeeDTO> emp = Mapper.getInstance().getEmployee("205952971");
        failureHelper(emp);
        Response r = emp.getValue().setBankAccount("123456");
        failureHelper(r);
    }

    public ShiftDate getShiftDate(){
        LocalDate date = LocalDate.now();
        LocalTime start = LocalTime.of(6,0);
        LocalTime end = LocalTime.of(14,0);
        return new ShiftDate(date, start, end);
    }

    @Test
    void DAL_Shift_insert(){
        LocalDate date = LocalDate.now();
        LocalTime start = LocalTime.of(6,0);
        LocalTime end = LocalTime.of(14,0);
        ShiftDAO s = new ShiftDAO();
        ShiftDTO shift = new ShiftDTO(date, start, end, true, new HashMap<>(),new HashMap<>(),new HashMap<>(),s);
        Response r = shift.persist();
        failureHelper(r);
        assertEquals(shift, Mapper.getInstance().getShift(new ShiftDate(date, start, end)).getValue());
    }

    @Test
    void DAL_Shift_addConstrain(){
        ShiftDate shiftDate = getShiftDate();
        ResponseT<ShiftDTO> shift = Mapper.getInstance().getShift(shiftDate);
        failureHelper(shift);
        EmployeeDTO emp = Mapper.getInstance().getEmployee("205952971").getValue();
        Response r = shift.getValue().addConstrain(emp, 2);
        failureHelper(r);
    }

    @Test
    void DAL_Shift_assignEmployee(){
        ShiftDate shiftDate = getShiftDate();
        ResponseT<ShiftDTO> shift = Mapper.getInstance().getShift(shiftDate);
        failureHelper(shift);
        EmployeeDTO emp = Mapper.getInstance().getEmployee("205952971").getValue();
        Response r = shift.getValue().AssignEmployee(emp, "SomeRole");
        failureHelper(r);
    }

    void failureHelper(Response r){
        if (r.isErrorOccured()){
            System.out.println(r.getErrorMessage());
            fail();
        }
    }

}