package packages.pages;

import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import packages.datastructures.*;
import packages.exceptions.*;

@SuppressWarnings("resource")
class ModifyTeacher extends Page{
    public ModifyTeacher(Datasets datasets){
        this.datasets = datasets;
        this.table = new Table();
    }

    public void displayMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean endMenu = false;

        while (!endMenu) {
            table.clear();
            table.addColumn("Modify Teachers");
            ArrayList<String> teacherIds = new ArrayList<>(); 
            int index = 1;

            for (Map.Entry<String, String> entry : datasets.userLoginDetails.loginDetails.entrySet()) {
                String teacherId = entry.getKey();
                if (Pattern.matches("T\\d{4}", teacherId)) {
                    teacherIds.add(teacherId);
                    table.addRow(index + ". " + teacherId);
                    index++;
                }
            }

            table.addRow(index + ". Add another teacher");
            table.addRow((index + 1) + ". Exit");

            table.print(); 

            
            System.out.print("\nEnter choice: ");
            int choice = Integer.parseInt(scanner.nextLine());

            
            if (choice >= 1 && choice < index) {
                String selectedTeacherId = teacherIds.get(choice - 1); 
                displayTeacherDetails(selectedTeacherId);
            } else if (choice == index) {
                createNewTeacher();
                //System.out.println("Add another teacher functionality not implemented yet.");
            } else if (choice == index + 1) {
                endMenu = true;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void displayTeacherDetails(String teacherId) {
        String details = datasets.userLoginDetails.loginDetails.get(teacherId);
        System.out.println("Details for " + teacherId + ": " + details);

        LinkedHashMap<String, String[]> timetable = datasets.timeTableAllotment.teacherTimeTables.get(teacherId);

        if (timetable != null) {
            
            Table timetableTable = new Table();

            timetableTable.addColumn("Day");
            int maxPeriods = 0;

            
            for (String[] periods : timetable.values()) {
                if (periods.length > maxPeriods) {
                    maxPeriods = periods.length;
                }
            }

            
            for (int i = 1; i <= maxPeriods; i++) {
                timetableTable.addColumn("Period " + i);
            }

            
            for (Map.Entry<String, String[]> dayEntry : timetable.entrySet()) {
                String day = dayEntry.getKey();
                String[] periods = dayEntry.getValue();
                String[] row = new String[maxPeriods + 1]; 
                row[0] = day; 

                
                for (int i = 0; i < maxPeriods; i++) {
                    if (i < periods.length && periods[i] != null && !periods[i].isEmpty()) {
                        row[i + 1] = periods[i]; 
                    } else {
                        row[i + 1] = "Free"; 
                    }
                }
                timetableTable.addRow(row);
            }

            
            System.out.println("Timetable for " + teacherId + ":");
            timetableTable.print();

            
            Scanner scanner = new Scanner(System.in);
            System.out.print("Do you want to modify the timetable for " + teacherId + "? (yes/no): ");
            String modifyChoice = scanner.nextLine().trim().toLowerCase();

            if (modifyChoice.equals("yes")) {
                datasets.timeTableAllotment.createTimeTable(teacherId, maxPeriods);
            }
        } else {
            System.out.println("No timetable available for " + teacherId + ".");
        }
    }

    private void createNewTeacher() {
        Scanner scanner = new Scanner(System.in);
        String teacherId = "";
        boolean validId = false;

        
        while (!validId) {
            System.out.print("Enter Teacher ID (format: T[0-9]{4}): ");
            teacherId = scanner.nextLine().trim();

            if (isValidTeacherId(teacherId)) {
                if (datasets.userLoginDetails.loginDetails.containsKey(teacherId)) {
                    try {
                        throw new UserIDAlreadyExistsException(teacherId);
                    } catch (UserIDAlreadyExistsException e) {
                        System.out.println(e);
                    }
                } else {
                    validId = true;
                }
            } else {
                try {
                    throw new InvalidIDException('T');
                } catch (InvalidIDException e) {
                    System.out.println(e);
                }
            }
        }

        
        String password = "";
        boolean validPassword = false;

        while (!validPassword) {
            System.out.print("Enter password (at least 8 characters, includes 1 digit and 1 special character): ");
            password = scanner.nextLine().trim();
            if (isValidPassword(password)) {
                validPassword = true;
            } else {
                try {
                    throw new WeakPasswordException();
                } catch (WeakPasswordException e) {
                    System.out.println(e);
                }
            }
        }

        
        datasets.userLoginDetails.loginDetails.put(teacherId, password);
        datasets.timeTableAllotment.createTimeTable(teacherId, 5);

        System.out.println("Teacher created successfully with ID: " + teacherId);
    }

    private boolean isValidTeacherId(String teacherId) {
        return teacherId.matches("T\\d{4}");
    }

    private boolean isValidPassword(String password) {
        Pattern passwordPattern = Pattern.compile( "^(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$");
        return passwordPattern.matcher(password).matches();
    }
}

@SuppressWarnings("resource")
class ModifyClassroom extends Page {
    private Datasets datasets;
    private Table table;
    private static final Pattern STUDENT_ID_PATTERN = Pattern.compile("S\\d{4}");
    private static final Pattern CLASS_ID_PATTERN = Pattern.compile("H\\d{4}");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$");

    public ModifyClassroom(Datasets datasets) {
        this.datasets = datasets;
        this.table = new Table();
    }

    public void displayMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean endMenu = false;

        while (!endMenu) {
            table.clear();
            table.addColumn("Class IDs:");
            ArrayList<String> classIds = new ArrayList<>();
            int index = 1;

            
            for (String classID : datasets.studentClassAllotment.classStudentList.keySet()) {
                classIds.add(classID);
                table.addRow(index + ". " + classID);
                index++;
            }

            
            table.addRow(index + ". Add New Class");
            table.addRow((index + 1) + ". Exit");

            table.print();

            
            System.out.print("\nEnter choice: ");
            int choice = Integer.parseInt(scanner.nextLine());

            
            if (choice >= 1 && choice < index) {
                String selectedClassID = classIds.get(choice - 1);
                displayClassTimeTable(selectedClassID);
                displayStudentsForClass(selectedClassID, scanner);
            } else if (choice == index) {
                addNewClass(scanner);
            } else if (choice == index + 1) {
                endMenu = true;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void displayClassTimeTable(String classID) {
        LinkedHashMap<String, String[]> timetable = datasets.timeTableAllotment.classTimeTables.get(classID);

        if (timetable != null) {
            Table timetableTable = new Table();

            timetableTable.addColumn("Day");
            int maxPeriods = 0;

            
            for (String[] periods : timetable.values()) {
                if (periods.length > maxPeriods) {
                    maxPeriods = periods.length;
                }
            }

            
            for (int i = 1; i <= maxPeriods; i++) {
                timetableTable.addColumn("Period " + i);
            }

            
            for (Map.Entry<String, String[]> dayEntry : timetable.entrySet()) {
                String day = dayEntry.getKey();
                String[] periods = dayEntry.getValue();
                String[] row = new String[maxPeriods + 1]; 
                row[0] = day; 

                for (int i = 0; i < maxPeriods; i++) {
                    if (i < periods.length && periods[i] != null && !periods[i].isEmpty()) {
                        row[i + 1] = periods[i]; 
                    } else {
                        row[i + 1] = "Free"; 
                    }
                }
                timetableTable.addRow(row); 
            }

            System.out.println("Timetable for " + classID + ":");
            timetableTable.print();
        } else {
            System.out.println("No timetable available for " + classID + ".");
        }
    }

    private void displayStudentsForClass(String classID, Scanner scanner) {
        ArrayList<String> studentIDs = datasets.studentClassAllotment.classStudentList.get(classID);
        table.clear();
        table.addColumn("Students in " + classID + ":");

        for (String studentID : studentIDs) {
            table.addRow(studentID);
        }
        table.print();

        
        System.out.print("Do you want to add students to this class? (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();

        if (response.equals("yes")) {
            boolean continueAdding = true;
            while (continueAdding) {
                System.out.print("Enter the range of student IDs to add (startID endID): ");
                String[] rangeInput = scanner.nextLine().trim().split(" ");
                if (rangeInput.length != 2) {
                    System.out.println("Please enter two IDs.");
                    continue;
                }

                String startID = rangeInput[0];
                String endID = rangeInput[1];
                try {
                    addStudentsInRange(classID, startID, endID);
                    continueAdding = false; 
                } catch (InvalidIDException | WeakPasswordException | UserIDAlreadyExistsException e) {
                    System.out.println(e);
                }
            }
        }
    }

    private void addStudentsInRange(String classID, String startID, String endID)
            throws InvalidIDException, WeakPasswordException, UserIDAlreadyExistsException {
        if (!STUDENT_ID_PATTERN.matcher(startID).matches() || !STUDENT_ID_PATTERN.matcher(endID).matches()) {
            throw new InvalidIDException('S');
        }

        
        int startNum = Integer.parseInt(startID.substring(1)); 
        int endNum = Integer.parseInt(endID.substring(1));

        for (int i = startNum; i <= endNum; i++) {
            String studentID = "S" + String.format("%04d", i); 
            if (datasets.userLoginDetails.loginDetails.containsKey(studentID) ||
                    datasets.studentClassAllotment.studentToClasses.containsKey(studentID)) {
                throw new UserIDAlreadyExistsException(studentID);
            }

            String password = "password@" + studentID;
            if (!PASSWORD_PATTERN.matcher(password).matches()) {
                throw new WeakPasswordException();
            }

            datasets.studentClassAllotment.classStudentList.get(classID).add(studentID);
            datasets.studentClassAllotment.studentToClasses.put(studentID, classID);
            datasets.userLoginDetails.loginDetails.put(studentID, password);
            System.out.println("Student " + studentID + " added successfully.");
        }
    }

    private void addNewClass(Scanner scanner) {
        String newClassID;
        while (true) {
            System.out.print("Enter new class ID: ");
            newClassID = scanner.nextLine().trim();
            if (CLASS_ID_PATTERN.matcher(newClassID).matches()) {
                if (!datasets.studentClassAllotment.classStudentList.containsKey(newClassID)) {
                    datasets.studentClassAllotment.classStudentList.put(newClassID, new ArrayList<>());
                    System.out.println("Class added successfully.");
                    break;
                } else {
                    System.out.println("Class ID already exists. Please enter a different class ID.");
                }
            } else {
                System.out.println("Invalid class ID format. Please try again.");
            }
        }
    }
}

@SuppressWarnings("resource")
class ModifyAdmin extends Page{

    String currentID;

    ModifyAdmin(Datasets datasets, String currentID){
        this.datasets = datasets;
        this.table = new Table();
        this.currentID = currentID;
    }

    public void displayMenu(){
        Scanner input = new Scanner(System.in);
        boolean endMenu = false;
        table.addColumn("Modify Admin:");
        table.addRow("1. Change my password");
        table.addRow("2. Create new Admin");
        table.addRow("3. Exit");

        while(!endMenu){
            table.print();
            boolean gotChoice = false;
            while(!gotChoice){
                System.out.print("\nEnter choice : ");
                int choice = Integer.parseInt(input.nextLine());
                switch(choice){
                    case 1:
                        gotChoice = true;
                        changePassword();
                        break;
                    case 2:
                        gotChoice = true;
                        createAdmin();
                        break;
                    case 3:
                        gotChoice = true;
                        endMenu = true;
                        break;
                    default:
                        System.out.println("Invalid Input, enter again!");
                }
            }
        }
    }

    private void changePassword(){
        Scanner scanner = new Scanner(System.in);
        String password = "";
        boolean validPassword = false;

        while (!validPassword) {
            System.out.print("Enter new password (at least 8 characters, includes 1 digit and 1 special character): ");
            password = scanner.nextLine();
            if (isValidPassword(password)) {
                validPassword = true;
            }
            else {
                try {
                    throw new WeakPasswordException();
                }
                catch (WeakPasswordException e) {
                    System.out.println(e);
                }
            }
        }

        datasets.userLoginDetails.loginDetails.put(currentID, password);
        System.out.println("Successfully Made Changes!");
    }

    private void createAdmin() {
        Scanner scanner = new Scanner(System.in);
        String userId = "";
        boolean validId = false;

        while (!validId) {
            System.out.print("Enter user ID (format: A[0-9]{4}): ");
            userId = scanner.nextLine();

            if (isValidUserId(userId)) {
                if (datasets.userLoginDetails.loginDetails.containsKey(userId)) {
                    try {
                        throw new UserIDAlreadyExistsException(userId);
                    }
                    catch (UserIDAlreadyExistsException e) {
                        System.out.println(e);
                    }
                }
                else {
                    validId = true;
                }
            }
            else {
                try {
                    throw new InvalidIDException('A');
                }
                catch (InvalidIDException e) {
                    System.out.println(e);
                }
            }
        }
        changePassword();
        /*String password = "";
        boolean validPassword = false;

        while (!validPassword) {
            System.out.print("Enter password (at least 8 characters, includes 1 digit and 1 special character): ");
            password = scanner.nextLine();
            if (isValidPassword(password)) {
                validPassword = true;
            }
            else {
                try {
                    throw new WeakPasswordException();
                }
                catch (WeakPasswordException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        datasets.userLoginDetails.loginDetails.put(userId, password);*/
        System.out.println("New Admin created!");
    }

    private boolean isValidUserId(String userId) {
        return Pattern.matches("A\\d{4}", userId);
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 8 &&
                Pattern.compile("[0-9]").matcher(password).find() &&
                Pattern.compile("[!@#$%^&*(),.?\":{}|<>]").matcher(password).find();
    }


}

@SuppressWarnings("resource")
class ModifyUsersPage extends Page{
    String currentID;

    public ModifyUsersPage(Datasets datasets, String currentID){
        this.datasets = datasets;
        this.table = new Table();
        this.currentID = currentID;
    }

    public void displayMenu(){
        Scanner input = new Scanner(System.in);
        boolean endMenu = false;
        table.addColumn("Modify users:");
        table.addRow("1. Modify Admin");
        table.addRow("2. Modify Teachers");
        table.addRow("3. Modify Classrooms");
        table.addRow("4. Exit");

        while(!endMenu){
            table.print();
            boolean gotChoice = false;
            while(!gotChoice){
                System.out.print("\nEnter choice : ");
                int choice = Integer.parseInt(input.nextLine());
                switch(choice){
                    case 1:
                        gotChoice = true;
                        ModifyAdmin modifyAdmin = new ModifyAdmin(datasets, currentID);
                        modifyAdmin.displayMenu();
                        break;
                    case 2:
                        gotChoice = true;
                        ModifyTeacher modifyTeacher = new ModifyTeacher(datasets);
                        modifyTeacher.displayMenu();
                        break;
                    case 3:
                        gotChoice = true;
                        ModifyClassroom modifyClassroom = new ModifyClassroom(datasets);
                        modifyClassroom.displayMenu();
                        break;
                    case 4:
                        //datasets.studentAttendance.populateTeacherDailyAttendance();
                        gotChoice = true;
                        endMenu = true;
                        break;
                    default:
                        System.out.println("Invalid Input, enter again!");
                }
            }
        }
        //input.close();
    }

}

@SuppressWarnings("resource")
class AdminPage extends Page {

    String currentID;

    public AdminPage(Datasets datasets, String currentID){
        this.table = new Table();
        this.datasets = datasets;
        this.currentID = currentID;
    }

    public void displayMenu(){
        Scanner input = new Scanner(System.in);
        boolean endMenu = false;
        table.addColumn("Admin Dashboard");
        table.addRow("What would you like to do?");
        table.addRow(" ");
        table.addRow("1. Modify users");
        table.addRow("2. Edit Academic Calendar");
        table.addRow("3. Logout");

        while(!endMenu){
            table.print();
            boolean gotChoice = false;
            while(!gotChoice){
                System.out.print("\nEnter choice : ");
                int choice = Integer.parseInt(input.nextLine());
                switch(choice){
                    case 1:
                        gotChoice = true;
                        ModifyUsersPage createNewPage = new ModifyUsersPage(datasets,currentID);
                        createNewPage.displayMenu();
                        break;
                    case 2:
                        gotChoice = true;
                        editAcademicCalendar();
                        break;
                    case 3:
                        datasets.saveData("src/data/datasets.ser");

                        //datasets.studentAttendance.populateTeacherDailyAttendance();
                        //System.out.println(datasets.studentAttendance.teacherDailyAttendance.get("T0006"));
                        datasets.printData();
                        gotChoice = true;
                        endMenu = true;
                        break;
                    default:
                        System.out.println("Invalid Input, enter again!");
                }
            }
        }
        //input.close();
    }

    public void editAcademicCalendar(){
        datasets.academicCalendar.printCalendar();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Want to create new academic calendar? (yes/no): ");
        String modifyChoice = scanner.nextLine().trim().toLowerCase();

        if (modifyChoice.equals("yes")) {
            datasets.academicCalendar.createAcademicCalendar();
        }
        System.out.println("Academic Calendar Modified!");
    }

    public static void main(String[] args) {
        Datasets datasets = Datasets.loadData("src/data/datasets.ser");
        AdminPage adminPage = new AdminPage(datasets, "A1000");
        adminPage.displayMenu();

    }
}