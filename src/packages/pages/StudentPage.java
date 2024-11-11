package packages.pages;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import packages.datastructures.*;
import packages.exceptions.InvalidIDException;
import packages.exceptions.UserIDNotFoundException;
import packages.exceptions.WeakPasswordException;

@SuppressWarnings("resource")
public class StudentPage extends Page {

    String currentID;
    
    public StudentPage(Datasets datasets,String currentID){
        this.table = new Table();
        this.datasets = datasets;
        this.currentID = currentID;
    }

    public void displayMenu(){
        datasets=Datasets.loadData("src/data/datasets.ser");
        Scanner input = new Scanner(System.in);
        boolean endMenu = false;
        table.addColumn("Student Dashboard");
        table.addRow("What would you like to do?");
        table.addRow(" ");
        table.addRow("1. Change my password");
        table.addRow("2. Show Time Table");
        table.addRow("3. Show Attendance Report");
        table.addRow("4. Logout");
        
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
                        displayStudentDetails();
                        break;
                    case 3:
                        gotChoice = true;
                        showAttendanceReport();
                        break;
                    case 4:
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

    private boolean isValidPassword(String password) {
        return password.length() >= 8 &&
                Pattern.compile("[0-9]").matcher(password).find() &&
                Pattern.compile("[!@#$%^&*(),.?\":{}|<>]").matcher(password).find();
    }

    private void displayStudentDetails() {
        String details = datasets.userLoginDetails.loginDetails.get(currentID);
        System.out.println("Details for " + currentID + ": " + details);

        
        String classID = datasets.studentClassAllotment.studentToClasses.get(currentID);
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

            
            System.out.println("Timetable for " + currentID + ":");
            timetableTable.print();
        } else {
            System.out.println("No timetable available for " + currentID + ".");
        }
    }

    public void showAttendanceReport() {
        Scanner scanner = new Scanner(System.in);
        String teacherID = "";
        String courseID = "";
        boolean validInput = false;

        while (!validInput) {
            try {
                System.out.print("Enter Teacher ID (format: TXXXX): ");
                teacherID = scanner.nextLine();
                validateTeacherID(teacherID);

                if (!datasets.userLoginDetails.loginDetails.containsKey(teacherID)) {
                    throw new UserIDNotFoundException(teacherID);
                }

                System.out.print("Enter Course ID (format: CXXXX): ");
                courseID = scanner.nextLine();
                validateCourseID(courseID);

                validInput = true; 

            } catch (InvalidIDException e) {
                System.out.println("Invalid ID format. Please try again.");
            } catch (UserIDNotFoundException e) {
                System.out.println("Teacher ID not found. Please try again.");
            }
        }
        float present_count=0;
        int total_count=0;
        String classID=datasets.studentClassAllotment.studentToClasses.get(currentID);
        String teacherPeriod=courseID+"/"+classID;
        System.out.println("generated Teacher Period:"+teacherPeriod);
        for(String Date:datasets.academicCalendar.academicCalendar.keySet()) {
            if(datasets.studentAttendance.teacherDailyAttendance.get(teacherID).get(Date).get(teacherPeriod)==null){
                continue;
            }
            Boolean x = datasets.studentAttendance.teacherDailyAttendance.get(teacherID).get(Date).get(teacherPeriod).get(currentID);
            //System.out.println(x);
            //System.out.println(Date+":"+datasets.studentAttendance.teacherDailyAttendance.get(teacherID).get(Date).get(teacherPeriod));

            if(x==null) {
                continue;
            } else if(x==true){
                present_count++;
                total_count++;
            }else if(x==false){
                total_count++;
            }


            //System.out.println("Hi");
        }

        //System.out.println("present Count:"+present_count+"Total Count"+total_count);
        System.out.println("Overall Attendance Percentage:"+(present_count/total_count)*100.0+"%");

        
    }

    
    private void validateTeacherID(String teacherID) throws InvalidIDException {
        Pattern pattern = Pattern.compile("^T\\d{4}$");
        Matcher matcher = pattern.matcher(teacherID);
        if (!matcher.matches()) {
            throw new InvalidIDException('T');
        }
    }

    private void validateCourseID(String courseID) throws InvalidIDException {
        Pattern pattern = Pattern.compile("^C\\d{4}$");
        Matcher matcher = pattern.matcher(courseID);
        if (!matcher.matches()) {
            throw new InvalidIDException('C');
        }
    }

    public static void main(String[] args) {
        Datasets datasets = Datasets.loadData("src/data/datasets.ser");
        StudentPage studentPage = new StudentPage(datasets, "S0001");
        studentPage.displayMenu();

    }
}

