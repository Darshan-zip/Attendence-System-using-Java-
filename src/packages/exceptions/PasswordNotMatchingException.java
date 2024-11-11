package packages.exceptions;

public class PasswordNotMatchingException extends Exception{
    @Override
    public String toString() {
        return "Incorrect Password Entered!";
    }
}
