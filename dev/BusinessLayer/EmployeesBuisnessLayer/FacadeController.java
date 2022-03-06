package BusinessLayer.EmployeesBuisnessLayer;

import serviceObjects.Response;
import serviceObjects.ResponseT;
import serviceObjects.ResponseUpdate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


public class FacadeController {
    private static FacadeController facadeController = null;
    private EmployeeController employeeController;
    private ShiftController shiftController;

    private FacadeController(){
        employeeController = EmployeeController.getInstance();
        shiftController = ShiftController.getInstance();
    }

    public static FacadeController getInstance(){
        if (facadeController == null)
            facadeController = new FacadeController();
        return facadeController;
    }



    /**
     *This function add new Employee to EmployeeController Hashmap of Employees only if userID is
     *HR manage or General Manger and return Response if successful, else return Response(error message)
     * @param userID The ID of the user itself
     * @param EmpID The ID of the new employee
     * @param name employee's name
     * @param bankAccount employee's bank account
     * @param salary employee's salary
     * @param sickDays employee's terms
     * @param studyFund employee's terms
     * @param daysOff employee's terms
     * @param roleName employee's role
     * @param _dateOfHire employee's date of hire
     * @return A Response in case an error has occurred.
     */
    public Response addEmployee(String userID, String EmpID, String name, String bankAccount, int salary, int sickDays,
                                int studyFund, int daysOff, String roleName, Integer licence, LocalDate _dateOfHire){

        return update(userID, () -> employeeController.AddEmployee(EmpID, name, bankAccount, salary, sickDays, studyFund,
                daysOff, roleName, licence, _dateOfHire));
    }

    private Response update(String userID, ResponseUpdate updateFunc){
        ResponseT<Employee> rE = checkAuthorization(userID);
        if (rE.isErrorOccured())
            return rE;
        return updateFunc.update();
    }

    /**
     *Updating Employee's name
     * @param userID The ID of the user
     * @param EmpID  The ID of the employee
     * @param newEmpName The new Name
     * @return A response
     */
    public Response updateEmpName(String userID, String EmpID, String newEmpName){
        return update(userID, () -> employeeController.setEmpName(EmpID, newEmpName));
    }

    /**
     *Updating Employee's bank account
     * @param userID The ID of the user
     * @param EmpID  The ID of the employee
     * @param newBankAccount the new bank account
     * @return A response
     */
    public Response updateEmpBankAccount(String userID, String EmpID, String newBankAccount) {
        return update(userID, () -> employeeController.updateEmpBankAccount(EmpID, newBankAccount));
    }

    /**
     * Updating Employee's salary
     * @param userID
     * @param EmpID
     * @param newSalary
     * @return A response
     */
    public Response updateEmpSalary(String userID, String EmpID, int newSalary) {
        return update(userID, () -> employeeController.updateEmpSalary(EmpID, newSalary));
    }

    /**
     * Updating Employee's sick days
     * @param userID
     * @param EmpID
     * @param UpdatedsickDays
     * @return A response
     */
    public Response updateEmpSickDays(String userID, String EmpID, int UpdatedsickDays){
        return update(userID, () -> employeeController.updateEmpSickDays(EmpID, UpdatedsickDays));
    }

    /**
     * Updating Employee's advanced study fund
     * @param userID
     * @param EmpID
     * @param newStudyFund
     * @return A response
     */
    public Response updateEmpStudyFund(String userID, String EmpID, int newStudyFund) {
        return update(userID, () -> employeeController.updateEmpStudyFund(EmpID, newStudyFund));
    }

    /**
     * Updating Employee's days off
     * @param userID
     * @param EmpID
     * @param newDaysOff
     * @return A response
     */
    public Response updateEmpDaysOff(String userID, String EmpID, int newDaysOff) {
        return update(userID, () -> employeeController.updateEmpDaysOff(EmpID, newDaysOff));
    }

    /**
     * Adding a role to the employee's roles list
     * @param userID
     * @param EmpID
     * @param role
     * @return A response
     */
    public Response addRoleToEmp(String userID, String EmpID, String role){
        return update(userID, () -> employeeController.addRoleToEmp(EmpID, role));
    }

    /**
     * Applying a constrain {@code pref} to the shift on {@code date} which start on {@code start}
     * and ends on {@code end}
     * @param userID
     * @param date
     * @param start
     * @param end
     * @param pref
     * @return A response
     */
    public Response putConstrain(String userID, LocalDate date, LocalTime start, LocalTime end, int pref/*0-cant 1-can 2-want*/){
        ResponseT<Employee> rE = employeeController.getEmployee(userID);
        if(rE.isErrorOccured())
            return rE;
        return shiftController.putConstrain(rE.getValue(), date, start, end, pref);
    }


