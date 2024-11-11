package packages.datastructures.datasets;

import java.util.ArrayList;
import java.util.TreeMap;
import java.io.Serializable;

public class StudentClassAllotment implements Serializable {
    // classStudentList[classID][index] = studentID
	public TreeMap<String, ArrayList<String>> classStudentList = new TreeMap<>();
	// studentToClasses[studentID] = classID
	public TreeMap<String, String> studentToClasses = new TreeMap<>();
	public void generateClassStudentList() {
		int k = 1;

		// 5 classrooms
		for(int i=1 ; i<=5 ; i++) {
			ArrayList<String> studentList = new ArrayList<>();
			String classID = String.format("H%04d", i);

			// 10 students each
			for(int j=1 ; j<=10 ; j++) {
				String studentID = String.format("S%04d", k);
				studentList.add(studentID);
				studentToClasses.put(studentID,classID);
				k++;
			}
			classStudentList.put(classID,studentList);
		}
	}
	public void createClass(String classID) {
		ArrayList<String> studentList = new ArrayList<>();
		classStudentList.put(classID,studentList);
	}
	public void addStudentToClass(String classID, String studentID) {
		classStudentList.get(classID).add(studentID);
		studentToClasses.put(studentID, classID);
	}

}
