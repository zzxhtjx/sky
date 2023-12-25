package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.constant.PasswordConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "员工管理")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation("员工登录")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    // TODO 跳过token验证的拦截,使用swagger进行测试的时候需要token
    // TODO 前端提供的DTO是没有Id信息的,需要从jwt解析获得empId
    @PutMapping("/editPassword")
    @ApiOperation("修改员工密码")
    public Result<String> editPassword(@RequestHeader("token") String token, @RequestBody PasswordEditDTO passwordEditDTO){
        try {
            Claims claims = JwtUtil.parseJWT(jwtProperties.getAdminSecretKey(), token);
            Long empId = Long.valueOf(claims.get(JwtClaimsConstant.EMP_ID).toString());
            passwordEditDTO.setEmpId(empId);
        } catch (Exception ex) {
            return Result.error("the token check fail");
        }
        if(employeeService.editPassword(passwordEditDTO)) {
            return Result.success();
        }else return Result.error("modify the password of id : " + passwordEditDTO.getEmpId()
                + " fail");
    }

    // TODO 实现启用还是禁用账号
    @PostMapping("/status/{status}")
    @ApiOperation("修改员工状态")
        Result<String> modifyStatus(@RequestParam Long id, @PathVariable int status){
        if(employeeService.modifyStatus(id, status)){
            return Result.success();
        }else return Result.error("modify the state of id : " + id + " fail");
    }

    // TODO 实现分页查询, 问题: data接口文档前端需要的是size + list<object>, 目前返回的值是list
    // 这个String的类型与框架集合自动转换成List的关系
    @GetMapping("/page")
    @ApiOperation("员工分页查询")
    Result<PageResult> pageQuery(EmployeePageQueryDTO employeePageQueryDTO){
        PageResult data = employeeService.pageQuery(employeePageQueryDTO);
        if(data == null){
            return Result.error("page query fail");
        }else {
            return Result.success(data);
        }
    }

    // TODO 新增员工,密码默认123456在数据持久层直接传入
    @PostMapping
    @ApiOperation("新增员工")
    Result<String>  add(@RequestBody EmployeeDTO employeeDTO) throws Exception{
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        employee.setPassword(PasswordConstant.DEFAULT_PASSWORD);
        employee.setCreateUser(BaseContext.getCurrentId());
        if(employeeService.add(employee)){
            return Result.success();
        }else {
            return Result.error("add employee fail");
        }
    }

    // TODO 根据id查询员工
    @GetMapping("{id}")
    @ApiOperation("根据id查询员工")
    Result<Employee> getById(@PathVariable Long id){
        Employee emp = employeeService.getById(id);
        if(emp != null){
            return Result.success(emp);
        }else {
            return Result.error("query id for " + id + " fail");
        }
    }

    // TODO 编辑员工信息
    @PutMapping
    @ApiOperation("修改员工信息")
    Result<String> modify(@RequestBody EmployeeDTO employeeDTO){
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        employee.setUpdateUser(BaseContext.getCurrentId());
        if(employeeService.modify(employee)){
            return Result.success();
        }else {
            return Result.error("modify employee fail");
        }
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation("员工退出")
    public Result<String> logout() {
        return Result.success();
    }

}
