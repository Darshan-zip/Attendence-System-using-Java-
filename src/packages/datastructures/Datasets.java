
package packages.datastructures;

import java.io.Serializable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import packages.datastructures.datasets.*;

public class Datasets implements Serializable {

	private static final long serialVersionUID = 1L;
	public UserLoginDetails userLoginDetails = new UserLoginDetails();
	public StudentClassAllotment studentClassAllotment = new StudentClassAllotment();
	public TimeTableAllotment timeTableAllotment = new TimeTableAllotment();
	public AcademicCalendar academicCalendar = new AcademicCalendar();
	public StudentAttendance studentAttendance = new StudentAttendance(loadData("src/data/datasets.ser"));

	public Datasets() {
		userLoginDetails.generateUserLoginDetails();
		//studentAttendance.generateStudentAttendance();
		timeTableAllotment.generateTimeTable();
		studentClassAllotment.generateClassStudentList();
		academicCalendar.generateAcademicCalendar();
		studentAttendance.populateTeacherDailyAttendance();
	}

	public void saveData(String filename) {
		try (FileOutputStream fileOut = new FileOutputStream(filename);
			        ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
			out.writeObject(this); 
			System.out.println("Data has been serialized to " + filename);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Datasets loadData(String filename) {
		Datasets datasets = null;
		try (FileInputStream fileIn = new FileInputStream(filename);
			        ObjectInputStream in = new ObjectInputStream(fileIn)) {
			datasets = (Datasets) in.readObject();
			System.out.println("Data has been deserialized from " + filename);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return datasets;
	}

	public void printData() {
		System.out.println("User Login Details: " + userLoginDetails.loginDetails); System.out.println();
		System.out.println("Student to Classes: " + studentClassAllotment.studentToClasses); System.out.println(); 
		System.out.println("Class Student List: " + studentClassAllotment.classStudentList);System.out.println();
		System.out.println("Class Time Tables: " + timeTableAllotment.classTimeTables);System.out.println();
		System.out.println("Teacher Time Tables: " + timeTableAllotment.teacherTimeTables);System.out.println();
		academicCalendar.printCalendar();System.out.println();
		System.out.println("Teacher Daily Attendance: " + studentAttendance.teacherDailyAttendance);System.out.println();
		//System.out.println("Student Daily Attendance: " + studentAttendance.studentDailyAttendance);System.out.println();
		System.out.println("Teacher Course Attendance: " + studentAttendance.teacherCourseAttendance);System.out.println();
		//System.out.println("Student Course Attendance: " + studentAttendance.studentCourseAttendance);System.out.println();
	}

	public static void main(String[] args) {
		Datasets datasets = new Datasets();
		datasets.saveData("src/data/datasets.ser");

		Datasets loadedDatasets = loadData("src/data/datasets.ser");
		if (loadedDatasets != null) {
			loadedDatasets.printData();
		}
	}
}

