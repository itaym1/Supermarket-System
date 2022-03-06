package BusinessLayer.EmployeesBuisnessLayer;
import DataAccessLayer.EmployeesDataAccessLayer.DAOs.ShiftDAO;
import DataAccessLayer.EmployeesDataAccessLayer.DTOs.ShiftDTO;

import java.time.LocalDate;

import java.time.LocalTime;

public class EveningShift extends Shift {

    EveningShift(LocalDate _date, ShiftDAO dao) {
        super(_date, dao);
    }

    public EveningShift(ShiftDTO dto) {
        super(dto);
    }

    @Override
    public EveningShift clone() {
        return new EveningShift(dto);
    }

    @Override
    public void setStart() {
        start = LocalTime.of(14,0);
    }

    @Override
    public void setEnd() {
        end = LocalTime.of(22,0);
    }
}
