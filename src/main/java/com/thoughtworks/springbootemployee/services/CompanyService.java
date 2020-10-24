package com.thoughtworks.springbootemployee.services;

import com.thoughtworks.springbootemployee.exceptions.CompanyNotFoundException;
import com.thoughtworks.springbootemployee.exceptions.InvalidCompanyException;
import com.thoughtworks.springbootemployee.models.Company;
import com.thoughtworks.springbootemployee.models.Employee;
import com.thoughtworks.springbootemployee.repository.ICompanyRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.isNull;

@Service
public class CompanyService {
    private ICompanyRepository companyRepository;

    public CompanyService(ICompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public List<Company> getAll() {
        return companyRepository.findAll();
    }

    public Company create(Company newCompany) {
        validateCompany(newCompany);
        return companyRepository.save(newCompany);
    }

    public Company searchById(Integer id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException("Company with id:" + id + " not found"));
    }

    public List<Employee> getEmployeesByCompanyId(Integer id) {
        Company company = searchById(id);
        return company.getEmployees();
    }

    public Company update(Integer id, Company updatedCompany) {
        Company company = searchById(id);
        company.setCompanyName(updatedCompany.getCompanyName());
        return companyRepository.save(company);
    }

    public void delete(Integer id) {
        companyRepository.delete(searchById(id));
    }

    public List<Company> getCompaniesByPageAndPageSize(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        return companyRepository.findAll(pageable).toList();
    }

    private void validateCompany(Company company) {
        if (isNull(company.getCompanyName())) {
            throw new InvalidCompanyException("Company given has null fields!");
        }
    }
}
