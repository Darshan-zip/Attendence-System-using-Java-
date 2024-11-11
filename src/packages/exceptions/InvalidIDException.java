package packages.exceptions;

public class InvalidIDException extends Exception{
    char type;
    public InvalidIDException(char type){
        this.type = type;
    }
    
    @Override
    public String toString() {
        return switch(type){
            case 'S' -> "Invalid Student ID: Student ID must be of the form \"[S][0-9]{4}\"";
            case 'T' -> "Invalid Teacher ID: Teacher ID must be of the form \"[T][0-9]{4}\"";
            case 'A' -> "Invalid Admin ID: Admin ID must be of the form \"[A][0-9]{4}\"";
            case 'P' -> "Invalid Teacher Period: It must be of the form \"[C][0-9]{4}/[H][0-9]{4}\"";
            case 'H' -> "Invalid Class ID: It must be of the form \"[H][0-9]{4}\"";
            case 'C' -> "Invalid Course ID: It must be of the form \"[C][0-9]{4}\"";
            default -> "Invalid ID";
        };
    }
}
