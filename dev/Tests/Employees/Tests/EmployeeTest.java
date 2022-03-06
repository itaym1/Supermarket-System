package Tests.Employees.Tests;

import BusinessLayer.EmployeesBuisnessLayer.Employee;
import BusinessLayer.EmployeesBuisnessLayer.Role;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeTest {
    Employee employee;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        employee = new Employee("Nitzan", "205952971", LocalDate.now());
    }

    @org.junit.jupiter.api.Test
    void haveRoleCheck() {
        assertTrue(employee.getRoles().getValue().isEmpty());
        employee.AddRole(new Role("HR manager"));
        assertTrue(employee.haveRoleCheck("HR manager"));
    }

    @org.junit.jupiter.api.Test
    void checkAuthorizedHrOrGenral() {
        assertTrue(!employee.checkAuthorizedHrOrGenral().getValue());
        employee.AddRole(new Role("HR Manager"));
        assertTrue(employee.checkAuthorizedHrOrGenral().getValue());
    }
}