package com.codegym.cms.service;

import com.codegym.cms.model.Department;

public interface DepartmentService {
    Iterable<Department> findAll();
    Department findById(Long id);
    void save(Department department);
    void remove(Long id);
}
