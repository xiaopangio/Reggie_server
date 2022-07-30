package org.xiaogang.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.xiaogang.server.entity.OrderDetail;
import org.xiaogang.server.mapper.OrderDetailMapper;
import org.xiaogang.server.service.OrderDetailService;

/**
 * className: OrderDetailServiceImpl
 * description:
 * author: xiaopangio
 * date: 2022/7/29 16:07
 * version: 1.0
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
