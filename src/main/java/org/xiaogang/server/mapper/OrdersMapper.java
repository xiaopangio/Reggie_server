package org.xiaogang.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.xiaogang.server.entity.Orders;
@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {
}
