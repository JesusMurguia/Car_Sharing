package carsharing.Car;

import carsharing.Company.Company;

import java.util.List;

public interface CarDao {
    public List<Car> getAllCarsByCompanyId(int id);
    public void addCar(Car car);

    public Car getCarById(int id);

    public List<Car> getAllAvailableCarsByCompanyId(int id);
}
