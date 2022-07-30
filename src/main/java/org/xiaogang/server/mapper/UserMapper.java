package org.xiaogang.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.xiaogang.server.entity.User;

@Mapper
public interface UserMapper  extends BaseMapper<User> {
}
