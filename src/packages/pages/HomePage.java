package packages.pages;

import java.util.Scanner;

import packages.datastructures.*;

@SuppressWarnings("resource")
public class HomePage extends Page {

    public HomePage(Datasets datasets){
        this.table = new Table();
        this.datasets = datasets;
    }

    public void displayMenu(){
        Scanner input = new Scanner(System.in);
        boolean endMenu = false;
        table.addColumn("Digital Attendance Tracking Application");
        table.addRow("Welcome to our application!");
        table.addRow("What would you like to do?");
        table.addRow(" ");
        table.addRow("1. Login");
        table.addRow("2. Exit");

        while(!endMenu){
            table.print();
            boolean gotChoice = false;

            while(!gotChoice){
                System.out.print("\nEnter choice : ");
                int choice = Integer.parseInt(input.nextLine());

                switch(choice){
                    case 1:
                        datasets.studentAttendance.populateTeacherDailyAttendance();
                        gotChoice = true;
                        LoginPage loginPage = new LoginPage(datasets);
                        loginPage.displayMenu();
                        break;
                    case 2:
                        gotChoice = true;
                        endMenu = true;
                        break;
                    default:
                        System.out.println("Invalid Input, enter again!");
                }
            }
        }
        //input.close();
    }

}

