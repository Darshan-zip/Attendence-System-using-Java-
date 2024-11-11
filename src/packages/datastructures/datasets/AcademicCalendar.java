package packages.datastructures.datasets;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.Iterator;

import packages.datastructures.*;
import packages.exceptions.*;

public class AcademicCalendar implements Serializable {

    public TreeMap<String, String> academicCalendar = new TreeMap<>();

    private void setAcademicCalendar(String startDateStr, String endDateStr) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

        try {
            LocalDate startDate = LocalDate.parse(startDateStr, inputFormatter);
            LocalDate endDate = LocalDate.parse(endDateStr, inputFormatter);
            LocalDate currentDate = startDate;

            while (!currentDate.isAfter(endDate)) {
                String dayOfWeek = currentDate.getDayOfWeek().toString();
                if (!dayOfWeek.equals("SUNDAY")) {
                    academicCalendar.put(currentDate.format(outputFormatter), dayOfWeek);
                }
                currentDate = currentDate.plusDays(1);
            }
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use dd/MM/yyyy.");
        }
    }

    private void setSaturdayTT(String saturdayDateStr, String replacementDay) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate saturdayDate;

        try {
            saturdayDate = LocalDate.parse(saturdayDateStr, inputFormatter);
            String formattedSaturday = saturdayDate.format(outputFormatter);

            if (academicCalendar.containsKey(formattedSaturday)) {
                //if (replacementDay.equals("HOLIDAY")) {
                //    academicCalendar.remove(formattedSaturday);
                //} else {
                academicCalendar.put(formattedSaturday, replacementDay);
                //}
            }
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use dd/MM/yyyy.");
        }
    }

    private void removeHoliday(String holidayDateStr) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

        try {
            LocalDate holidayDate = LocalDate.parse(holidayDateStr, inputFormatter);
            String formattedHoliday = holidayDate.format(outputFormatter);
            academicCalendar.remove(formattedHoliday);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use dd/MM/yyyy.");
        }
    }

    public void printCalendar() {
        if (academicCalendar.isEmpty()) {
            System.out.println("The calendar is empty.");
        } else {
            System.out.println("Current Academic Calendar:");
            Table table = new Table();
            table.addColumn("Working Dates");
            table.addColumn("Day's Time Table");
            //academicCalendar.forEach((date, day) -> System.out.printf("%s\t%s%n", date, day));
            academicCalendar.forEach((date, day) -> table.addRow(date,day));
            table.print();
        }
    }

    @SuppressWarnings("resource")
    public void createAcademicCalendar() {
        Scanner scanner = new Scanner(System.in);
        academicCalendar.clear();

        System.out.print("Enter start date (dd/MM/yyyy): ");
        String startDateStr = scanner.nextLine();
        System.out.print("Enter end date (dd/MM/yyyy): ");
        String endDateStr = scanner.nextLine();

        setAcademicCalendar(startDateStr, endDateStr);
        //printCalendar();

        LocalDate startDate = LocalDate.parse(startDateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        LocalDate endDate = LocalDate.parse(endDateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        
        while (true) {
            System.out.print("Enter a holiday date to remove (dd/MM/yyyy), or type 'done' to finish: ");
            String holidayDateStr = scanner.nextLine();
            if (holidayDateStr.equalsIgnoreCase("done")) {
                break;
            }

            try {
                LocalDate holidayDate = LocalDate.parse(holidayDateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                if (!holidayDate.isBefore(startDate) && !holidayDate.isAfter(endDate)) {
                    removeHoliday(holidayDateStr);
                } else {
                    throw new InvalidDateException('R');
                }
            } catch (InvalidDateException e) {
                System.out.println(e.getMessage());
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use dd/MM/yyyy.");
            }
        }

        while (true) {
            System.out.print("Enter a Working Saturday Date (dd/MM/yyyy), or type 'done' to finish: ");
            String saturdayDateStr = scanner.nextLine();
            
            if (saturdayDateStr.equalsIgnoreCase("done")) {
                removeAllHolidaySaturdays();
                break;
            }

            LocalDate saturdayDate;
            try {
                saturdayDate = LocalDate.parse(saturdayDateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                
                if (saturdayDate.getDayOfWeek().toString().equals("SATURDAY")) {

                    if (!saturdayDate.isBefore(startDate) && !saturdayDate.isAfter(endDate)) {
                        System.out.print("Enter the Day's Time Table to be followed on that day: ");
                        String replacementDay = scanner.nextLine();

                        setSaturdayTT(saturdayDateStr, replacementDay);
                        printCalendar();
                    } else {
                        throw new InvalidDateException('R');
                    }
                } else {
                    throw new InvalidDateException('S');
                }
            } catch (InvalidDateException e) {
                System.out.println(e.getMessage());
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use dd/MM/yyyy.");
            }
        }
    }

    private void removeAllHolidaySaturdays() {
        Iterator<Map.Entry<String, String>> iterator = academicCalendar.entrySet().iterator();
    
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            if (entry.getValue().equalsIgnoreCase("SATURDAY")) {
                iterator.remove();  
            }
        }
    }

    public void generateAcademicCalendar(){
        /*
        setAcademicCalendar("01/10/2024", "15/10/2024");
        // Handle working Saturdays and holidays
        setSaturdayTT("05/10/2024", "MONDAY");
        setSaturdayTT("12/10/2024", "HOLIDAY");
        removeHoliday("11/10/2024");
        */

        setAcademicCalendar("01/10/2024", "15/10/2024");
        removeHoliday("02/10/2024");
        setSaturdayTT("", "WEDNESDAY");
        removeAllHolidaySaturdays();
    }

    public static void main(String[] args) {

        AcademicCalendar calendar = new AcademicCalendar();
        /*
        // Example usage
        calendar.setAcademicCalendar("01/10/2024", "15/10/2024");
        calendar.printCalendar();

        // Handle working Saturdays and holidays
        calendar.setSaturdayTT("05/10/2024", "MONDAY");
        calendar.setSaturdayTT("12/10/2024", "HOLIDAY");
        calendar.removeHoliday("11/10/2024");
        */

        calendar.generateAcademicCalendar();
        //calendar.createAcademicCalendar();
        calendar.printCalendar();
    }
}