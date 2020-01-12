package com.codegym.cms.repository;

import com.codegym.cms.model.Department;
import com.codegym.cms.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface EmployeeRepository extends PagingAndSortingRepository<Employee, Long> {
    Iterable<Employee> findAllByDepartment(Department department);

    Page<Employee> findAllByDepartmentName(String name, Pageable pageable);
}
