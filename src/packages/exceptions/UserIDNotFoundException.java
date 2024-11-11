
package packages.exceptions;

public class UserIDNotFoundException extends Exception{
    private final String userID;
    public UserIDNotFoundException(String userID){
        this.userID = userID;
    }

    @Override
    public String toString() {
        return "UserID \"" + userID + "\" does not exist!";
    }
}



