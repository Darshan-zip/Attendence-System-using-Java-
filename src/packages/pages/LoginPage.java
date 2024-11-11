package packages.pages;

import java.io.Console;

import packages.exceptions.*;
import packages.datastructures.*;

public class LoginPage extends Page {
    private String userID = " ";
    private String password = " ";
    private String hiddenPassword = " ";

    public LoginPage(Datasets datasets){
        this.table = new Table();
        this.datasets = datasets;
    }

    public void displayMenu(){
        table.addColumn("Login Page");
        table.addRow("User ID : ");
        table.addRow("Password : ");
        boolean enteredDetails = false;

        while(!enteredDetails){
                table.print();

                getDetails();
                try{
                    verifyDetails();
                    enteredDetails = true;
                }
                catch(UserIDNotFoundException e){
                    System.out.println(e);
                    System.out.println("Please try again!\n");
                }
                catch(PasswordNotMatchingException e){
                    System.out.println(e);
                    System.out.println("Please try again!\n");
                }
        }

        switch(userID.charAt(0)){
            case 'A':
                System.out.println("Logging in Admin...");
                AdminPage adminPage = new AdminPage(datasets, userID);
                adminPage.displayMenu();
                break;
            case 'T':
                System.out.println("Logging in Teacher..");
                TeacherPage teacherPage = new TeacherPage(datasets, userID);
                teacherPage.displayMenu();
                break;
            case 'S':
                System.out.println("Logging in Student...");
                StudentPage studentPage = new StudentPage(datasets, userID);
                studentPage.displayMenu();
                break;
        }
    }

    private void getDetails(){
        Console console = System.console();
        /*if (console == null) {
            System.out.println("No console available. Please run in a command-line environment.");
            return;
        }*/
        userID = console.readLine("\nEnter your user ID: ");
        char[] passwordArray = console.readPassword("Enter your password: ");
        password = new String(passwordArray);
        hiddenPassword = new String(new char[password.length()]).replace('\0', '*');
        table.updateRow(0, "User ID : " + userID);
        table.updateRow(1, "Password : " + hiddenPassword);
        table.print();
    }

    private void verifyDetails() throws UserIDNotFoundException, PasswordNotMatchingException{
        if(!datasets.userLoginDetails.loginDetails.containsKey(userID)){
            throw new UserIDNotFoundException(userID);
        }
        if(!datasets.userLoginDetails.loginDetails.get(userID).equals(password)){
            throw new PasswordNotMatchingException();
        }
    }

    
}


