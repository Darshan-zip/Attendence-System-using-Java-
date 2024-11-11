package packages.exceptions;

public class UserIDAlreadyExistsException extends Exception{
    private final String userID;
    public UserIDAlreadyExistsException(String userID){
        this.userID = userID;
    }

    @Override
    public String toString() {
        return "UserID \"" + userID + "\" already exists!";
    }
}
