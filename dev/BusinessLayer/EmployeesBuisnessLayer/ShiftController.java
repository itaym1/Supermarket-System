package BusinessLayer.EmployeesBuisnessLayer;

import DataAccessLayer.EmployeesDataAccessLayer.DAOs.ShiftDAO;
import DataAccessLayer.EmployeesDataAccessLayer.DTOs.ShiftDTO;
import DataAccessLayer.EmployeesDataAccessLayer.Objects.Mapper;
import DataAccessLayer.EmployeesDataAccessLayer.Objects.ShiftDate;
import serviceObjects.Response;
import serviceObjects.ResponseT;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


//This class is Singleton

public class ShiftController {

    private static ShiftController shiftController = null;
    private ArrayList<WeeklyShifts> weeklyShifts;
    private Mapper mapper; 

    private ShiftController(){
        weeklyShifts = new ArrayList<>();  //creating with 4 weeks slots
        mapper = Mapper.getInstance();
//        ShiftController.add4WeeksSlots();
    }

    // static method to create instance of Singleton class
    public static ShiftController getInstance()
    {
        if (shiftController == null)
            shiftController = new ShiftController();
        return shiftController;
    }

    public List<WeeklyShifts> getWeeklyShifts(){return weeklyShifts;}

    public Response add2WeeksSlots(){
        if (weeklyShifts.isEmpty()){
            LocalDate tempDate = LocalDate.now();
            for (int i = 0 ; i < 2 ; i++) {
                weeklyShifts.add(new WeeklyShifts(tempDate.plusWeeks(i), tempDate.plusWeeks(i+1)));
            }
        }
        else{
            //Star to add slots from day after the last day we have in our weeklyShifts list
            LocalDate tempDate = weeklyShifts.get(weeklyShifts.size()-1).getToDate().plusDays(1);
            for (int i = 0 ; i < 2 ; i++) {
                weeklyShifts.add(new WeeklyShifts(tempDate.plusWeeks(i), tempDate.plusWeeks(i+1)));
                }
        }
        return new Response();
    }

    public Response add1WeeksSlot(){
        LocalDate tempDate;
        if (weeklyShifts.isEmpty()){
            tempDate = LocalDate.now();
        }
        else{
            //Star to add slots from day after the last day we have in our weeklyShifts list
            tempDate = weeklyShifts.get(weeklyShifts.size() - 1).getToDate();
        }
        weeklyShifts.add(new WeeklyShifts(tempDate, tempDate.plusWeeks(1)));
        return new Response();
    }

    public ResponseT<Shift> findShift(LocalDate date, LocalTime StartTime, LocalTime EndTime){
        for(WeeklyShifts ws : weeklyShifts){
            for(Shift s : ws.getShifts()){
                if (s.compare(date, StartTime, EndTime))
                    return new ResponseT<>(s);
            }
        }
        ResponseT<ShiftDTO> shiftDTO = mapper.getShift(new ShiftDate(date,StartTime,EndTime));
        if (shiftDTO.isErrorOccured()) {
            Shift shift = createShift(date, StartTime, EndTime);
            return new ResponseT<>(shift);
        }
        return new ResponseT<>(fromDTO(shiftDTO.getValue()));
    }

    private Shift createShift(LocalDate date, LocalTime startTime, LocalTime endTime) {
        if(startTime.equals(LocalTime.of(6,0)))
            return new MorningShift(date, new ShiftDAO());
        else return new EveningShift(date, new ShiftDAO());
    }

    private Shift fromDTO(ShiftDTO dto) {
        if(dto.getStart().equals(LocalTime.of(6,0)))
            return new MorningShift(dto);
        return new EveningShift(dto);
    }

    public ResponseT<Shift> findShift(LocalDate date, char type) {
        ResponseT<Shift> shift;
        if (type == 'M')
            shift = findShift(date, LocalTime.of(6,0), LocalTime.of(14,0));
        else if (type == 'E')
            shift = findShift(date, LocalTime.of(14,0), LocalTime.of(22,0));
        else
            return new ResponseT<>(null, "Incorrect type");
        if (shift.isErrorOccured())
            return shift;
        return new ResponseT<>(shift.getValue().clone());
    }

//    public ResponseT<List<Shift>> getShiftsByDate(LocalDate date){
//        List<Shift> shifts = new ArrayList<>();
//        for (WeeklyShifts ws: weeklyShifts)
//            for (Shift s: ws.getShifts())
//                if (date.compareTo(s.getDate()) == 0)
//                    shifts.add(s);
//        if (shifts.isEmpty())
//            return new ResponseT<>(null, "No shifts on this date");
//        return new ResponseT<>(shifts);
//    }

    public Response putConstrain(Employee employee, LocalDate date, LocalTime start, LocalTime end, int pref/*0-want 1-can 2-cant*/) {
        ResponseT<Shift> rS = findShift(date, start, end);
        if (!rS.isErrorOccured())
            return rS.getValue().AddConstrain(employee, pref);
        return rS;

    }

    public ResponseT<List<Shift>> getFutureShifts(){
        List<Shift> shifts = new ArrayList<>();
        LocalDate now = LocalDate.now();
        for(WeeklyShifts week: weeklyShifts){
            if (now.compareTo(week.getToDate()) <= 0){ // positive if now date is after week.getToDate()
                for (Shift s: week.getShifts()){
                    if (now.compareTo(s.getDate()) <= 0)
                        shifts.add(s.clone()); // added clone() method
                }
            }
        }
        if(shifts.size() < 28) { // 2 Weeks shifts
            add2WeeksSlots();
        }
        else {
            return new ResponseT<>(shifts);
        }
        return getFutureShifts();
    }

