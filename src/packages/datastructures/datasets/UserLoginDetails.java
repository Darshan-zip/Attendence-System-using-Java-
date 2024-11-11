package packages.datastructures.datasets;
import java.util.TreeMap;
import java.io.Serializable;

public class UserLoginDetails implements Serializable {
    // userLoginDetails[userID] = password
	public TreeMap<String, String> loginDetails = new TreeMap<>();
	public void generateUserLoginDetails() {
		// 1 Admin
		loginDetails.put("A1000","passwordA1");

		// 50 Students
		for (int i = 1; i <= 50; i++) {
			loginDetails.put(
			    String.format("S%04d", i),
			    "passwordS" + i
			);
		}

		// 5 Teachers
		for(int i=1 ; i<=5 ; i++) {
			loginDetails.put(
			    String.format("T%04d", i),
			    "passwordT"+i
			);
		}
	}
	public void addUserLoginDetails(String userID, String password) {
		loginDetails.put(userID, password);
	}
	
}