    public Response generate2WeeklyShifts(String userID) {
        ResponseT<Employee> rE = checkAuthorization(userID);
        if (rE.isErrorOccured())
            return rE;
        return shiftController.add2WeeksSlots();
    } // only HR and general manager

    /**
     * Generates 1 weekly shift
     * @param userID
     * @return A response
     */
    public Response generate1weeklyShifts(String userID) {
        ResponseT<Employee> rE = checkAuthorization(userID);
        if (rE.isErrorOccured())
            return rE;
        return shiftController.add1WeeksSlot();
    }

    public ResponseT<List<Shift>> getFutureShifts(String userID){
        ResponseT<Employee> rE = checkAuthorization(userID);
        if (rE.isErrorOccured())
            return new ResponseT<>(null, rE.getErrorMessage());
        return shiftController.getFutureShifts();
    }

    /**
     * Assigning the employee with the given {@code EmpID} to the given shift with a given role
     * @param userID
     * @param EmpID
     * @param date
     * @param start
     * @param end
     * @param role
     * @return A response
     */
    public Response assignEmpToShift(String userID, String EmpID, LocalDate date, LocalTime start, LocalTime end, String role){
        ResponseT<Employee> rE = checkAuthorization(userID);
        if(rE.isErrorOccured())
            return rE;
        rE = employeeController.getEmployee(EmpID);
        if(rE.isErrorOccured())
            return rE;
        return shiftController.assignToShift(rE.getValue(), date, start, end, role);
    }

    /**
     * Removing the employee with the given {@code EmpID} from the given shift
     * @param userID
     * @param EmpID
     * @param date
     * @param start
     * @param end
     * @return A response
     */
    public Response removeEmpFromShift(String userID, String EmpID, LocalDate date, LocalTime start, LocalTime end){
        ResponseT<Employee> rE = checkAuthorization(userID);
        if(rE.isErrorOccured())
            return rE;
        rE = employeeController.getEmployee(EmpID);
        if(rE.isErrorOccured())
            return rE;
        return shiftController.removeEmpFromShift(rE.getValue(), date, start, end);
    }

    /**
     * Returns all the employees which assigned to the given shift
     * @param date
     * @param start
     * @param end
     * @return A response of list of employees
     */
    public ResponseT<List<Employee>> getAssignedEmpForShift(LocalDate date, LocalTime start, LocalTime end){
        return shiftController.getAssignedEmps(date, start, end);
    }


    /**
     * Changing the status of a given shift to close
     * @param date
     * @param start
     * @param end
     * @return A response
     */

    public ResponseT<List<Employee>> getAssignedDriversForShift(LocalDate date, LocalTime start, LocalTime end){
        return shiftController.getAllAssignedDrivers(date, start, end);
    }

    /**
     * Changing the status of a given shift to close
     * @param userID
     * @param date
     * @param start
     * @param end
     * @return A response
     */

    public Response closeShift(String userID, LocalDate date, LocalTime start, LocalTime end){
        ResponseT<Employee> rE = checkAuthorization(userID);
        if (rE.isErrorOccured())
            return rE;
        return shiftController.closeShift(date, start, end);
    } // only HR and general manager

    /**
     * Changing the status of a given shift to open
     * @param userID
     * @param date
     * @param start
     * @param end
     * @return A response
     */
    public Response openShift(String userID, LocalDate date, LocalTime start, LocalTime end){
        ResponseT<Employee> rE = checkAuthorization(userID);
        if (rE.isErrorOccured())
            return rE;
        return shiftController.openShift(date, start, end);
    }

    /**
     * Returns a list of the employees that assigned to a given shift with a given employee ID {@code userID}
     * @param userID
     * @param date
     * @param start
     * @param end
     * @return todo change to ResponseT<Employee> ?
     */
    public ResponseT<String> getWhoIWorkWith(String userID, LocalDate date, LocalTime start, LocalTime end) {
        ResponseT<Employee> rE = employeeController.getEmployee(userID);
        if(rE.isErrorOccured())
            return new ResponseT(null, rE.getErrorMessage());
        return shiftController.getWhoIWorkWith(rE.getValue(), date, start, end);
    } // return names and IDs only

