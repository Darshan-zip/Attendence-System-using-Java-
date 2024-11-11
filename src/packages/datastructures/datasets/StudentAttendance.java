package packages.datastructures.datasets;
import java.util.*;
import java.io.Serializable;

import packages.datastructures.Datasets;



public class StudentAttendance implements Serializable {
    Datasets datasets;
    public Map<String, Map<String, Map<String, Map<String, Boolean>>>> teacherDailyAttendance = new TreeMap<>(); // teacherID -> date -> teacherPeriod -> studentID -> attendance

    public Map<String, Map<String, Map<String, Integer>>> teacherCourseAttendance = new TreeMap<>(); // teacherID -> courseID -> studentID -> attendance count

    
    public StudentAttendance(Datasets datasets) {
        this.datasets = datasets;
    }

    public void populateTeacherDailyAttendance() {
        this.datasets=Datasets.loadData("src/data/datasets.ser");
        for (String teacherID : datasets.userLoginDetails.loginDetails.keySet()) {
            if (teacherID.startsWith("S") || teacherID.startsWith("A")) {
                continue;
            }
            //System.out.println(teacherID);

            
            Map<String, Map<String, Map<String, Boolean>>> dateAttendanceMap = new TreeMap<>();

            
            for (String date : datasets.academicCalendar.academicCalendar.keySet()) {
                Map<String, Map<String, Boolean>> periodAttendanceMap = new TreeMap<>();
                String day=datasets.academicCalendar.academicCalendar.get(date);
                if(day.equals("HOLIDAY")){
                    continue;
                }
                List<String> teacherPeriods = getNonNullPeriodsForDate(teacherID,day);

                
                for (String teacherPeriod : teacherPeriods) {
                    //System.out.println(teacherPeriod);
                    String classCode = parseClassFromPeriod(teacherPeriod);

                    
                    List<String> students = datasets.studentClassAllotment.classStudentList.get(classCode);
                    Map<String, Boolean> studentAttendanceMap = new TreeMap<>();

                    for (String studentID : students) {
                        //studentAttendanceMap.put(studentID, null);
                        //studentAttendanceMap.getOrDefault(studentID, null);
                        //Boolean value = studentAttendanceMap.get(studentID);
                        studentAttendanceMap.putIfAbsent(studentID, null);
                        //System.out.println(studentAttendanceMap.get(studentID));
                        //studentAttendanceMap.put(studentID, null);
                    }

                    periodAttendanceMap.put(teacherPeriod, studentAttendanceMap);

                }
                dateAttendanceMap.put(date, periodAttendanceMap);
            }
            teacherDailyAttendance.put(teacherID, dateAttendanceMap);
        }
    }

    
    private List<String> getNonNullPeriodsForDate(String teacherID, String day) {
        
        List<String> arrayList = new ArrayList<>(Arrays.asList(datasets.timeTableAllotment.teacherTimeTables.get(teacherID).get(day)));
        arrayList.removeIf(Objects::isNull); 
        return arrayList;
    }

    private String parseClassFromPeriod(String teacherPeriod) {
        return teacherPeriod.split("/")[1];
    }

    public static void main(String[] args) {
        Datasets datasets = Datasets.loadData("src/data/datasets.ser");
        StudentAttendance attendance = new StudentAttendance(datasets);
        attendance.populateTeacherDailyAttendance();
        System.out.println(attendance.teacherDailyAttendance.get("T0006"));
        //attendance.populateTeacherCourseAttendance();
        //System.out.println(attendance.teacherCourseAttendance.get("T0001"));
    }

}

