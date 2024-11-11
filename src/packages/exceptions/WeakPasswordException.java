package packages.exceptions;

public class WeakPasswordException extends Exception {
    public String toString() {
        return "Password should contain atleast 8 characters with atleast one digit and a special character!";
    }
}
