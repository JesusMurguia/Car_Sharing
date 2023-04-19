package carsharing.Customer;

import carsharing.Car.Car;
import carsharing.Company.Company;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static carsharing.DatabaseUtil.closeConnection;
import static carsharing.DatabaseUtil.getConnection;

public class CustomerDaoImpl implements CustomerDao{

    @Override
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String query = "SELECT ID, NAME, RENTED_CAR_ID FROM CUSTOMER";

        try(Statement statement = getConnection().createStatement()) {
            ResultSet rs = statement.executeQuery(query);

            while(rs.next()){
                customers.add(new Customer(rs.getInt("ID"), rs.getString("NAME"),(Integer) rs.getObject("RENTED_CAR_ID")));
            }

            rs.close();
            closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return customers;
    }

    @Override
    public void assignCarToCustomer(Customer customer, Car car) {
        String query = "UPDATE CUSTOMER SET RENTED_CAR_ID = ? WHERE ID = ?";

        try(PreparedStatement statement = getConnection().prepareStatement(query)) {
            if(car == null){
                statement.setNull(1, JDBCType.INTEGER.getVendorTypeNumber());
            }else{
                statement.setInt(1, car.getId());
            }
            statement.setInt(2,customer.getId());
            statement.executeUpdate();
            closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addCustomer(Customer customer) {
        String query = "INSERT INTO CUSTOMER(NAME) VALUES(?)";

        try(PreparedStatement statement = getConnection().prepareStatement(query)) {
            statement.setString(1, customer.getName());
            statement.executeUpdate();
            closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Customer getCustomerById(int id){
        Customer customer = null;
        String query = "SELECT " +
                "ID, " +
                "NAME, " +
                "RENTED_CAR_ID " +
                "FROM CUSTOMER " +
                "WHERE ID = ?";

        try(PreparedStatement statement = getConnection().prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();

            if(rs.next()){
                customer = new Customer(rs.getInt("ID"), rs.getString("NAME"), (Integer) rs.getObject("RENTED_CAR_ID"));
            }

            rs.close();
            closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return customer;
    }



}
