package PresentationLayer.EmployeesPresentationLayer;

import BusinessLayer.EmployeesBuisnessLayer.*;
import serviceObjects.Response;
import serviceObjects.ResponseT;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class CLIController {
    private static CLIController clientController = null;
    private EmployeeCLI employeeCli;
    private FacadeController facade;



    private String userID;



    private CLIController(){
        facade = FacadeController.getInstance();
    }

    public static CLIController getInstance(){
        if (clientController == null)
            clientController = new CLIController();
        return clientController;
    }

    public void setUserID(String userID) {
        employeeCli = EmployeeCLI.getInstance();
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }


//    public void initData() {
//        facade.initData();
//    }

    public ResponseT<Boolean> checkAuthorizedHrOrGenral(String id){
        return facade.checkAuthorizationBool(id);
    }




    public void Mmainmanue(int action) {
        if (action == 1){
            employeeCli.MempMenu();
        }
        if (action == 2){
            employeeCli.MshiftsMenu();
        }
    }

    public void MempMenu(int action) {
        if (action == 1){ addNewEmployee(); }
        if (action == 2){ employeeCli.MempUpdateMenu(); }

    }

    public void MupdateEmployeeMenu(int action) {
        if (action == 1){ updateEmployeeName(); }
        if (action == 2){ updateEmployeeBankAc(); }
        if (action == 3){ updateEmployeeSalary(); }
        if (action == 4) { updateEmployeeSickDays(); }
        if (action == 5) { updateEmployeeStudyFound(); }
        if (action == 6) { updateEmployeeDaysOff(); }
        if (action == 7) { updateEmployeeRole(); }
    }

    public void MshiftsMenu(int action) {
        // get shift
        if (action == 1)
            handleSingleShiftMenu();
//        if (action == 2)
//            employeeCli.weeksMenu();
//        if (action == 3)
//            generate1weeklyShift();
    }

    private void generate1weeklyShift() {
        Response r = facade.generate1weeklyShifts(userID);
        if(r.isErrorOccured())
            employeeCli.print(r.getErrorMessage());
        else
            employeeCli.print("The generation has done");
    }

    private char getShiftType(){
        char type = employeeCli.getChar("Enter 'M' for Morning shift or 'E' for Evening shift");
        while(type != 'E' && type != 'M'){
            type = employeeCli.getChar("Enter 'M' for Morning shift or 'E' for Evening shift");
        }
        return type;
    }

    private void handleSingleShiftMenu() {
        LocalDate date = employeeCli.getDate("Please enter date");
        char type = getShiftType();
        ResponseT<Shift> shift = facade.getShift(date, type);
        if (shift.isErrorOccured()) {
            employeeCli.print(shift.getErrorMessage());
            return;
        }
        employeeCli.MSingleShiftMenu(shift.getValue());
    }


    public void EmainMenu(int action) {
        //Show employee information
        if (action == 1){ employeeCli.print(showAllMyInformation()); }
        //Show employee preferences for a shift
        if (action == 2){ employeeCli.print(showMyPreferences()); }
        //Assign employee preferences for a specific shift
        if (action == 3){ assignPreferenceForShift(); }
        // Show colleagues whom the employee work with in a shift
        if (action == 4) { employeeCli.print(showColleaguesWorkWithMe()); }


    }

    public void addNewEmployee(){
        String EmpID = employeeCli.getString("Enter employee ID:");
        String name = employeeCli.getString("Enter employee name:");
        String bankAccount = employeeCli.getString("Enter employee's bank account:");

        int salary = employeeCli.getInt("Enter employee salary:");
        while (!isSalaryValid(salary))
            salary = employeeCli.getInt("Invalid value Enter employee salary:");

        int sickDays = employeeCli.getInt("Enter employee's sick days:");
        while(!isValidSickDays(sickDays))
            sickDays = employeeCli.getInt("Invalid value Enter employee's sick days:");

        int studyFund = employeeCli.getInt("Enter employee's study fund:");
        while(!isValidFund(studyFund))
            studyFund = employeeCli.getInt("Invalid value Enter employee's study fund:");

        int daysOff = employeeCli.getInt("Enter employee's days off:");
        while(!isValidDaysOff(daysOff))
            daysOff = employeeCli.getInt("Invalid value Enter employee's days off:");

        String roleName = employeeCli.getString("Enter employee's Role:");

        Integer licence = employeeCli.getInt("Enter employee's licence, if none, enter -1 :");

        LocalDate dateOfHire = employeeCli.getDate("Enter employee's Date of Hire");



        if (licence.equals(-1)){
            Response r = facade.addEmployee(clientController.userID, EmpID, name, bankAccount, salary, sickDays, studyFund,
                    daysOff, roleName, null, dateOfHire);
            if(r.isErrorOccured())
                employeeCli.print(r.getErrorMessage());
            else employeeCli.print("Employee added");
        }

        else{
            Response r = facade.addEmployee(clientController.userID, EmpID, name, bankAccount, salary, sickDays, studyFund,
                    daysOff, roleName, licence, dateOfHire);
            if(r.isErrorOccured())
                employeeCli.print(r.getErrorMessage());
            else employeeCli.print("Employee added");
        }



    }

    private boolean isValidDaysOff(int daysOff) {
        return daysOff > 0;
    }

    private boolean isValidFund(int studyFund) {
        return studyFund > 0;
    }

    private boolean isValidSickDays(int sickDays) {
        return sickDays > 0;
    }

    private boolean isSalaryValid(int salary) {
        return salary > 0;
    }


    public void updateEmployeeName(){
        String EmpID = employeeCli.getString("Enter employee's ID:");
        String newEmpID = employeeCli.getString("Enter new employee's name:");
        Response r = facade.updateEmpName(clientController.userID, EmpID, newEmpID);
        if(r.isErrorOccured())
            employeeCli.print(r.getErrorMessage());
        else employeeCli.print("Employee updated");
    }

    public void updateEmployeeBankAc(){
        String EmpID = employeeCli.getString("Enter employee's ID:");
        String newBankAc = employeeCli.getString("Enter new employee's bank account:");
        Response r = facade.updateEmpBankAccount(clientController.userID, EmpID, newBankAc);
        if(r.isErrorOccured())
            employeeCli.print(r.getErrorMessage());
        else employeeCli.print("Employee updated");
    }

    public void updateEmployeeSalary(){
        String EmpID = employeeCli.getString("Enter employee's ID:");
        int newSalary = employeeCli.getInt("Enter new employee's salary:");
        Response r =facade.updateEmpSalary(clientController.userID, EmpID, newSalary);
        if(r.isErrorOccured())
            employeeCli.print(r.getErrorMessage());
        else employeeCli.print("Employee updated");
    }

    public void updateEmployeeSickDays(){
        String EmpID = employeeCli.getString("Enter employee's ID:");
        int newSickDays = employeeCli.getInt("Enter new employee's sick Days:");
        Response r =facade.updateEmpSickDays(clientController.userID, EmpID, newSickDays);
        if(r.isErrorOccured())
            employeeCli.print(r.getErrorMessage());
        else employeeCli.print("Employee updated");
    }

    public void updateEmployeeStudyFound(){
        String EmpID = employeeCli.getString("Enter employee's ID:");
        int newStudyFound = employeeCli.getInt("Enter new employee's study Found:");
        Response r = facade.updateEmpStudyFund(clientController.userID, EmpID, newStudyFound);
        if(r.isErrorOccured())
            employeeCli.print(r.getErrorMessage());
        else employeeCli.print("Employee updated");
    }

    public void updateEmployeeDaysOff(){
        String EmpID = employeeCli.getString("Enter employee's ID:");
        int newDaysOff = employeeCli.getInt("Enter new employee's days off:");
        Response r = facade.updateEmpDaysOff(clientController.userID, EmpID, newDaysOff);
        if(r.isErrorOccured())
            employeeCli.print(r.getErrorMessage());
        else employeeCli.print("Employee updated");
    }


    public void updateEmployeeRole(){
        String EmpID = employeeCli.getString("Enter employee's ID:");
        String newRole = employeeCli.getString("Enter new employee's Role:");
        Response r = facade.addRoleToEmp(clientController.userID, EmpID, newRole);
        if(r.isErrorOccured())
            employeeCli.print(r.getErrorMessage());
        else employeeCli.print("Employee updated");
    }


    public String showAllMyInformation(){
        ResponseT<String> r = facade.getMyData(clientController.userID);
        if(r.isErrorOccured())
            return r.getErrorMessage();
        else
            return facade.getMyData(clientController.userID).getValue() + "\n";
    }

    public String showMyPreferences(){
        LocalDate shiftDate = employeeCli.getDate("Enter Shift Date ID");
//        String MorningEvning = cli.getChar("Enter M for morning shift or Enter E for evening shift"); // todo: check if works after changed to char
        char MorningEvning = getShiftType();
        if (MorningEvning == ('M')){
            return facade.getMyPreferences(userID, shiftDate,
                    LocalTime.of(6,0), LocalTime.of(14,0)).getValue() + "\n";
        }
        else
            return facade.getMyPreferences(userID, shiftDate,
                    LocalTime.of(14,0), LocalTime.of(22,0)).getValue() + "\n";

    }


    public void assignPreferenceForShift(){
        LocalDate shiftDate = employeeCli.getDate("Enter Shift Date ID");
//        String MorningEvning = cli.getString("Enter M for morning shift || Enter E for evning shift"); // todo: check if works after changed to char
        char MorningEvning = getShiftType();
        int preference = employeeCli.getInt("""
                Enter 0 if you CANT work on this shift
                Enter 1 if you CAN work on this shift
                Enter 2 if you WANT work on this shift
                """);
        if (MorningEvning == ('M')){
            facade.putConstrain(clientController.userID, shiftDate,
                    LocalTime.of(6, 0), LocalTime.of(14,0), preference);
        }
        else
            facade.putConstrain(clientController.userID, shiftDate,
                    LocalTime.of(14, 0), LocalTime.of(22,0), preference);
    }

    public String showColleaguesWorkWithMe(){
        LocalDate shiftDate = employeeCli.getDate("Enter Shift Date ID");
//        String MorningEvning = cli.getString("Enter M for morning shift or Enter E for evening shift"); // todo: check if works after changed to char
        char MorningEvning = getShiftType();
        if (MorningEvning == ('M')){
            return facade.getWhoIWorkWith(clientController.userID, shiftDate,
                    LocalTime.of(6, 0), LocalTime.of(14,0)).getValue() + "\n";
        }
        else
            return facade.getWhoIWorkWith(clientController.userID, shiftDate,
                    LocalTime.of(14, 0), LocalTime.of(22,0)).getValue() + "\n";
    }

    public void MSingleShiftOptions(int action, Shift shift) {
        if (action == 1)
            assignEmployee(shift);
        if(action == 2)
            getEmployeesPreferences(shift);
        if(action == 3)
            closeShift(shift);
        if(action == 4)
            openShift(shift);
        if(action == 5)
            showStatus(shift);
        if(action == 6)
            getAssignedEmployees(shift);
        if(action == 7)
            getAssignedDrivers(shift);
        if(action == 8)
            removeEmpFromShift(shift);
    }

    private void removeEmpFromShift(Shift shift) {
        String empID = employeeCli.getString("Enter the employee's ID");
        while(!isValidID(empID))
            empID = employeeCli.getString("Invalid ID, please try again");
        Response r = facade.removeEmpFromShift(userID, empID, shift.getDate(), shift.getStart(), shift.getEnd());
        if(r.isErrorOccured())
            employeeCli.print(r.getErrorMessage());
        else employeeCli.print("Remove accomplished");
    }

    private void getAssignedEmployees(Shift shift) {
        ResponseT<List<Employee>> employees = facade.getAssignedEmpForShift(shift.getDate(), shift.getStart(), shift.getEnd());
        if(employees.isErrorOccured()){
            employeeCli.print(employees.getErrorMessage());
            return;
        }
        employeeCli.displayEmployees(employees.getValue(), shift);
    }

    private void getAssignedDrivers(Shift shift) {
        ResponseT<List<Employee>> employees = facade.getAssignedDriversForShift(shift.getDate(), shift.getStart(), shift.getEnd());
        if(employees.isErrorOccured()){
            employeeCli.print(employees.getErrorMessage());
            return;
        }
        employeeCli.displayEmployees(employees.getValue(), shift);
    }

    private void showStatus(Shift shift) {
        if(shift.isClosed())
            employeeCli.print("Shift is close");
        else
            employeeCli.print("Shift is open");
    }

    private void openShift(Shift shift) {
        Response r = facade.openShift(userID, shift.getDate(), shift.getStart(), shift.getEnd());
        if(r.isErrorOccured())
            employeeCli.print(r.getErrorMessage());
        employeeCli.print("Shift Opened");
    }

    private void closeShift(Shift shift) {
        Response r = facade.closeShift(userID, shift.getDate(), shift.getStart(), shift.getEnd());
        if(r.isErrorOccured())
            employeeCli.print(r.getErrorMessage());
        employeeCli.print("Shift Closed");
    }

    private void getEmployeesPreferences(Shift shift) {
        ResponseT<String> r = facade.getEmployeesConstrainsForShift(userID, shift.getDate(), shift.getStart(), shift.getEnd());
        if(r.isErrorOccured())
            employeeCli.print(r.getErrorMessage());
        employeeCli.print(r.getValue());
    }

    private ResponseT<Boolean> isStorekeeperAssigned(Shift shift){
        return facade.isStorekeeperAssigned(shift.getDate(), shift.getStart());
    }

    private void assignEmployee(Shift shift) {
        String empID = employeeCli.getString("Enter the employee's ID");
        while(!isValidID(empID))
            empID = employeeCli.getString("Invalid ID, please try again");
        String role = employeeCli.getString("Enter employee's role you want to assign to");
        Response r = facade.assignEmpToShift(userID, empID, shift.getDate(), shift.getStart(), shift.getEnd(), role);
        if(r.isErrorOccured())
            employeeCli.print(r.getErrorMessage());
        else employeeCli.print("Assigning accomplished");
    }

    private boolean isValidID(String empID) {
        return true;
    }

    public void MWeeksMenu(int action) {
        ResponseT<List<WeeklyShifts>> weeklyShifts = facade.getFutureWeeklyShifts();
        if(weeklyShifts.isErrorOccured()) {
            employeeCli.print(weeklyShifts.getErrorMessage());
            return;
        }
        if(action == 1)
            employeeCli.displayWeekly(weeklyShifts.getValue().subList(0,1));
        if(action == 2)
            employeeCli.displayWeekly(weeklyShifts.getValue().subList(1,2));
        handleSingleShiftMenu();
    }

    public List<String> EmpNotifications(){
        ResponseT<List<String>> msgs = facade.getNotifications(userID);
        if(msgs.isErrorOccured()){
            employeeCli.print(msgs.getErrorMessage());
            return null;
        }
        return msgs.getValue();
    }

    public List<String> ManagerNotifications() {
        EmpNotifications();
        List<String> msgs = EmpNotifications();
        if(msgs == null) return null; // some error occurred
        ResponseT<List<String>> ManagerMsgs = facade.getNotifications("0");
        if(ManagerMsgs.isErrorOccured()){
            employeeCli.print(ManagerMsgs.getErrorMessage());
            return null;
        }
        msgs.addAll(ManagerMsgs.getValue());
        return msgs;
    }

//    public String showThisShiftStatus(){
//        return facade.getShiftStatuts(clientController.userID).getValue();
//    }

}