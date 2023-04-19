package carsharing.Company;

import carsharing.Company.Company;

import java.util.List;

public interface CompanyDao {
    public List<Company> getAllCompanies();
    public void addCompany(Company company);

    public Company getCompanyById(int id);
}
