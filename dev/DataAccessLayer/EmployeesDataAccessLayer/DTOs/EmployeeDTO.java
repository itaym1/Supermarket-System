package DataAccessLayer.EmployeesDataAccessLayer.DTOs;

import serviceObjects.Response;
import DataAccessLayer.EmployeesDataAccessLayer.DAOs.EmployeeDAO;
import serviceObjects.UpdateFunction;

import java.time.LocalDate;
import java.util.List;

public class EmployeeDTO {
    private String name;
    private String ID;
    private String bankAccount;
    private int salary;
    private int sickDays;
    private int advancedStudyFund;
    private int daysOff;
    private LocalDate dateOfHire;
    private  List<RoleDTO> roles;
    private EmployeeDAO dao;


    public EmployeeDTO(String name, String id, String bankAccount, int salary, int sickDays, int advancedStudyFund,
                       int daysOff, LocalDate date, List<RoleDTO> roles, EmployeeDAO dao){
        this.name = name;
        this.ID = id;
        this.bankAccount = bankAccount;
        this.salary = salary;
        this.sickDays = sickDays;
        this.advancedStudyFund = advancedStudyFund;
        this.daysOff = daysOff;
        dateOfHire = date;
        this.roles = roles;
        this.dao = dao;
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public Response setName(String name_){
        return set(() -> this.name = name_);
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public Response setBankAccount(String bankAccount) {
        return set(() -> this.bankAccount = bankAccount);
    }

    public int getSalary() {
        return salary;
    }

    public Response setSalary(int salary){
        return set(() -> this.salary = salary);
    }

    public int getSickDays() {
        return sickDays;
    }

    public Response setSickDays(int sickDays) {
        return set(() -> this.sickDays = sickDays);
    }

    public int getAdvancedStudyFund() {
        return advancedStudyFund;
    }

    public Response setAdvancedStudyFund(int newVal){
        return set(() -> advancedStudyFund = newVal);
    }

    public int getDaysOff() {
        return daysOff;
    }

    public Response setDaysOff(int newVal){
        return set(() -> daysOff = newVal);
    }

    public LocalDate getDateOfHire() {
        return dateOfHire;
    }

    public List<RoleDTO> getRoles() {
        return roles;
    }

    public Response addRole(RoleDTO role){
        Response r = dao.addRole(ID, role);
        if(!r.isErrorOccured())
            roles.add(role);
        return r;
    }

    private Response set(UpdateFunction updateFunction){
        updateFunction.update();
        return dao.update(this);
    }

    public Response persist() {
        return dao.insert(this);
    }

    @Override
    public String toString() {
        return "EmployeeDTO{" +
                "name='" + name + '\'' +
                ", ID='" + ID + '\'' +
                ", bankAccount='" + bankAccount + '\'' +
                ", salary=" + salary +
                ", sickDays=" + sickDays +
                ", advancedStudyFund=" + advancedStudyFund +
                ", daysOff=" + daysOff +
                ", dateOfHire=" + dateOfHire +
                ", \nroles=" + roles +
                '}';
    }
}
