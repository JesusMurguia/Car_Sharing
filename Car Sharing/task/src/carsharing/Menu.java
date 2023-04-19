package carsharing;

import carsharing.Car.Car;
import carsharing.Car.CarDao;
import carsharing.Company.Company;
import carsharing.Company.CompanyDao;
import carsharing.Customer.Customer;
import carsharing.Customer.CustomerDao;

import java.util.List;
import java.util.Scanner;

public class Menu {
    Scanner scanner;
    CompanyDao companyDao;
    CarDao carDao;

    CustomerDao customerDao;

    public Menu(Scanner scanner, CompanyDao companyController, CarDao carController, CustomerDao customerDao) {
        this.scanner = scanner;
        this.companyDao = companyController;
        this.carDao = carController;
        this.customerDao = customerDao;
    }

    public void mainMenu(){
        System.out.println("1. Log in as a manager");
        System.out.println("2. Log in as a customer");
        System.out.println("3. Create a customer");
        System.out.println("0. Exit");

        while(scanner.hasNextLine()){
            String command = scanner.nextLine();

            switch (command){
                case "1" -> managerMenu();
                case "2" -> handleShowCustomerList();
                case "3" -> handleCreateCustomer();
                case "0" -> System.exit(0);
            }
        }
    }

    private void managerMenu() {

        String command = "";

        while(!"0".equals(command)){
            System.out.println("1. Company list");
            System.out.println("2. Create a company");
            System.out.println("0. Back");

            command = scanner.nextLine();

            switch (command){
                case "1"-> handleShowCompanyList();
                case "2" -> handleCreateCompany();
                case "0" -> mainMenu();
            }
        }

    }

    private void handleCreateCompany() {
        System.out.println("Enter the company name:");

        String name = scanner.nextLine();

        companyDao.addCompany(new Company(0,name));
        System.out.println("The company was created!");
        System.out.println();
    }

    private void handleShowCompanyList(){
        List<Company> companies = companyDao.getAllCompanies();

        if(companies.size() == 0){
            System.out.println("The company list is empty!");
            System.out.println();
            return;
        }

        int index = -1;

        while(index != 0) {
            System.out.println("Choose the company:");
            companies.forEach(System.out::println);
            System.out.println("0. Back");

            index = scanner.nextInt();
            scanner.nextLine();

            if (index <= companies.size() && index > 0) {
                companyMenu(companies.get(index - 1));
            }
        }
    }

    private void companyMenu(Company company){
        String command = "";


        System.out.println("'" +company.getName()+ "' company");

        while(!command.equals("0")){
            System.out.println("1. Car list");
            System.out.println("2. Create a car");
            System.out.println("0. Back");

            command = scanner.nextLine();

            switch (command){
                case "1" -> handleShowCarMenu(company);
                case "2" -> handleCreateCar(company);
                case "0" -> managerMenu();
            }
        }
    }

    private void handleCreateCar(Company company) {
        System.out.println("Enter the car name:");

        String name = scanner.nextLine();

        carDao.addCar(new Car(0,name, company.getId()));
        System.out.println("The car was added!");
        System.out.println();
    }

    private void handleShowCarMenu(Company company){
        List<Car> cars = carDao.getAllCarsByCompanyId(company.getId());

        if(cars.size() == 0){
            System.out.println("The car list is empty!");
            System.out.println();
        } else{
            for(int i = 0; i < cars.size(); i++){
                System.out.println(i+1+". "+cars.get(i).getName());
            }
            System.out.println();
        }
    }

    private void handleCreateCustomer() {
        System.out.println("Enter the customer name:");

        String name = scanner.nextLine();

        customerDao.addCustomer(new Customer(name));
        System.out.println("The customer was added!");
        System.out.println();
        mainMenu();
    }

    private void handleShowCustomerList(){
        List<Customer> customers = customerDao.getAllCustomers();

        if(customers.size() == 0){
            System.out.println("The customer list is empty!");
            System.out.println();
            return;
        }

        System.out.println("Customer list:");
        customers.forEach(System.out::println);
        System.out.println("0. Back");

        int index = scanner.nextInt();
        scanner.nextLine();

        if(index == 0) {
            mainMenu();
        }

        if (index <= customers.size() && index > 0) {
            customerMenu(customers.get(index - 1));
        }
    }

    private void customerMenu(Customer customer){
        String command = "";


        while(!command.equals("0")){
            System.out.println("1. Rent a car");
            System.out.println("2. Return a rented car");
            System.out.println("3. My rented car");
            System.out.println("0. Back");

            command = scanner.nextLine();

            switch (command){
                case "1" -> handleRentACar(customer.getId());
                case "2" -> handleReturnCar(customer.getId());
                case "3" -> handleShowRentedCar(customer.getId());
                case "0" -> mainMenu();
            }
        }
    }

    private void handleReturnCar(int id) {
        Customer customer = customerDao.getCustomerById(id);
        if(customer.getRentedCarId() == null){
            System.out.println("You didn't rent a car!");
            System.out.println();
            return;
        }
        customerDao.assignCarToCustomer(customer, null);
        System.out.println("You've returned a rented car!");
    }

    private void handleShowRentedCar(int id) {
        Customer customer = customerDao.getCustomerById(id);
        if(customer.getRentedCarId() == null){
            System.out.println("You didn't rent a car!");
            System.out.println();
            return;
        }

        Car rentedCar = carDao.getCarById(customer.getRentedCarId());
        Company company = companyDao.getCompanyById(rentedCar.getCompanyId());

        System.out.println("Your rented car:");
        System.out.println(rentedCar.getName());
        System.out.println("Company:");
        System.out.println(company.getName());
        System.out.println();
    }

    private void handleRentACar(int id){
        Customer customer = customerDao.getCustomerById(id);

        if(customer.getRentedCarId() != null){
            System.out.println("You've already rented a car!");
            System.out.println();
            customerMenu(customer);
        }
        List<Company> companies = companyDao.getAllCompanies();

        if(companies.size() == 0){
            System.out.println("The company list is empty!");
            System.out.println();
            customerMenu(customer);
        }

        int index = -1;

        System.out.println("Choose the company:");
        companies.forEach(System.out::println);
        System.out.println("0. Back");

        index = scanner.nextInt();
        scanner.nextLine();

        if (index <= companies.size() && index > 0) {
            List<Car> cars = carDao.getAllAvailableCarsByCompanyId(index);

            if(cars.size() == 0){
                System.out.println("The car list is empty!");
                System.out.println();
            } else{
                System.out.println("Choose a car:");
                for(int i = 0; i < cars.size(); i++){
                    System.out.println(i+1+". "+cars.get(i).getName());
                }
                System.out.println();

                index = scanner.nextInt();
                scanner.nextLine();
                if(index <= cars.size() && index > 0){
                    customerDao.assignCarToCustomer(customer, cars.get(index-1));
                    System.out.println("You rented '"+cars.get(index-1).getName()+"'");
                    System.out.println();
                    customerMenu(customer);
                }
            }
        }
    }

}
