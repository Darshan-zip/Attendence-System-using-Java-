package packages.exceptions;

public class InvalidDateException extends Exception {
    char type;
    public InvalidDateException(char type){
        this.type = type;
    }
    
    @Override
    public String toString() {
        return switch(type){
            case 'S' -> "This date is not a Saturday!";
            case 'R' -> "Date is not between start and end date!";
            case 'A' -> "Date is not in academic calendar";
            default -> "Invalid Date";
        };
    }
}
