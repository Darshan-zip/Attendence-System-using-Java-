package packages.datastructures.datasets;

import java.util.TreeMap;

import packages.exceptions.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.Serializable;

public class TimeTableAllotment implements Serializable {
    // classTimeTables[classID][Day][PeriodNo] = "courseID/teacherID"
	public TreeMap<String, LinkedHashMap<String, String[]>> classTimeTables = new TreeMap<>();
	// teacherTimeTables[userID][Day][PeriodNo] = "courseID/classID"
	public TreeMap<String, LinkedHashMap<String, String[]>> teacherTimeTables = new TreeMap<>();
	
    public void generateTimeTable() {
		// 5 Teachers
        LinkedHashMap<String, String[]> timeTable = new LinkedHashMap<>();
		timeTable.put("MONDAY", new String[]{"C0001/H0001", "C0002/H0002", null, null, null});
        timeTable.put("TUESDAY", new String[]{"C0001/H0001", "C0003/H0003", null, null, null});
        timeTable.put("WEDNESDAY", new String[]{"C0002/H0002", "C0004/H0004", null, null, null});
        timeTable.put("THURSDAY", new String[]{"C0003/H0003", "C0005/H0005", null, null, null});
        timeTable.put("FRIDAY", new String[]{"C0004/H0004", "C0005/H0005", null, null, null});
        teacherTimeTables.put(String.format("T%04d", 1), timeTable);

        timeTable = new LinkedHashMap<>();
        timeTable.put("MONDAY", new String[]{"C0003/H0003", "C0004/H0004", null, null, null});
        timeTable.put("TUESDAY", new String[]{"C0002/H0002", "C0005/H0005", null, null, null});
        timeTable.put("WEDNESDAY", new String[]{"C0001/H0001", "C0005/H0005", null, null, null});
        timeTable.put("THURSDAY", new String[]{"C0001/H0001", "C0002/H0002", null, null, null});
        timeTable.put("FRIDAY", new String[]{"C0003/H0003", "C0004/H0004", null, null, null});
        teacherTimeTables.put(String.format("T%04d", 2), timeTable);

        timeTable = new LinkedHashMap<>();
        timeTable.put("MONDAY", new String[]{"C0005/H0005", "C0001/H0001", null, null, null});
        timeTable.put("TUESDAY", new String[]{"C0004/H0004", "C0002/H0002", null, null, null});
        timeTable.put("WEDNESDAY", new String[]{"C0003/H0003", "C0001/H0001", null, null, null});
        timeTable.put("THURSDAY", new String[]{"C0002/H0002", "C0005/H0005", null, null, null});
        timeTable.put("FRIDAY", new String[]{"C0004/H0004", "C0003/H0003", null, null, null});
        teacherTimeTables.put(String.format("T%04d", 3), timeTable);

        timeTable = new LinkedHashMap<>();
        timeTable.put("MONDAY", new String[]{"C0002/H0002", "C0003/H0003", null, null, null});
        timeTable.put("TUESDAY", new String[]{"C0004/H0004", "C0005/H0005", null, null, null});
        timeTable.put("WEDNESDAY", new String[]{"C0001/H0001", "C0004/H0004", null, null, null});
        timeTable.put("THURSDAY", new String[]{"C0005/H0005", "C0001/H0001", null, null, null});
        timeTable.put("FRIDAY", new String[]{"C0002/H0002", "C0003/H0003", null, null, null});
        teacherTimeTables.put(String.format("T%04d", 4), timeTable);

        timeTable = new LinkedHashMap<>();
        timeTable.put("MONDAY", new String[]{"C0004/H0004", "C0005/H0005", null, null, null});
        timeTable.put("TUESDAY", new String[]{"C0001/H0001", "C0003/H0003", null, null, null});
        timeTable.put("WEDNESDAY", new String[]{"C0002/H0002", "C0001/H0001", null, null, null});
        timeTable.put("THURSDAY", new String[]{"C0004/H0004", "C0002/H0002", null, null, null});
        timeTable.put("FRIDAY", new String[]{"C0005/H0005", "C0003/H0003", null, null, null});
        teacherTimeTables.put(String.format("T%04d", 5), timeTable);
        timeTable = new LinkedHashMap<>();

		inverseTimeTable();
	}
	
    @SuppressWarnings("resource")
	public void createTimeTable(String teacherId, int maxPeriods){
        Scanner scanner = new Scanner(System.in);
        LinkedHashMap<String, String[]> timetable = new LinkedHashMap<>();
        String[] days = {"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"};

        for (String day : days) {
            System.out.println(day + ":");
            String[] periods = new String[maxPeriods];

            for (int i = 0; i < maxPeriods; i++) {
                boolean enteredChoice = false;
                while(!enteredChoice){
                    System.out.print("Period " + (i + 1) + ": ");
                    String periodDetail = scanner.nextLine().trim();
                    try{
                        isValidPeriod(periodDetail);
                        enteredChoice = true;
                    }
                    catch(InvalidIDException e){
                        System.out.println(e);
                    }
                    periods[i] = periodDetail.isEmpty() ? null : periodDetail; 
                }
                
            }

            timetable.put(day, periods); 
        }

        teacherTimeTables.put(teacherId, timetable);
        System.out.println("Timetable for " + teacherId + " has been updated.");
        inverseTimeTable();
    }

    private void isValidPeriod(String period) throws InvalidIDException{
        if(period.matches("C\\d{4}/H\\d{4}") || period.isEmpty()){
            return;
        }
        throw new InvalidIDException('P');
    }

	public void inverseTimeTable() {
        for (Map.Entry<String, LinkedHashMap<String, String[]>> teacherEntry : teacherTimeTables.entrySet()) {
            String teacherId = teacherEntry.getKey();
            LinkedHashMap<String, String[]> teacherTimetable = teacherEntry.getValue();
            //System.out.println("calculating inverse");
            for (Map.Entry<String, String[]> dayEntry : teacherTimetable.entrySet()) {
                String day = dayEntry.getKey();
                String[] periods = dayEntry.getValue();
    
                
                for (int periodNo = 0; periodNo < periods.length; periodNo++) {
                    String periodDetail = periods[periodNo];
                    if (periodDetail != null) {
                        String[] parts = periodDetail.split("/");
                        if (parts.length == 2) {
                            String courseId = parts[0]; 
                            String classId = parts[1]; 
    
                            classTimeTables.putIfAbsent(classId, new LinkedHashMap<>());
                            LinkedHashMap<String, String[]> classTimetable = classTimeTables.get(classId);
                            classTimetable.putIfAbsent(day, new String[periods.length]);
                            classTimetable.get(day)[periodNo] = courseId + "/" + teacherId; // Format: "courseID/teacherID"
                        } else {
                            System.out.println("Invalid period detail: " + periodDetail);
                        }
                    }
                }
            }
        }
    }
}
