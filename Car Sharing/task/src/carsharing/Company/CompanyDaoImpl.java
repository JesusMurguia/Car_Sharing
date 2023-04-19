package carsharing.Company;

import carsharing.Car.Car;
import carsharing.Company.Company;
import carsharing.Company.CompanyDao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static carsharing.DatabaseUtil.closeConnection;
import static carsharing.DatabaseUtil.getConnection;

public class CompanyDaoImpl implements CompanyDao {
    @Override
    public List<Company> getAllCompanies() {
        List<Company> companies = new ArrayList<>();
        String query = "SELECT ID, NAME FROM COMPANY";

        try(Statement statement = getConnection().createStatement()) {
            ResultSet rs = statement.executeQuery(query);

            while(rs.next()){
                companies.add(new Company(rs.getInt("ID"), rs.getString("NAME")));
            }

            rs.close();
            closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return companies;
    }

    @Override
    public void addCompany(Company company) {
        String query = "INSERT INTO COMPANY(NAME) VALUES(?)";

        try(PreparedStatement statement = getConnection().prepareStatement(query)) {
            statement.setString(1, company.getName());
            statement.executeUpdate();
            closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Company getCompanyById(int id) {
        Company company = null;
        String query = "SELECT " +
                "ID, " +
                "NAME, " +
                "FROM COMPANY " +
                "WHERE ID = ?";

        try(PreparedStatement statement = getConnection().prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();

            if(rs.next()){
                company = new Company(rs.getInt("ID"), rs.getString("NAME"));
            }

            rs.close();
            closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return company;
    }
}
