package PresentationLayer.EmployeesPresentationLayer;

import BusinessLayer.EmployeesBuisnessLayer.*;
import serviceObjects.ResponseT;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class EmployeeCLI {
    private static EmployeeCLI employeeCli = null;
    private CLIController cliController;
    private String userID;
    Scanner scanner;

    private EmployeeCLI(){
        cliController = CLIController.getInstance();
        scanner = new Scanner(System.in);
    }

    public static EmployeeCLI getInstance(){
        if (employeeCli == null)
            employeeCli = new EmployeeCLI();
        return employeeCli;
    }



    private boolean isNameValid(String name){
        return name != null && !name.equals("") && !name.equals(" ");
    }

//    private boolean isIdValid(String id){
//        return id.length() == 9;
//    }

    //starts the login menu of the program
    public void start(String ID) {

//        String ID;
        int action;
        do {
//            DisloginMenu();
//            ID = scanner.next();
            if(ID.equals("0"))
                break;
            cliController.setUserID(ID);
            ResponseT<Boolean> r = cliController.checkAuthorizedHrOrGenral(ID);
            while(r.isErrorOccured()) {
                System.out.println("ID not found, please try again");
                ID = scanner.next();
                r = cliController.checkAuthorizedHrOrGenral(ID);
            }
            if (r.getValue()) {
                do {
                    //The User is Hr or General manager
                    DisMmainMenu();
                    action = scanner.nextInt();
                    if (action == 0)
                        System.exit(0);
                    scanner.nextLine();
                    cliController.Mmainmanue(action);
                } while (true);
            } else {
                do { //TODO
                    //The User is Regular Employee (not Hr or General manager)
                    DisEmainMenu();
                    action = scanner.nextInt();
                    if (action == 0)
                        break;
                    scanner.nextLine();
                    cliController.EmainMenu(action);
                } while (true);

            }
        }while (true);
    }

    public void DisloginMenu(){
        System.out.println("""
                * Welcome! **
               
                Please Enter ID:
                
                
                To exit press 0 
                """);
    }


    public void DisMmainMenu(){
        printNotifications(cliController.ManagerNotifications());
        System.out.println("""
                
                1) Employees Menu
                2) Shifts Menu
                To exit press 0
                
                """);
    }

    private void printNotifications(List<String> notifications) {
        System.out.println(String.format("You have %s new messages", notifications.size()));
        notifications.forEach(System.out::println);
    }

    //------------------------------MANAGER ONLY----------------------------------

    public void MempMenu() {
        int action;
        do {
            DisMempMenu();
            action = scanner.nextInt();
            if (action == 0)
                break;
            scanner.nextLine();
            cliController.MempMenu(action);
        } while (true);

    }

    public void DisMempMenu(){
        System.out.println("""
                1) Add new employee to the system
                2) update or edit existing employee
                To exit press 0
                
                """);
    }

    public void MempUpdateMenu() {
        int action;
        do {
            DisMempUpdateMenu();
            action = scanner.nextInt();
            if (action == 0)
                break;
            scanner.nextLine();
            cliController.MupdateEmployeeMenu(action);
        } while (true);

    }

    public void DisMempUpdateMenu(){
        System.out.println("""
                1) Update employee name
                2) Update employee bank account
                3) Update employee salary
                4) Update employee sick days
                5) Update employee study found
                6) Update employee days off
                7) Update employee role
                To return to the previous menu press 0
                
                """);
    }

    public void MshiftsMenu() {
        int action;
        do {
            DisMshiftsMenu();
            action = scanner.nextInt();
            if (action == 0)
                break;
            scanner.nextLine();
            cliController.MshiftsMenu(action);
        } while (true);

    }

    public void DisMshiftsMenu(){
        System.out.println("""
                1) Choose shift by date
                To return to the previous menu press 0
                """);
    }

    public void MSingleShiftMenu(Shift shift) {
        int action;
        do {
            MSingleShiftDisplay(shift);
            action = scanner.nextInt();
            if (action == 0)
                break;
            scanner.nextLine();
            cliController.MSingleShiftOptions(action, shift);
        }while (true);
    }

    private void MSingleShiftDisplay(Shift shift) {
        System.out.println("""
                Shift Date: %s
                Start: %s\tEnd: %s
                
                """.formatted(shift.getDate(), shift.getStart(), shift.getEnd()));

        System.out.println("""
                1) Assign Employee
                2) Get Employees Preferences
                3) Close Shift
                4) Open Shift
                5) Get Shift's Status
                6) Get Assigned Employees
                7) Get Assigned Drivers
                8) Remove Employee From Shift
                To Return to the previous menu press 0
                
                """);
    }

    public void weeksMenu() {
        int action;
        do {
            DisMWeeksShiftMenu();
            action = scanner.nextInt();
            if (action == 0)
                break;
            scanner.nextLine();
            cliController.MWeeksMenu(action);
        }while (true);
    }


    public void DisMWeeksShiftMenu(){
        System.out.println("""
                1) This week shifts 
                2) Next week shifts
                To return to the previous menu press 0
                
                """);
    }


    //----------------------------------------------------------------------------




    //------------------------------EMPLOYEE ONLY---------------------------------

    public void EmainMenu() {
        int action;
        do {
            DisEmainMenu();
            action = scanner.nextInt();
            if (action == 5)
                break;
            scanner.nextLine();
            cliController.EmainMenu(action);
        } while (true);

    }

    public void DisEmainMenu(){
        printNotifications(cliController.EmpNotifications());
        System.out.println("""
                
                1) Show all my information
                2) Show my preferences for a shift
                3) Apply preferences for a shift
                4) Show colleagues whom work with me in a shift
                To return to the previous menu press 0
                
                """);
    }

    //-----------------------------------------------------------------------------


    public void displayShift(Shift s) {
        System.out.println("""
                Date: %s
                Start: %s\tEnd: %s
                
                """.formatted(s.getDate(), s.getStart(), s.getEnd()));
    }

    public void displayWeekly(List<WeeklyShifts> weeks) {
        for(WeeklyShifts week: weeks)
            for(Shift s: week.getShifts())
                displayShift(s);
    }

    //display `msg` to the user and returns an int read from the user
    public int getInt(String msg) {
        System.out.println(msg);
        int out = scanner.nextInt();
        scanner.nextLine();
        return out;
    }

    //display `msg` to the user and returns a string read from the user
    public String getString(String msg) {
        System.out.println(msg);
        String out = scanner.nextLine();
//        scanner.nextLine();
        return out;
    }

    //display `msg` to the user and returns LocalDate read from the user
    public LocalDate getDate(String msg) {
        System.out.println(msg);
        System.out.println("Enter the day:");
        int day = scanner.nextInt();
        while(day <= 0 || day > 31){
            System.out.println("Invalid day");
            day = scanner.nextInt();
        }

        System.out.println("Enter the month:");
        int month = scanner.nextInt();
        while(month <= 0 || month > 12){
            System.out.println("Invalid month");
            month = scanner.nextInt();
        }

        System.out.println("Enter the year:");
        int year = scanner.nextInt();
        return LocalDate.of(year,month,day);
    }

    public char getChar(String msg) {
        System.out.println(msg);
        char out = scanner.next().charAt(0);
        scanner.nextLine();
        return out;
    }

    //display `msg` to the user
    public void print(String msg) {
        System.out.println(msg);
    }


    public void displayEmployees(List<Employee> employees, Shift shift) {
        for (Employee e: employees){
            System.out.println("""
                    ID: %s\tName: %s\t\tRoles: %s
                    
                    """.formatted(e.getID().getValue(), e.getName().getValue(),
                    FacadeController.getInstance().getAllAssignedRolesForEmployeeInShift
                            (e.getID().getValue(), shift.getDate(), shift.getStart(), shift.getEnd()).getValue()));
        }
    }

    public void runWithConsole() {
    }

//    public Delivery.BusinessLayer.FacadeController getFacade() {
//    }
}