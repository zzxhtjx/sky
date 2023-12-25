package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

import java.util.List;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    boolean editPassword(PasswordEditDTO passwordEditDTO);

    boolean modifyStatus(Long id, int status);

    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    boolean add(Employee employee);

    Employee getById(Long id);

    boolean modify(Employee employee);
}
