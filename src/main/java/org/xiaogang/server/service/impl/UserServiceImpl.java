package org.xiaogang.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.xiaogang.server.entity.User;
import org.xiaogang.server.mapper.UserMapper;
import org.xiaogang.server.service.UserService;

/**
 * className: UserServiceImpl
 * description:
 * author: xiaopangio
 * date: 2022/7/27 17:42
 * version: 1.0
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
