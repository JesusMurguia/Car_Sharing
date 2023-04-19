package carsharing.Customer;

import carsharing.Car.Car;

import java.util.List;

public interface CustomerDao {
    public void addCustomer(Customer customer);

    public List<Customer> getAllCustomers();

    public void assignCarToCustomer(Customer customer, Car car);

    public Customer getCustomerById(int id);

}
