package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import java.util.List;

import com.sky.enumeration.OperationType;
import com.sky.result.PageResult;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    @Update("update employee set password = #{newPassword} where id = #{empId} and password = #{oldPassword}")
    int updatePassword(PasswordEditDTO passwordEditDTO);

    @Update("update employee set status = #{status} where id = #{id}")
    int modifyStatus(Long id, int status);

    Page<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    @Insert("insert into employee (id, id_number, name, phone, sex, username, " +
            "password, create_user, create_time, update_user, update_time) " +
            "values(#{id}, #{idNumber}, #{name}, #{phone}, #{sex}, #{username}," +
            " #{password}, #{createUser}, #{createTime}, #{updateUser}, #{updateTime})")
    @AutoFill(OperationType.INSERT)
    int add(Employee employee);

    @Select("select * from employee where id = #{id}")
    Employee getById(Long id);

    @AutoFill(OperationType.UPDATE)
    int modify(Employee employee);
}
