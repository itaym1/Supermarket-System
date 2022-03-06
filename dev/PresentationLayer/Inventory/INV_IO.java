package PresentationLayer.Inventory;


import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class INV_IO {

    private static INV_IO instance;
    private IO_Controller ioCtrl;
    private Scanner scanner;

    // Singletone class
    public static INV_IO getInstance() {
        if (instance == null) {
            instance = new INV_IO();
        }
        return instance;
    }

    private INV_IO() {
        ioCtrl = new IO_Controller();
        scanner = new Scanner(System.in);
    }

    //starts the main menu of the program
    public void start() {
        int action;
        do {
            System.out.println("1) Accept or Reject Order\n" +
                    "2) Reports\n" +
                    "3) Stock Management\n" +
                    "\n9) Exit");
            try {
                action = scanner.nextInt();
                if (action > 3 || action < 1)
                    break;
                scanner.nextLine();
                ioCtrl.mainMenu(action);
            } catch (InputMismatchException e) {
                System.out.println("Illegal input");
                scanner.nextLine();
            }
        } while (true);

    }

    //starts the edit menu of the program
    public void editMenu() {
        int action;
        do {
            System.out.println("1) Add Item discount\n" +
                    "2) Add Category discount\n" +
                    "3) Add manufacturer discount\n"+
                    "7) Remove Item\n" +
                    "5) Change item shelf\n" +
                    "6) Move item to shelf from storage\n" +
                    "\n9) Back");
            try {
                action = scanner.nextInt();
                if (action > 6 || action < 1)
                    break;
                scanner.nextLine();
                ioCtrl.editMenu(action);
            } catch (InputMismatchException e) {
                System.out.println("Illegal input");
                scanner.nextLine();
            }
        } while (true);
    }

    //display `msg` to the user and returns an int read from the user
    public int getInt(String msg) {
        try {
            System.out.println(msg);
            int out = scanner.nextInt();
            scanner.nextLine();
            return out;
        } catch (InputMismatchException err) {
            scanner.nextLine();
            return getInt("Invalid input - please enter a whole number");
        }
    }

    //display `msg` to the user and returns a double read from the user
    public double getDouble(String msg) {
        try {
            System.out.println(msg);
            return scanner.nextDouble();
        } catch (InputMismatchException err) {
            scanner.nextLine();
            return getDouble("Invalid input - please enter a number");
        }

    }

    //display `msg` to the user and returns a string read from the user
    public String getString(String msg) {
        System.out.println(msg);
        return scanner.nextLine();
    }

    //display `msg` to the user and returns LocalDate read from the user
    public LocalDate getDate(String msg) {
        try {
            System.out.println(msg);
            System.out.println("Enter the day:");
            int day = scanner.nextInt();
            System.out.println("Enter the month:");
            int month = scanner.nextInt();
            System.out.println("Enter the year:");
            int year = scanner.nextInt();
            return LocalDate.of(year,month,day);
        } catch (InputMismatchException err) {
            scanner.nextLine();
            return getDate("Invalid input - please enter the date as whole numbers");
        }

    }
    //display `msg` to the user
    public void print(String msg) {
        System.out.println(msg);
    }

    public void badInput(String msg) {
        print(msg);
        scanner.nextLine();
    }

    public List<String> getList(String msg) {
        System.out.println(msg);
        List<String> cats = new LinkedList<>();
        String ans = "";
        int i = 1;
        while (!ans.equals("-1")) {
            System.out.println("'-1' to exit");
            ans = getString("Enter category name #" + i++);
            if (!ans.equals("-1")) {
                cats.add(ans);
            }
        }
        return cats;
    }

    public void reportMenu() {
        int action;
        do {
            System.out.println("1) Stock Report\n" +
                    "2) Faulty Report\n" +
                    "3) Category Report\n" +
                    "\n9) Exit");
            try {
                action = scanner.nextInt();
                if (action > 3 || action < 1)
                    break;
                scanner.nextLine();
                ioCtrl.reportsMenu(action);
            } catch (InputMismatchException e) {
                System.out.println("Illegal input");
                scanner.nextLine();
            }
        } while (true);
    }

    public void stockMenu() {
        int action;
        do {
            System.out.println("1) Add Sale\n" +
                    "2) Report Faulty\n" +
                    "3) Add Item\n" +
                    "4) Add Category\n" +
                    "5) Edit" +
                    "\n9) Exit");
            try {
                action = scanner.nextInt();
                if (action > 5 || action < 1)
                    break;
                scanner.nextLine();
                ioCtrl.stockMenu(action);
            } catch (InputMismatchException e) {
                System.out.println("Illegal input");
                scanner.nextLine();
            }
        } while (true);
    }
}
