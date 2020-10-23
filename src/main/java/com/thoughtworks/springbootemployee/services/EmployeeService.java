package com.thoughtworks.springbootemployee.services;

import com.thoughtworks.springbootemployee.exceptions.EmployeeNotFoundException;
import com.thoughtworks.springbootemployee.exceptions.InvalidEmployeeException;
import com.thoughtworks.springbootemployee.models.Employee;
import com.thoughtworks.springbootemployee.repository.IEmployeeRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.*;

@Service
public class EmployeeService {

    private IEmployeeRepository employeeRepository;

    public EmployeeService(IEmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAll() {
        return employeeRepository.findAll();
    }

    public Employee create(Employee newEmployee) {
        return employeeRepository.save(newEmployee);
    }

    public Employee searchById(Integer id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee with id:" + id + " not found"));
    }

    public Employee update(Integer id, Employee employee) {
        validateEmployeeUpdate(employee);

        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isPresent()) {
            optionalEmployee.get().setSalary(employee.getSalary());
            optionalEmployee.get().setAge(employee.getAge());
            optionalEmployee.get().setGender(employee.getGender());
            optionalEmployee.get().setName(employee.getName());
            return employeeRepository.save(optionalEmployee.get());
        }
        return null;
    }

    private void validateEmployeeUpdate(Employee employee)
    {
        if(isNull(employee.getGender())
                || isNull(employee.getAge())
                || isNull(employee.getSalary())
                || isNull(employee.getName())) {
            throw new InvalidEmployeeException("Employee given has null fields!");
        }
    }

    public void delete(Integer id) {
        employeeRepository.deleteById(id);
    }

    public List<Employee> searchByGender(String gender) {
        return employeeRepository.findByGender(gender);
    }

    public List<Employee> getEmployeeByPageAndPageSize(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);

        return employeeRepository.findAll(pageable).toList();
    }
}