    /**
     *
     * @param userID
     * @param date
     * @param start
     * @param end
     * @return todo change to an object?
     */
    public ResponseT<String> getMyPreferences(String userID, LocalDate date, LocalTime start, LocalTime end){
        ResponseT<Employee> rE = employeeController.getEmployee(userID);
        if(rE.isErrorOccured())
            return new ResponseT(null, rE.getErrorMessage());
        return shiftController.getMyPreferences(rE.getValue(), date, start, end);
    }

    /**
     *
     * @param date
     * @param type
     * @return
     */
    public ResponseT<Shift> getShift(LocalDate date, char type){
        return shiftController.findShift(date, type);
    }

    /**
     *
     * @return
     */
    public ResponseT<List<WeeklyShifts>> getFutureWeeklyShifts(){
        return shiftController.getFutureWeeklyShifts();
    }

    /**
     *
     * @param userID
     * @param date
     * @param start
     * @param end
     * @return
     */
    public ResponseT<String> getEmployeesConstrainsForShift(String userID, LocalDate date, LocalTime start, LocalTime end) {
        ResponseT<Employee> rE = checkAuthorization(userID);
        if (rE.isErrorOccured())
            return new ResponseT(null, rE.getErrorMessage());
        return shiftController.getEmployeesConstrainsForShift(date, start, end);
    }// only shift assigner **Yanay think that General Manager should be authorized as well

    /**
     *
     * @param userID
     * @return
     */
    public ResponseT<String> getMyData(String userID) {
        ResponseT<Employee> rE = employeeController.getEmployee(userID);
        if(rE.isErrorOccured())
            return new ResponseT(null, rE.getErrorMessage());
        return employeeController.getEmpData(rE.getValue());

        //What kind of Error response should it return?
    } // toString (req 12)

    /**
     * This method confirm the authorization of an employee to get access for sort of other methods.
     * @param userID The ID of the user who tries to get access.
     * @return Response<Employee> which gives back the Employee object if no error occurred, otherwise returns the
     * correct error
     */
    private ResponseT<Employee> checkAuthorization(String userID){
        ResponseT<Employee> r = employeeController.getEmployee(userID);
        if (r.isErrorOccured())
            return r;
        Employee employee = r.getValue();
        ResponseT<Boolean> rA = employee.checkAuthorizedHrOrGenral();
        if (rA.isErrorOccured())
            return new ResponseT<>(null, rA.getErrorMessage());
        if (!rA.getValue())
            return new ResponseT<>(null, "Not Authorized! Only HR Manager Or General Manager Authorized For This Action");
        return r;
    }

    /**
     * Returns true if the user is a manager and false otherwise.
     * @param userID
     * @return
     */
    public ResponseT<Boolean> checkAuthorizationBool(String userID){
        ResponseT<Employee> r = employeeController.getEmployee(userID);
        if (r.isErrorOccured())
            return new ResponseT<>(null, r.getErrorMessage());
        Employee employee = r.getValue();
        return employee.checkAuthorizedHrOrGenral();
    }

    public ResponseT<Boolean> isDeliveryManager(String userID){
        return employeeController.isDeliveryManager(userID);
    }

    public ResponseT<Boolean> isStorekeeperAssigned(LocalDate date, LocalTime departure){
        return shiftController.isStorekeeperAssigned(date, departure);
    }

    public ResponseT<List<String>> getAllAssignedRolesForEmployeeInShift(String EmpID, LocalDate date,
                                                                         LocalTime start, LocalTime end) {

        return shiftController.getAllAssignedRolesForEmployeeInShift(EmpID, date, start, end);
    }

    public Response addNotification(String id, String msg){
        return employeeController.addNotification(id, msg);
    }

    public ResponseT<List<String>> getNotifications(String id){
        return employeeController.getNotifications(id);
    }

    public ResponseT<Boolean> isEmployee(String id) {
        return employeeController.getEmployee(id).isErrorOccured() ?
                new ResponseT<>(false) : new ResponseT<>(true);
    }

    public ResponseT<Boolean> hasRole(String id, String role) {
        return employeeController.hasRole(id, role);
    }

    // init with 2 managers and 1 week forward
//    public void initData(){
//        employeeController.initData();
//        shiftController.add1WeeksSlot();
//        assignEmpToShift("312174295", "123456789", LocalDate.now().plusDays(1), LocalTime.of(14,0), LocalTime.of(22,0), "Cashier");
//        assignEmpToShift("312174295", "987654321", LocalDate.now().plusDays(1), LocalTime.of(14,0), LocalTime.of(22,0), "Driver");
//
//    }

//    public ResponseT<List<Shift>> getShiftsHistory(String userID){}

    //public ResponseT<shift> generateCustomShift(String userID, LocalDate from, LocalDate to) // only HR and general manager

}