    public Response assignToShift(Employee employee, LocalDate date, LocalTime start, LocalTime end, String role){
        if(!employee.haveRoleCheck(role))
            return new Response("The role does not match with the employee's roles");
//        ResponseT<List<Shift>> rShifts = getShiftsByDate(date);
//        if(rShifts.isErrorOccured())
//            return rShifts;
//        for(Shift s: rShifts.getValue())
//            if (s.isAssigned(employee))
//                return new ResponseT<>(null, "The employee already assigned to a shift on this day");

        ResponseT<Shift> rS = findShift(date, start, end);
        if(rS.isErrorOccured())
            return rS;
        ResponseT<Shift> rS2;
        if(rS.getValue().getStart().equals(LocalTime.of(6,0))) // is morning shift
            rS2 = findShift(date, 'E');
        else rS2 = findShift(date, 'M');
        if(rS2.isErrorOccured())
            return rS2;
        if(rS2.getValue().isAssigned(employee) || rS.getValue().isAssigned(employee))
            return new ResponseT<>(null, "The employee already assigned to a shift on this day");
        return rS.getValue().AssignEmployee(employee, role);
    }

    public Response removeEmpFromShift(Employee employee, LocalDate date, LocalTime start, LocalTime end) {
        ResponseT<Shift> rS = findShift(date, start, end);
        if(rS.isErrorOccured())
            return rS;
        return rS.getValue().removeEmp(employee);
    }


    public Response closeShift(LocalDate date, LocalTime start, LocalTime end) {
        ResponseT<Shift> rS = findShift(date, start, end);
        if(rS.isErrorOccured())
            return rS;
        return rS.getValue().close();
    }

    public Response openShift(LocalDate date, LocalTime start, LocalTime end) {
        ResponseT<Shift> rS = findShift(date, start, end);
        if(rS.isErrorOccured())
            return rS;
        return rS.getValue().open();
    }

    public ResponseT<String> getEmployeesConstrainsForShift(LocalDate date, LocalTime start, LocalTime end) {
        ResponseT<Shift> rS = findShift(date, start, end);
        if(rS.isErrorOccured())
            return new ResponseT<>(null, rS.getErrorMessage());
        return rS.getValue().getShiftConstrainsString();
    }

    public ResponseT<String> getWhoIWorkWith(Employee employee, LocalDate date, LocalTime start, LocalTime end) {
        ResponseT<Shift> rS = findShift(date, start, end);
        if (rS.isErrorOccured())
            return new ResponseT<>(null, rS.getErrorMessage());
        return rS.getValue().getWhoIWorkWith(employee);
    }

    public ResponseT<String> getMyPreferences(Employee employee, LocalDate date, LocalTime start, LocalTime end) {
        ResponseT<Shift> rS = findShift(date, start, end);
        if (rS.isErrorOccured())
            return new ResponseT<>(null, rS.getErrorMessage());
        return rS.getValue().getEmpPreferences(employee);
    }

    public ResponseT<List<Employee>> getAssignedEmps(LocalDate date, LocalTime start, LocalTime end) {
        ResponseT<Shift> rS = findShift(date, start, end);
        if (rS.isErrorOccured())
            return new ResponseT<>(null, rS.getErrorMessage());
        return rS.getValue().getAllAssignedEmployees();
    }

    public ResponseT<List<Employee>> getAllAssignedDrivers(LocalDate date, LocalTime departure) {
        char type;
        if(departure.isAfter(LocalTime.of(5,59)) && departure.isBefore(LocalTime.of(14, 0)))
            type = 'M';
        else type = 'E';
        ResponseT<Shift> rS = findShift(date, type);
        if (rS.isErrorOccured())
            return new ResponseT<>(null, rS.getErrorMessage());
        return rS.getValue().getAllAssignedDrivers();
    }

    public ResponseT<Boolean> isStorekeeperAssigned(LocalDate date, LocalTime departure) {
        char type;
        if(departure.isAfter(LocalTime.of(5,59)) && departure.isBefore(LocalTime.of(14, 0)))
            type = 'M';
        else type = 'E';
        ResponseT<Shift> rS = findShift(date, type);
        if (rS.isErrorOccured())
            return new ResponseT<>(null, rS.getErrorMessage());
        for(Employee emp : rS.getValue().getAllAssignedEmployees().getValue()){
            if (emp.haveRoleCheck("Storekeeper"))
                return new ResponseT<>(true);
        }
        return new ResponseT<>(false);
    }

    public ResponseT<List<Employee>> getAllAssignedDrivers(LocalDate date, LocalTime start, LocalTime end){
        ResponseT<Shift> rS = findShift(date, start, end);
        if (rS.isErrorOccured())
            return new ResponseT<>(null, rS.getErrorMessage());
        return rS.getValue().getAllAssignedDrivers();
    }


    public ResponseT<List<WeeklyShifts>> getFutureWeeklyShifts() {
        if (weeklyShifts.size() < 2)
            add2WeeksSlots();
        List<WeeklyShifts> weeklyShift = new ArrayList<>();
        LocalDate now = LocalDate.now();
        for(WeeklyShifts week: weeklyShifts){
            if (now.compareTo(week.getToDate()) <= 0)
                weeklyShift.add(new WeeklyShifts(week));
        }
        return new ResponseT<>(weeklyShift);
    }

    public ResponseT<List<String>> getAllAssignedRolesForEmployeeInShift(String EmpID, LocalDate date,
                                                                         LocalTime start, LocalTime end){
        ResponseT<Shift> shift = findShift(date, start, end);
        if(shift.isErrorOccured())
            return new ResponseT<>(null, shift.getErrorMessage());
        return shift.getValue().getAllAssignedRolesForEmployeeInShift(EmpID);

    }


}
