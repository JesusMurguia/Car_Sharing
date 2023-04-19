package carsharing.Car;

import carsharing.Company.Company;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static carsharing.DatabaseUtil.closeConnection;
import static carsharing.DatabaseUtil.getConnection;

public class CarDaoImpl implements CarDao{
    @Override
    public List<Car> getAllCarsByCompanyId(int id) {
        List<Car> cars = new ArrayList<>();
        String query = "SELECT " +
                "CAR.ID, " +
                "CAR.NAME, " +
                "CAR.COMPANY_ID " +
                "FROM CAR " +
                "INNER JOIN COMPANY " +
                "ON COMPANY.ID = CAR.COMPANY_ID " +
                "AND CAR.COMPANY_ID = ?; ";

        try(PreparedStatement statement = getConnection().prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();

            while(rs.next()){
                cars.add(new Car(rs.getInt("ID"), rs.getString("NAME"), rs.getInt("COMPANY_ID")));
            }

            rs.close();
            closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return cars;
    }

    @Override
    public List<Car> getAllAvailableCarsByCompanyId(int id) {
        List<Car> cars = new ArrayList<>();
        String query = "SELECT " +
                "CAR.ID, " +
                "CAR.NAME, " +
                "CAR.COMPANY_ID " +
                "FROM CAR " +
                "LEFT JOIN CUSTOMER " +
                "ON CUSTOMER.RENTED_CAR_ID = CAR.ID " +
                "INNER JOIN COMPANY " +
                "ON COMPANY.ID = CAR.COMPANY_ID " +
                "AND CAR.COMPANY_ID = ? " +
                "AND CUSTOMER.RENTED_CAR_ID IS NULL;";

        try(PreparedStatement statement = getConnection().prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();

            while(rs.next()){
                cars.add(new Car(rs.getInt("ID"), rs.getString("NAME"), rs.getInt("COMPANY_ID")));
            }

            rs.close();
            closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return cars;
    }
    @Override
    public void addCar(Car car) {
        String query = "INSERT INTO CAR(NAME, COMPANY_ID) VALUES(?,?)";

        try(PreparedStatement statement = getConnection().prepareStatement(query)) {
            statement.setString(1, car.getName());
            statement.setInt(2, car.getCompanyId());
            statement.executeUpdate();
            closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Car getCarById(int id) {
            Car car = null;
            String query = "SELECT " +
                    "ID, " +
                    "NAME, " +
                    "COMPANY_ID " +
                    "FROM CAR " +
                    "WHERE ID = ?";

            try(PreparedStatement statement = getConnection().prepareStatement(query)) {
                statement.setInt(1, id);
                ResultSet rs = statement.executeQuery();

                if(rs.next()){
                    car = new Car(rs.getInt("ID"), rs.getString("NAME"), rs.getInt("COMPANY_ID"));
                }

                rs.close();
                closeConnection();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            return car;
    }
}
