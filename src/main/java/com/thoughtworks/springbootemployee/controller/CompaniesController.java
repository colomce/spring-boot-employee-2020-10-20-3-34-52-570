package com.thoughtworks.springbootemployee.controller;

import com.thoughtworks.springbootemployee.dto.CompanyRequest;
import com.thoughtworks.springbootemployee.dto.CompanyResponse;
import com.thoughtworks.springbootemployee.dto.EmployeeResponse;
import com.thoughtworks.springbootemployee.mapper.CompanyMapper;
import com.thoughtworks.springbootemployee.mapper.EmployeeMapper;
import com.thoughtworks.springbootemployee.models.Company;
import com.thoughtworks.springbootemployee.models.Employee;
import com.thoughtworks.springbootemployee.services.CompanyService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompaniesController {

    private final CompanyMapper companyMapper;
    private final EmployeeMapper employeeMapper;
    private final CompanyService companyService;

    public CompaniesController(CompanyMapper companyMapper, EmployeeMapper employeeMapper, CompanyService companyService) {
        this.companyMapper = companyMapper;
        this.employeeMapper = employeeMapper;
        this.companyService = companyService;
    }

    @GetMapping
    public List<CompanyResponse> getAll() {
        return companyMapper.toResponseList(companyService.getAll());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompanyResponse create(@RequestBody CompanyRequest companyRequest) {
        Company company = companyMapper.toEntity(companyRequest);
        Company createdCompany = companyService.create(company);
        return companyMapper.toResponse(createdCompany);
    }

    @GetMapping("/{companyId}")
    public CompanyResponse searchById(@PathVariable("companyId") Integer companyId) {
        return companyMapper.toResponse(companyService.searchById(companyId));
    }

    @GetMapping("/{companyId}/employees")
    public List<EmployeeResponse> getEmployeesByCompanyId(@PathVariable("companyId") Integer companyId) {
        return employeeMapper.toResponseList(companyService.getEmployeesByCompanyId(companyId));
    }

    @PutMapping("/{companyId}")
    public Company update(@PathVariable("companyId") Integer companyId, @RequestBody Company updatedCompany) {
        return companyService.update(companyId, updatedCompany);
    }

    @DeleteMapping("/{companyId}")
    public void delete(@PathVariable("companyId") Integer companyId) {
        companyService.delete(companyId);
    }

    @GetMapping(params = {"page", "pageSize"})
    public List<Company> getCompaniesByPageAndPageSize(@RequestParam("page") Integer page,
                                                       @RequestParam("pageSize") Integer pageSize) {
        return companyService.getCompaniesByPageAndPageSize(page, pageSize);
    }
}
