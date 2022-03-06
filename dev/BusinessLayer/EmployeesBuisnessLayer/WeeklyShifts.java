package BusinessLayer.EmployeesBuisnessLayer;

import DataAccessLayer.EmployeesDataAccessLayer.DAOs.ShiftDAO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class WeeklyShifts {

    private LocalDate fromDate;
    private LocalDate toDate;
    private ArrayList<Shift> shifts;
    private ShiftDAO dao;

    public WeeklyShifts(LocalDate _fromDate, LocalDate _toDate){
        dao = new ShiftDAO();
        fromDate = _fromDate;
        toDate = _toDate;
        shifts = new ArrayList<>();
        for (int i=0; i < 7 ; i++){
            shifts.add(new MorningShift(fromDate.plusDays(i), dao));
            shifts.add(new EveningShift(fromDate.plusDays(i), dao));
        }
    }

    public WeeklyShifts(WeeklyShifts other){
        fromDate = other.fromDate;
        toDate = other.toDate;
        shifts = new ArrayList<>();
        for(Shift s: other.shifts)
            shifts.add(s.clone());
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public List<Shift> getShifts() {
        return shifts;
    }



}
