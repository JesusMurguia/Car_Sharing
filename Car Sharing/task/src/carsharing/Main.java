package carsharing;

import carsharing.Car.CarDao;
import carsharing.Car.CarDaoImpl;
import carsharing.Company.CompanyDao;
import carsharing.Company.CompanyDaoImpl;
import carsharing.Customer.CustomerDao;
import carsharing.Customer.CustomerDaoImpl;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        if(args.length > 1 && args[0].equals("-databaseFileName")){
            DatabaseUtil.setDatabaseFileName(args[1]);
            DatabaseUtil.setUpTables();
        }

        CarDao carDao = new CarDaoImpl();
        CompanyDao companyDao = new CompanyDaoImpl();
        CustomerDao customerDao= new CustomerDaoImpl();

        new Menu( new Scanner(System.in), companyDao, carDao, customerDao).mainMenu();
    }
}