package BusinessLayer.EmployeesBuisnessLayer;

import serviceObjects.Response;

public class TermsOfEmployee {
    int sickDays;
    int advancedStudyFund;
    int daysOff;

    TermsOfEmployee(int _sickDays, int _advancedStudyFund, int _daysOff){
        sickDays = _sickDays;
        advancedStudyFund = _advancedStudyFund;
        daysOff = _daysOff;
    }

    TermsOfEmployee(TermsOfEmployee other){
        sickDays = other.sickDays;
        advancedStudyFund = other.advancedStudyFund;
        daysOff = other.daysOff;
    }

    public int getAdvancedStudyFund() {
        return advancedStudyFund;
    }

    public int getSickDays() {
        return sickDays;
    }

    public int getDaysOff() {
        return daysOff;
    }

    public Response setAdvancedStudyFund(int advancedStudyFund) {
        this.advancedStudyFund = advancedStudyFund;
        return new Response();
    }

    public Response setDaysOff(int daysOff) {
        this.daysOff = daysOff;
        return new Response();
    }

    public Response setSickDays(int sickDays) {
        this.sickDays = sickDays;
        return new Response();
    }
}
