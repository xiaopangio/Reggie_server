package org.xiaogang.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.xiaogang.server.entity.Employee;
import org.xiaogang.server.mapper.EmployeeMapper;
import org.xiaogang.server.service.EmployeeService;

/**
 * className: EmployeeServiceImpl
 * description: 员工服务
 * author: xiaopangio
 * date: 2022/7/25 6:49
 * version: 1.0
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {


}
