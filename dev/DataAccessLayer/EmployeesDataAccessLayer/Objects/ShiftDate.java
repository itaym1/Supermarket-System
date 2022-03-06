package DataAccessLayer.EmployeesDataAccessLayer.Objects;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

/**
 * This class is only used as a key in the identity map
 */

public class ShiftDate {
    private LocalDate date;
    private LocalTime start;
    private LocalTime end;

    public ShiftDate(LocalDate d, LocalTime s, LocalTime e){
        date = d;
        start = s;
        end = e;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShiftDate shiftDate = (ShiftDate) o;
        return date.equals(shiftDate.date) && start.equals(shiftDate.start) && end.equals(shiftDate.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, start, end);
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStart() {
        return start;
    }

    public LocalTime getEnd() {
        return end;
    }
}
