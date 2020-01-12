package com.codegym.cms.service.impl;

import com.codegym.cms.model.Department;
import com.codegym.cms.repository.DepartmentRepository;
import com.codegym.cms.repository.EmployeeRepository;
import com.codegym.cms.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;

public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    EmployeeRepository employeeRepository;

    @Override
    public Iterable<Department> findAll() {
       return departmentRepository.findAll();
    }

    @Override
    public Department findById(Long id) {
        return departmentRepository.findOne(id);
    }

    @Override
    public void save(Department department) {
departmentRepository.save(department);
    }

    @Override
    public void remove(Long id) {
departmentRepository.delete(id);
    }
}

