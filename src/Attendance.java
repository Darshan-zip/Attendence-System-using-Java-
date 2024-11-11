import packages.pages.*;
import packages.datastructures.*;

public class Attendance{
    public static void main(String args[]){
        Datasets datasets = Datasets.loadData("src/data/datasets.ser");
        datasets.printData();
        HomePage MainApp = new HomePage(datasets);
        MainApp.displayMenu();

    }
}

