package com.codegym.cms.service;

import com.codegym.cms.model.Department;
import com.codegym.cms.model.Employee;
import com.codegym.cms.model.EmployeeForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {
    Page<Employee> findAll(Pageable pageable);

    Employee findById(Long id);

    void save(Employee employee);

    void remove(Long id);

    Iterable<Employee> findAllByDepartment(Department department);

    Page<Employee> findAllByDepartmentName(String s, Pageable pageable);
}
