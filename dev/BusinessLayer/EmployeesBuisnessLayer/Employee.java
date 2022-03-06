package BusinessLayer.EmployeesBuisnessLayer;

import DataAccessLayer.EmployeesDataAccessLayer.DAOs.EmployeeDAO;
import DataAccessLayer.EmployeesDataAccessLayer.DTOs.EmployeeDTO;
import DataAccessLayer.EmployeesDataAccessLayer.DTOs.RoleDTO;
import serviceObjects.Response;
import serviceObjects.ResponseT;
import serviceObjects.ResponseUpdate;
import serviceObjects.UpdateFunction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Employee {
    private String name;
    private String ID;
    private String bankAccount;
    private int salary;
    private List<Role> roles;
    private TermsOfEmployee terms;
    private LocalDate dateOfHire;
    private EmployeeDTO dto;
    private boolean isPersisted;

    public Employee(String _name, String _ID, LocalDate _dateOfHire) {
        if (!isNameValid(_name))
            throw new IllegalArgumentException("Invalid name");
         if (!isIdValid(_ID))
            throw new IllegalArgumentException("Invalid ID, required 9 digits");
        if (!isDateValid(_dateOfHire))
            throw new IllegalArgumentException("Invalid date");
        name = _name;
        ID = _ID;
        dateOfHire = _dateOfHire;
        roles = new ArrayList<>();
    }

    // copy ctr
    public Employee(Employee other){
        name = other.name;
        ID = other.ID;
        bankAccount = other.bankAccount;
        salary = other.salary;
        roles = other.roles.stream().map(Role::clone).collect(Collectors.toList());
        terms = new TermsOfEmployee(other.terms);
        dateOfHire = other.dateOfHire;
    }

    //dto to employee
    public Employee(EmployeeDTO other){
        name = other.getName();
        ID = other.getID();
        bankAccount = other.getBankAccount();
        salary = other.getSalary();
        roles = new ArrayList<>();
        for (RoleDTO role : other.getRoles())
            roles.add(new Role(role));
        terms = new TermsOfEmployee(other.getSickDays(), other.getAdvancedStudyFund(), other.getDaysOff());
        dateOfHire = other.getDateOfHire();
        dto = other;
        isPersisted = true;
    }


    public void setDTO(EmployeeDAO dao) {
        // mapping each role into its equivalent RoleDTO
        List<RoleDTO> rolesDTO = roles.stream().map(Role::toDTO).collect(Collectors.toList());
        dto = new EmployeeDTO(name, ID, bankAccount, salary, getTerms().getSickDays(),
                getTerms().getAdvancedStudyFund(), getTerms().getDaysOff(), dateOfHire, rolesDTO, dao);
    }

    public Response persist(){
        Response r = dto.persist();
        if(!r.isErrorOccured())
            isPersisted = true;
        return r;
    }

    private boolean isNameValid(String name){
        return name != null && !name.equals("") && !name.equals(" ");
    }

    private boolean isIdValid(String id){
        return id.length() == 9;
    }

    private boolean isDateValid(LocalDate date){
        return date.isBefore(LocalDate.now()) || date.isEqual(LocalDate.now());
    }

    public ResponseT<String> getName() {
        return new ResponseT<String>(name);
    }


    public Response setName(String name) {
        if (!isNameValid(name))
            return new ResponseT("Invalid name");
        this.name = name;
        if (isPersisted)
            this.dto.setName(name);
        return new Response();
    }

    public ResponseT<String> getID() {
        return new ResponseT<String>(ID);
    }

    public ResponseT<String> getBankAccount() {
        return new ResponseT<String>(bankAccount);
    }

    public ResponseT<Integer> getSalary() {
        return new ResponseT<Integer>(salary);
    }


    public ResponseT<List<Role>> getRoles() {
        return new ResponseT<>(roles);
    }

    public TermsOfEmployee getTerms() {
        return terms;
    }

    public Response setSalary(int salary) {
        return set(() -> dto.setSalary(salary), () -> this.salary = salary);
//        this.salary = salary;
//        if (isPersisted)
//            this.dto.setSalary(salary);
//        return new Response();
    }

    public Response setBankAccount(String bankAccount) {
        return set(() -> dto.setBankAccount(bankAccount), () -> this.bankAccount = bankAccount);
//        this.bankAccount = bankAccount;
//        if (isPersisted)
//            this.dto.setBankAccount(bankAccount);
//        return new Response();
    }

    public Response AddRole(Role role){
        return set(() -> dto.addRole(role.toDTO()), () -> roles.add(role));
//        roles.add(role);
//        if (isPersisted)
//            this.dto.addRole(role.toDTO());
//        return new Response();
    }

    public Response setTerms(TermsOfEmployee terms) {
        this.terms = terms;
        if(!isPersisted)
            return new Response();
        Response r1 = this.dto.setSickDays(terms.getSickDays());
        Response r2 = this.dto.setAdvancedStudyFund(terms.getAdvancedStudyFund());
        Response r3 = this.dto.setDaysOff(terms.daysOff);
        if(r1.isErrorOccured() || r2.isErrorOccured() || r3.isErrorOccured())
            return new Response("Some error occurred, we can't tell exactly what");
        return new Response();
    }

    public Response setSickDays(int updatedSickDays) {
        return set(() -> dto.setSickDays(updatedSickDays), () -> terms.setSickDays(updatedSickDays));
    }

    public Response setAdvancedStudyFund(int newStudyFund) {
        return set(() -> dto.setAdvancedStudyFund(newStudyFund), () -> terms.setAdvancedStudyFund(newStudyFund));
    }

    public Response setDaysOff(int newDaysOff) {
        return set(() -> dto.setDaysOff(newDaysOff), () -> terms.setDaysOff(newDaysOff));
    }

    private Response set(ResponseUpdate updateDTO, UpdateFunction updateEmp){
        updateEmp.update();
        if(isPersisted)
            return updateDTO.update();
        return new Response();
    }

    public boolean haveRoleCheck(String roleToCheck) {
        for (Role role : roles) {
            if (role.compare(roleToCheck))
                return true;
        }
        return false;
    }

    public String getRolesTostring(){
        StringBuilder res = new StringBuilder("[");
        for(int i = 0 ; i < roles.size() ; i++){
            if(i < roles.size() - 1){
                res.append(roles.get(i).getName()).append(", ");
            }
            else{
                res.append(roles.get(i).getName()).append("]");
            }
        }
        return res.toString();
    }


    //Check if this employee is HR/generarManager authorize
    public ResponseT<Boolean> checkAuthorizedHrOrGenral(){
        if(this.haveRoleCheck("HR Manager") || this.haveRoleCheck("General Manager"))
            return new ResponseT<Boolean>(true);
        return new ResponseT<Boolean>(false);
    }

    public ResponseT<String> getEmpDataTostring(){
        return new ResponseT<String>("Name: %s \nID: %s \nBank Account: %s \nSalary: %s \nRoles: %s \nDate Of Hire: %s".formatted(
                name, ID, bankAccount, salary, getRolesTostring(), dateOfHire)
        );
        //what kind of error Response should it return??
    }


    public EmployeeDTO getDTO() {
        return dto;
    }

    public ResponseT<Boolean> isDeliveryManager() {
        boolean success = roles.stream().anyMatch((role) -> role.compare("Delivery Manager"));
        return new ResponseT<>(success);
    }

    public ResponseT<Integer> getLicenceType() {
        for (Role r: getRoles().getValue()){
            if (r.compare("Driver"))
                return new ResponseT<>(r.getLicence());
        }
        return new ResponseT<>(null); //That should never happened.
    }



}
