package packages.pages;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import packages.datastructures.*;
import packages.exceptions.InvalidDateException;
import packages.exceptions.InvalidIDException;
import packages.exceptions.WeakPasswordException;

@SuppressWarnings("resource")
class TeacherPage extends Page {

    String currentID;
    public TeacherPage(Datasets datasets, String currentID){
        this.table = new Table();
        this.datasets = datasets;
        this.currentID = currentID;
    }

    public void displayMenu(){

        Scanner input = new Scanner(System.in);
        boolean endMenu = false;
        table.addColumn("Teacher Dashboard");
        table.addRow("What would you like to do?");
        table.addRow(" ");
        table.addRow("1. Change my password");
        table.addRow("2. Display my TimeTable");
        table.addRow("3. Take Attendance");
        table.addRow("4. Check Overall Attendance of Students");
        table.addRow("5. Grant Absence Exemption");
        table.addRow("6. Logout");

        while(!endMenu){
            table.print();
            boolean gotChoice = false;
            while(!gotChoice){
                System.out.print("\nEnter choice : ");
                int choice = Integer.parseInt(input.nextLine());
                switch(choice){
                    case 1:
                        gotChoice=true;
                        changePassword();
                        break;
                    case 2:
                        gotChoice=true;
                        displayTeacherDetails(currentID);
                        break;
                    case 3:
                        datasets=Datasets.loadData("src/data/datasets.ser");
                        gotChoice=true;
                        System.out.print("\nEnter Date(yyyy/mm/dd) : ");
                        String date=input.nextLine();
                        String day=datasets.academicCalendar.academicCalendar.get(date);

                        List<String> arrayList = new ArrayList<>(Arrays.asList(datasets.timeTableAllotment.teacherTimeTables.get(currentID).get(day)));
                        arrayList.removeIf(item -> item == null);
                        System.out.print("\nSelect Period to take attendance :- \n");
                        for (int i = 0; i < arrayList.size(); i++) {
                            System.out.println((i+1)+"."+arrayList.get(i));
                        }
                        boolean allNull = arrayList != null && arrayList.stream().allMatch(element -> element == null);
                        if(allNull){
                            System.out.println("\nNo periods on this date");
                            break;
                        }
                        System.out.print("\nEnter choice : ");
                        int period = Integer.parseInt(input.nextLine());
                        System.out.println("\nStudents List:\n");
                        for(String StudentID :datasets.studentAttendance.teacherDailyAttendance.get(currentID).get(date).get(arrayList.get(period-1)).keySet()){

                            int c=0;
                            while(c==0) {
                                System.out.print(StudentID+"(1 for present|0 for absent):");
                                int bool = Integer.parseInt(input.nextLine());
                                if (bool == 1) {
                                    datasets.studentAttendance.teacherDailyAttendance.get(currentID).get(date).get(arrayList.get(period - 1)).put(StudentID, true);
                                    //System.out.println(datasets.studentAttendance.teacherDailyAttendance.get(currentID).get(date).get(arrayList.get(period - 1)).get(StudentID));
                                    c=1;
                                } else if (bool == 0) {
                                    datasets.studentAttendance.teacherDailyAttendance.get(currentID).get(date).get(arrayList.get(period - 1)).put(StudentID, false);
                                    c=1;
                                } else {
                                    System.out.println("Invalid Entry");
                                }
                            }

                        }
                        //datasets.saveData("src/data/datasets.ser");

                        //System.out.println(datasets.studentAttendance.teacherDailyAttendance.get("T0001").get("2024/10/01"));
                        break;
                    case 4:
                        datasets.saveData("src/data/datasets.ser");
                        datasets=Datasets.loadData("src/data/datasets.ser");
                        gotChoice=true;
                        //System.out.println(datasets.studentAttendance.teacherDailyAttendance.get("T0001").get("2024/10/01"));
                        System.out.println("\nEnter Course/Hall:");
                        String teacherPeriod=input.nextLine();
                        Map<String, Integer> OverallAttendance = new TreeMap<>();
                        for(String tempDate:datasets.academicCalendar.academicCalendar.keySet()){
                            //System.out.println(tempDate);
                            //System.out.println(datasets.studentAttendance.teacherDailyAttendance.get(currentID).get(tempDate).get(teacherPeriod));
                            if(datasets.studentAttendance.teacherDailyAttendance.get(currentID).get(tempDate).get(teacherPeriod)==null){
                                continue;
                            }
                            Set<String> keys=datasets.studentAttendance.teacherDailyAttendance.get(currentID).get(tempDate).get(teacherPeriod).keySet();
                            Collection<Boolean> valuesCollection = datasets.studentAttendance.teacherDailyAttendance.get(currentID).get(tempDate).get(teacherPeriod).values();
                            ArrayList<Boolean> values = new ArrayList<>(valuesCollection);
                            ArrayList<String> students = new ArrayList<>(keys);
                            for(int i = 0; i < students.size(); i++){
                                if(values.get(i)==null){
                                    continue;
                                } else if (values.get(i)==true) {
                                    OverallAttendance.put(students.get(i), OverallAttendance.getOrDefault(students.get(i), 0) + 1);
                                }else if(values.get(i)==false){
                                    OverallAttendance.put(students.get(i), OverallAttendance.getOrDefault(students.get(i), 0));
                                }

                            }
                        }
                        for (Map.Entry<String, Integer> entry : OverallAttendance.entrySet()) {
                            String item = entry.getKey();
                            int count = entry.getValue();
                            int size=datasets.academicCalendar.academicCalendar.size();
                            System.out.printf("%s: %d/%d \n", item,count,size);
                        }

                        break;
                    case 5:
                        gotChoice=true;
                        grantAbsenceExemption();
                        break;
                    case 6:
                        datasets.saveData("src/data/datasets.ser");
                        gotChoice=true;
                        endMenu=true;
                        break;
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

    public void grantAbsenceExemption() {
        Scanner scanner = new Scanner(System.in);

        // Step 1: Input studentID with validation
        String studentID = getStudentID(scanner);
        
        // Step 2: Continuously ask for dates until user types "done"
        while (true) {
            System.out.println("Enter a date (yyyy/mm/dd) or type 'done' to finish: ");
            String date = scanner.nextLine().trim();

            if (date.equalsIgnoreCase("done")) {
                break;  
            }

            try {
                // Step 3: Validate date presence in academicCalendar
                if (!datasets.academicCalendar.academicCalendar.containsKey(date)) {
                    throw new InvalidDateException('A');
                }

                // Step 4: Process periods for the given date
                System.out.println("Processing date: " + date + " (" + datasets.academicCalendar.academicCalendar.get(date) + ")");
                processPeriodsForDate(date, studentID);

            } catch (InvalidDateException e) {
                System.out.println(e);  
            }
        }
    }

    private void processPeriodsForDate(String date, String studentID) {
        // Step 5: Convert the input date (yyyy/mm/dd) to a weekday name
        String dayOfWeek = datasets.academicCalendar.academicCalendar.get(date);  
    
        if (dayOfWeek == null) {
            System.out.println("Invalid date. No day found in the academic calendar for " + date);
            return;  // Exit if the date is not found in the academic calendar
        }
    
        // Step 6: Get the teacher's timetable for this weekday
        LinkedHashMap<String, String[]> teacherSchedule = datasets.timeTableAllotment.teacherTimeTables.get(currentID);
    
        if (teacherSchedule != null && teacherSchedule.containsKey(dayOfWeek)) {
            String[] availablePeriods = teacherSchedule.get(dayOfWeek);
    
            // Step 7: Filter periods where the student is present in the class
            Table table = new Table();
            table.addColumn("Period No.");
            table.addColumn("Period Description");
    
            int periodCounter = 1;  
            for (int i = 0; i < availablePeriods.length; i++) {
                String periodDescription = availablePeriods[i];
    
                if (periodDescription != null && !periodDescription.isEmpty()) {
                    
                    String[] parts = periodDescription.split("/");
                    if (parts.length == 2) {
                        String classID = parts[1];  
    
                        // Step 8: Check if student is in the class
                        if (datasets.studentClassAllotment.studentToClasses.containsKey(studentID) &&
                            datasets.studentClassAllotment.studentToClasses.get(studentID).equals(classID)) {
    
                            
                            table.addRow(String.valueOf(periodCounter), periodDescription);
                            periodCounter++;  
                        }
                    }
                }
            }
    
            
            System.out.println("Available periods for student " + studentID + " on " + date + " (" + dayOfWeek + "):");
            table.print();
    
            if (periodCounter == 1) {
                
                System.out.println("No valid periods available for exemption for this student on this day.");
                return;
            }
    
            // Step 9: Ask user to input periods to mark as exempt
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the period numbers to mark as exempt (comma separated): ");
            String input = scanner.nextLine();
            String[] selectedPeriods = input.split(",");
    
            // Step 10: Update the attendance for the selected periods
            for (String period : selectedPeriods) {
                try {
                    int periodNumber = Integer.parseInt(period.trim()) - 1; 
                    if (periodNumber >= 0 && periodNumber < availablePeriods.length) {
                        String teacherPeriod = availablePeriods[periodNumber];
    
                        
                        if (teacherPeriod != null && !teacherPeriod.isEmpty()) {
                            
                            Map<String, Map<String, Boolean>> dateAttendance = datasets.studentAttendance.teacherDailyAttendance
                                    .computeIfAbsent(currentID, k -> new TreeMap<>())  
                                    .computeIfAbsent(date, k -> new TreeMap<>());
                            Map<String, Boolean> periodAttendance = dateAttendance
                                    .computeIfAbsent(teacherPeriod, k -> new TreeMap<>());
                            periodAttendance.put(studentID, true);
    
                            System.out.println("Marked period " + (periodNumber + 1) + " as exempt for student " + studentID);
                        } else {
                            System.out.println("Invalid period number selected.");
                        }
                    } else {
                        System.out.println("Invalid period number: " + period);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter valid period numbers.");
                }
            }
        } else {
            System.out.println("No periods scheduled for teacher on " + date + " (" + dayOfWeek + ")");
        }
    }
    
    private String getStudentID(Scanner scanner) {
        String studentID;
        while (true) {
            System.out.print("Enter studentID (S[0-9]{4}): ");
            studentID = scanner.nextLine().trim();

            try {
                if (!isValidStudentID(studentID)) {
                    throw new InvalidIDException('S');
                }
                break; 
            } catch (InvalidIDException e) {
                System.out.println(e);  
            }
        }
        return studentID;
    }

    private boolean isValidStudentID(String studentID) {
        Pattern pattern = Pattern.compile("^S[0-9]{4}$");
        Matcher matcher = pattern.matcher(studentID);
        return matcher.matches();
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
    
            
            Set<String> printedClasses = new HashSet<>();
    
            
            for (String[] periods : timetable.values()) {
                for (String period : periods) {
                    if (period != null && !period.isEmpty()) {
                        
                        String classId = period.split("/")[1]; 
    
                        
                        if (printedClasses.contains(classId)) {
                            continue;
                        }
    
                        
                        printedClasses.add(classId);
    
                        
                        ArrayList<String> students = datasets.studentClassAllotment.classStudentList.get(classId);
    
                        if (students != null && !students.isEmpty()) {
                            
                            Table studentTable = new Table();
                            studentTable.addColumn("Student Name");
    
                            
                            for (String student : students) {
                                studentTable.addRow(new String[]{student});
                            }
    
                            
                            System.out.println("Students in class " + classId + ":");
                            studentTable.print();
                        } else {
                            System.out.println("No students found for class " + classId + ".");
                        }
                    }
                }
            }
            /*Scanner scanner = new Scanner(System.in);
            System.out.print("Do you want to modify the timetable for " + teacherId + "? (yes/no): ");
            String modifyChoice = scanner.nextLine().trim().toLowerCase();
    
            if (modifyChoice.equals("yes")) {
                datasets.timeTableAllotment.createTimeTable(teacherId, maxPeriods);
            }*/
        } else {
            System.out.println("No timetable available for " + teacherId + ".");
        }
    }
    
    public static void main(String[] args) {
        TeacherPage teacherPage = new TeacherPage(Datasets.loadData("src/data/datasets.ser"), "T0001");
        teacherPage.displayMenu();
    }
}