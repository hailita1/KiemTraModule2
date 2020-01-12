package com.codegym.cms.controller;

import com.codegym.cms.model.Department;
import com.codegym.cms.model.Employee;
import com.codegym.cms.model.EmployeeForm;
import com.codegym.cms.service.DepartmentService;
import com.codegym.cms.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

@Controller
@PropertySource("classpath:global_config_app.properties")
public class EmployeeController {
    @Autowired
    private Environment env;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private EmployeeService employeeService;

    @ModelAttribute("department")
    public Iterable<Department> departments() {
        return departmentService.findAll();
    }

    @GetMapping("/employees")
    public ModelAndView listEmployees(@RequestParam("s") Optional<String> s, @PageableDefault(value = 2, sort = "salary") Pageable pageable) {
        Page<Employee> employees;
        if (s.isPresent()) {
            employees = employeeService.findAllByDepartmentName(s.get(), pageable);

        } else {
            employees = employeeService.findAll(pageable);
        }
        ModelAndView modelAndView = new ModelAndView("/employee/list");
        modelAndView.addObject("employee", employees);
        return modelAndView;
    }

    @GetMapping("/create-employee")
    public ModelAndView showCreateForm() {
        ModelAndView modelAndView = new ModelAndView("/employee/create");
        modelAndView.addObject("employeeForm", new EmployeeForm());
        return modelAndView;
    }

    @PostMapping("/create-employee")
    public ModelAndView saveEmployee(@Validated @ModelAttribute("employeeForm") EmployeeForm employeeForm, BindingResult result) {

        if (result.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("/employee/create");
            modelAndView.addObject("employeeForm", new EmployeeForm());
            modelAndView.addObject("message", "\n" +
                    "Wrong data type of field birthDate !!!");
            return modelAndView;
        }

        MultipartFile multipartFile = employeeForm.getAvatar();
        String fileName = multipartFile.getOriginalFilename();
        String fileUpload = env.getProperty("file_upload").toString();

        try {
            FileCopyUtils.copy(employeeForm.getAvatar().getBytes(), new File(fileUpload + fileName));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Employee employee = new Employee(employeeForm.getName(), employeeForm.getBirthDate(),
                employeeForm.getAddress(), fileName, employeeForm.getSalary(), employeeForm.getDepartment());
        employeeService.save(employee);

        ModelAndView modelAndView = new ModelAndView("/employee/create");
        modelAndView.addObject("employee", new EmployeeForm());
        modelAndView.addObject("message", "Employee create successfully !!!");
        return modelAndView;

    }

    @GetMapping("/edit-employee/{id}")
    public ModelAndView showEditForm(@PathVariable Long id) {
        Employee employee = employeeService.findById(id);
        if (employee != null) {
            ModelAndView modelAndView = new ModelAndView("/employee/edit");
            modelAndView.addObject("employee", employee);
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("/error.404");
            return modelAndView;
        }
    }

    @PostMapping("/edit-employee")
    public ModelAndView updateEmployee(@ModelAttribute("employee") EmployeeForm employeeForm, BindingResult result) {
        MultipartFile multipartFile = employeeForm.getAvatar();
        String fileName = multipartFile.getOriginalFilename();
        String fileUpload = env.getProperty("file_upload").toString();
        try {
            FileCopyUtils.copy(employeeForm.getAvatar().getBytes(), new File(fileUpload + fileName));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Employee employee = employeeService.findById(employeeForm.getId());

        employee.setName(employeeForm.getName());
        employee.setDepartment(employeeForm.getDepartment());
        employee.setAvatar(fileName);
        employee.setSalary(employeeForm.getSalary());
        employee.setAddress(employeeForm.getAddress());
        employee.setBirthDate(employeeForm.getBirthDate());

        if (result.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("/employee/edit");
            modelAndView.addObject("employeeForm", new EmployeeForm());
            modelAndView.addObject("message", "\n" +
                    "Wrong data type of field birthDate !!!");
            return modelAndView;
        }
        if (employee != null) {
            employeeService.save(employee);
            ModelAndView modelAndView = new ModelAndView("/employee/edit");
            modelAndView.addObject("employee", employee);
            modelAndView.addObject("message", "Employee updated successfully");
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("/error.404");
            return modelAndView;

        }
    }

    @GetMapping("/delete-employee/{id}")
    public ModelAndView showDeleteForm(@PathVariable Long id) {
        Employee employee = employeeService.findById(id);
        if (employee != null) {
            ModelAndView modelAndView = new ModelAndView("/employee/delete");
            modelAndView.addObject("employee", employee);
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("/error.404");
            return modelAndView;
        }
    }

    @PostMapping("/delete-employee")
    public String deleteCustomer(@ModelAttribute("employee") Employee employee) {
        employeeService.remove(employee.getId());
        return "redirect:employees";
    }

}
