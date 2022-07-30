package org.xiaogang.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.xiaogang.server.common.R;
import org.xiaogang.server.entity.Employee;
import org.xiaogang.server.service.EmployeeService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

/**
 * className: EmployeeController
 * description:
 * author: xiaopangio
 * date: 2022/7/25 6:51
 * version: 1.0
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, HttpServletResponse response, @RequestBody Employee employee) {
        String password = employee.getPassword();
//        md5加密
        password = DigestUtils.md5DigestAsHex(password.getBytes());
//        封装查询条件
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
//        查询
        Employee emp = employeeService.getOne(queryWrapper);
        if (emp == null) {
            return R.error("用户名不存在");
        }
        if (!password.equals(emp.getPassword())) {
            return R.error("密码错误");
        }
        if (emp.getStatus() == 0) {
            return R.error("账号已被冻结");
        }
        request.getSession().setAttribute("employee", emp.getId());
        ResponseCookie cookie = ResponseCookie.from("JSESSIONID", request.getSession().getId()) // key & value
                .httpOnly(true)		// 禁止js读取
                .secure(true)		// 在http下也传输
//                    .domain("localhost")// 域名
                .path("/")			// path
                .maxAge(1000*60*30)	// 有效期
                .sameSite("None")	// 大多数情况也是不发送第三方 Cookie，但是导航到目标网址的 Get 请求除外
                .build()
                ;
        // 设置Cookie Header
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return R.success(emp);
    }

    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {
//       初始化密码
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        Long employeeId = (Long) request.getSession().getAttribute("employee");
        employeeService.save(employee);
        return R.success("新增员工成功");
    }

    @GetMapping("/page")
    public R<Page<Employee>> getList(int page, int pageSize, String name) {
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
//        姓名过滤条件
        queryWrapper.like(!StringUtils.isEmpty(name), Employee::getName, name);
//        按更新时间降序排序
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        Page<Employee> pageinfo = new Page<>(page, pageSize);
        employeeService.page(pageinfo, queryWrapper);
        return R.success(pageinfo);
    }
    @PutMapping
    public R<String>update(HttpServletRequest request,@RequestBody Employee employee){
        employee.setUpdateTime(LocalDateTime.now());
        Long empId = (Long) request.getSession().getAttribute("employee");
        employee.setUpdateUser(empId);
        employeeService.updateById(employee);
        return R.success("修改员工成功");
    }
    @GetMapping("/{id}")
    public R<Employee > getById(@PathVariable Long id){
        Employee employee = employeeService.getById(id);
        if(employee == null){
            return R.error("员工不存在");
        }
        return R.success(employee);
    }
}
